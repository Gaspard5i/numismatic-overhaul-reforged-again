package tallestred.numismaticoverhaul.cap;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import tallestred.numismaticoverhaul.currency.CurrencyConverter;
import tallestred.numismaticoverhaul.init.DataAttachmentInit;
import tallestred.numismaticoverhaul.item.CoinItem;

import java.util.ArrayList;
import java.util.List;

public class CurrencyHolder  {
    public static long getValue(Player player) {
        return player.getData(DataAttachmentInit.VALUE.get());
    }

    public static void setValue(Player player, long value) {
        player.setData(DataAttachmentInit.VALUE.get(), value);
    }
    public static void silentModify(Player player, long value) {
        setValue(player,getValue(player) + value);
    }
    public static Long popTransaction(ArrayList<Long> transactions) {
        return transactions.remove(transactions.size() - 1);
    }
    public static void pushTransaction(ArrayList<Long> transactions, long value) {
        transactions.add(value);
    }

    public static void modify(Player player, long value) {
        setValue(player, getValue(player) + value);

        long tempValue = value < 0 ? -value : value;

        List<ItemStack> transactionStacks = CurrencyConverter.getAsItemStackList(tempValue);
        if (transactionStacks.isEmpty()) return;

        MutableComponent message = value < 0 ? net.minecraft.network.chat.Component.literal("§c- ") : net.minecraft.network.chat.Component.literal("§a+ ");
        message.append(net.minecraft.network.chat.Component.literal("§7["));
        for (ItemStack stack : transactionStacks) {
            message.append(net.minecraft.network.chat.Component.literal("§b" + stack.getCount() + " "));
            message.append(net.minecraft.network.chat.Component.translatable("currency.numismaticoverhaul." + ((CoinItem) stack.getItem()).currency.name().toLowerCase()));
            if (transactionStacks.indexOf(stack) != transactionStacks.size() - 1) message.append(net.minecraft.network.chat.Component.literal(", "));
        }
        message.append(net.minecraft.network.chat.Component.literal("§7]"));

        player.displayClientMessage(message, true);
    }

    public static void commitTransactions(Player player) {
        modify(player, player.getData(DataAttachmentInit.TRANSACTIONS.get()).stream().mapToLong(Long::longValue).sum());
        player.getData(DataAttachmentInit.TRANSACTIONS.get()).clear();
    }
}
