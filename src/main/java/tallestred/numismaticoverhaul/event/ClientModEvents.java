package tallestred.numismaticoverhaul.event;

import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.client.gui.CurrencyTooltipComponent;
import tallestred.numismaticoverhaul.item.CurrencyTooltipData;
import tallestred.numismaticoverhaul.owostuff.ui.parsing.UIModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NumismaticOverhaul.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEvents {

    @SubscribeEvent
    public static void onTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(CurrencyTooltipData.class, CurrencyTooltipComponent::new);
    }
    @SubscribeEvent
    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new UIModelLoader());
    }
}
