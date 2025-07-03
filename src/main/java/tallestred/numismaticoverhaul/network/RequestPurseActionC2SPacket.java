package tallestred.numismaticoverhaul.network;

import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.cap.CurrencyHolder;
import tallestred.numismaticoverhaul.currency.CurrencyConverter;
import tallestred.numismaticoverhaul.currency.CurrencyHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.InventoryMenu;

public record RequestPurseActionC2SPacket(Action action, long value) implements CustomPacketPayload {
    public static final Type<RequestPurseActionC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(NumismaticOverhaul.MODID, "following"));
    public static final StreamCodec<FriendlyByteBuf, RequestPurseActionC2SPacket> STREAM_CODEC = StreamCodec.composite(new EnumStreamCodec<>(Action.class), RequestPurseActionC2SPacket::action,
            ByteBufCodecs.VAR_LONG, RequestPurseActionC2SPacket::value,
            RequestPurseActionC2SPacket::new
    );

    public static void handle(RequestPurseActionC2SPacket packet, IPayloadContext context) {
        final ServerPlayer player = (ServerPlayer) context.player();

        if (player.containerMenu instanceof InventoryMenu || isInventorioHandler(player)) {

            switch (packet.action) {
                case STORE_ALL -> CurrencyHolder.modify(player, CurrencyHelper.getMoneyInInventory(player, true));
                case EXTRACT -> {
                    //Check if we can actually extract this much money to prevent cheeky packet forgery
                    if (CurrencyHolder.getValue(player) < packet.value()) return;

                    CurrencyConverter.getAsItemStackList(packet.value()).forEach(stack -> player.getInventory().placeItemBackInInventory(stack));
                    CurrencyHolder.modify(player, -packet.value());
                }
                case EXTRACT_ALL -> {
                    CurrencyConverter.getAsValidStacks(CurrencyHolder.getValue(player))
                            .forEach(stack -> player.getInventory().placeItemBackInInventory(stack));

                    CurrencyHolder.modify(player, -CurrencyHolder.getValue(player));
                }
            }
        }
    }

    private static boolean isInventorioHandler(ServerPlayer player) {
        return false;
//                FabricLoader.getInstance().isModLoaded("inventorio")
//                && player.currentScreenHandler.getClass().getName().equals("me.lizardofoz.inventorio.player.InventorioScreenHandler");
    }

    public static RequestPurseActionC2SPacket storeAll() {
        return new RequestPurseActionC2SPacket(Action.STORE_ALL, 0);
    }

    public static RequestPurseActionC2SPacket extractAll() {
        return new RequestPurseActionC2SPacket(Action.EXTRACT_ALL, 0);
    }

    public static RequestPurseActionC2SPacket extract(long amount) {
        return new RequestPurseActionC2SPacket(Action.EXTRACT, amount);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public enum Action {
        STORE_ALL, EXTRACT, EXTRACT_ALL
    }
}
