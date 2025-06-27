package tallestred.numismaticoverhaul.mixin;

import tallestred.numismaticoverhaul.cap.CurrencyHolderAttacher;
import tallestred.numismaticoverhaul.client.gui.purse.PurseButton;
import tallestred.numismaticoverhaul.client.gui.purse.PurseWidget;
import tallestred.numismaticoverhaul.config.NOClientConfig;
import tallestred.numismaticoverhaul.network.NetworkHandler;
import tallestred.numismaticoverhaul.network.RequestPurseActionC2SPacket;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {
    public InventoryScreenMixin(InventoryMenu screenHandler, Inventory playerInventory, Component text) {
        super(screenHandler, playerInventory, text);
    }

    private PurseWidget numismatic$purse;
    private PurseButton numismatic$button;

    //The purse is injected via mixin instead of event because I need special callbacks in render(...) and mouseClicked(...) to handle
    //the non-button widget anyway, so I can just inject them here

    @Inject(method = "init", at = @At("TAIL"))
    public void addButton(CallbackInfo ci) {
        int purseX = NOClientConfig.CLIENT.pursePositionX.get();
        int purseY = NOClientConfig.CLIENT.pursePositionY.get();

        numismatic$purse = new PurseWidget(this.leftPos + purseX, this.topPos + purseY, minecraft, CurrencyHolderAttacher.getExampleHolderUnwrap(minecraft.player), this.minecraft.renderBuffers().bufferSource());

        numismatic$button = new PurseButton(this.leftPos + purseX + 29, this.topPos + purseY - 14, button -> {
            if (Screen.hasShiftDown()) {
                NetworkHandler.INSTANCE.sendToServer(RequestPurseActionC2SPacket.storeAll());
            } else {
                numismatic$purse.toggleActive();
            }
        }, minecraft.player, this);

        this.addRenderableWidget(numismatic$button);
    }

    //Incredibly beautiful lambda mixin
    @Inject(method = {"lambda$init$0"}, at = @At("TAIL"))
    private void updateWidgetPosition(Button button, CallbackInfo ci) {
        this.numismatic$button.setPosition(this.leftPos + 158, this.topPos + 6);
        this.numismatic$purse = new PurseWidget(this.leftPos + 129, this.topPos + 20, minecraft, CurrencyHolderAttacher.getExampleHolderUnwrap(minecraft.player), minecraft.renderBuffers().bufferSource());
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void onRender(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        numismatic$purse.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void onMouse(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (numismatic$purse.mouseClicked(mouseX, mouseY, button)) cir.setReturnValue(true);
    }

    @Override
    protected void renderTooltip(GuiGraphics matrices, int x, int y) {
        if (numismatic$purse.isMouseOver(x, y)) return;
        super.renderTooltip(matrices, x, y);
    }

}
