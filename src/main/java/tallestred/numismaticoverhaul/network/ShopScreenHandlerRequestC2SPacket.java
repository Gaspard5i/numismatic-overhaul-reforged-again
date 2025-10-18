package tallestred.numismaticoverhaul.network;

import io.wispforest.owo.network.ServerAccess;
import tallestred.numismaticoverhaul.block.ShopScreenHandler;

public record ShopScreenHandlerRequestC2SPacket(Action action, long value) {

    public ShopScreenHandlerRequestC2SPacket(Action action) {
        this(action, 0);
    }

    public static void handle(ShopScreenHandlerRequestC2SPacket message, ServerAccess access) {
        final var player = access.player();
        final long value = message.value();

        if (!(player.containerMenu instanceof ShopScreenHandler shopHandler)) return;

        switch (message.action()) {
            case LOAD_OFFER -> shopHandler.loadOffer(value);
            case CREATE_OFFER -> shopHandler.createOffer(value);
            case DELETE_OFFER -> shopHandler.deleteOffer();
            case EXTRACT_CURRENCY -> shopHandler.extractCurrency();
            case TOGGLE_TRANSFER -> shopHandler.toggleTransfer();
            case SET_BUFFER_COUNT -> shopHandler.setBufferCount((int) value);
            case SET_BUFFER_FROM_HELD -> shopHandler.setBufferFromHeld();
            case SET_BUFFER_FROM_CARRIED -> shopHandler.setBufferFromCarried();
            case MOVE_SHOP_SLOT_TO_PLAYER -> shopHandler.moveShopSlotToPlayer((int) value);
        }
    }

    public enum Action {
        CREATE_OFFER, DELETE_OFFER, LOAD_OFFER, EXTRACT_CURRENCY, TOGGLE_TRANSFER,
        SET_BUFFER_COUNT, SET_BUFFER_FROM_HELD, SET_BUFFER_FROM_CARRIED,
        MOVE_SHOP_SLOT_TO_PLAYER
    }

}