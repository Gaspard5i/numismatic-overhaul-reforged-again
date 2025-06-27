package tallestred.numismaticoverhaul.mixin.owomixins.ui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import tallestred.numismaticoverhaul.owostuff.ui.base.BaseOwoHandledScreen;
import tallestred.numismaticoverhaul.owostuff.util.pond.OwoSlotExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(AbstractContainerScreen.class)
public class HandledScreenMixin {

    @Unique
    private static boolean owo$inOwoScreen = false;

    @Unique
    private Slot owo$lastClickedSlot = null;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "render", at = @At("HEAD"))
    private void captureOwoState(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        owo$inOwoScreen = (Object) this instanceof BaseOwoHandledScreen<?, ?>;
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void resetOwoState(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick, CallbackInfo ci) {
        owo$inOwoScreen = false;
    }

    @Inject(method = "renderSlot", at = @At("HEAD"))
    private void injectSlotScissors(GuiGraphics pGuiGraphics, Slot pSlot, CallbackInfo ci) {
        if (!owo$inOwoScreen) return;

        var scissorArea = ((OwoSlotExtension) pSlot).owo$getScissorArea();
        if (scissorArea == null) return;

        GlStateManager._enableScissorTest();
        GlStateManager._scissorBox(scissorArea.x(), scissorArea.y(), scissorArea.width(), scissorArea.height());
    }

    @Inject(method = "renderSlot", at = @At("RETURN"))
    private void clearSlotScissors(GuiGraphics pGuiGraphics, Slot pSlot, CallbackInfo ci) {
        if (!owo$inOwoScreen) return;

        var scissorArea = ((OwoSlotExtension) pSlot).owo$getScissorArea();
        if (scissorArea == null) return;

        GlStateManager._disableScissorTest();
    }

    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V", shift = At.Shift.AFTER))
    private static void enableSlotDepth(GuiGraphics p_283692_, int p_281453_, int p_281915_, int p_283504_, int color, CallbackInfo ci) {
        if (!owo$inOwoScreen) return;
        RenderSystem.enableDepthTest();
        p_283692_.pose().translate(0, 0, 300);
    }

    @Inject(method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;III)V", at = @At("TAIL"))
    private static void clearSlotDepth(GuiGraphics pGuiGraphics, int pX, int pY, int pBlitOffset, CallbackInfo ci) {
        if (!owo$inOwoScreen) return;
        pGuiGraphics.pose().translate(0, 0, -300);
    }

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;getMillis()J"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void captureClickedSlot(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir, InputConstants.Key mouseKey, boolean bl, Slot slot) {
        this.owo$lastClickedSlot = slot;
    }

    @ModifyVariable(method = "mouseClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;", ordinal = 0), ordinal = 3)
    private int doNoThrow(int slotId) {
        if (!((Object) this instanceof BaseOwoHandledScreen<?, ?>) || this.owo$lastClickedSlot == null) return slotId;
        return this.owo$lastClickedSlot.index;
    }

    @Inject(method = "mouseClicked", at = @At(value = "RETURN"))
    private void captureClickedSlot(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        this.owo$lastClickedSlot = null;
    }
}
