package tallestred.numismaticoverhaul.mixin.owomixins.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(GuiGraphics.class)
public interface ScreenInvoker {

    @Invoker("renderTooltipInternal")
    void owo$renderTooltipFromComponents(Font pFont, List<ClientTooltipComponent> pComponents, int pMouseX, int pMouseY, ClientTooltipPositioner pTooltipPositioner);

}
