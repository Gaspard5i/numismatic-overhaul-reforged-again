package tallestred.numismaticoverhaul.owostuff.ui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class UISounds {

    public static final SoundEvent UI_INTERACTION = SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("owo", "ui.owo.interaction"));


    public static void playButtonSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
    }


    public static void playInteractionSound() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(UI_INTERACTION, 1));
    }

}
