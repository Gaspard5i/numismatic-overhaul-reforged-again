package tallestred.numismaticoverhaul.network;

import io.wispforest.owo.network.ClientAccess;
import tallestred.numismaticoverhaul.block.ShopBlockEntity;
import tallestred.numismaticoverhaul.block.ShopOffer;
import tallestred.numismaticoverhaul.client.gui.ShopScreen;

import java.util.List;

public record UpdateShopScreenS2CPacket(List<ShopOffer> offers, long storedCurrency, boolean transferEnabled) {

    public UpdateShopScreenS2CPacket(ShopBlockEntity shop) {
        this(shop.getOffers(), shop.getStoredCurrency(), shop.isTransferEnabled());
    }

    public static void handle(UpdateShopScreenS2CPacket message, ClientAccess access) {
        if (!(access.runtime().screen instanceof ShopScreen screen)) return;
        screen.update(message);
    }
}
