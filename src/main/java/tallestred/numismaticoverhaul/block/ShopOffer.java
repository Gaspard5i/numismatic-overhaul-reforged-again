package tallestred.numismaticoverhaul.block;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.trading.ItemCost;
import tallestred.numismaticoverhaul.currency.CurrencyConverter;
import tallestred.numismaticoverhaul.currency.CurrencyHelper;
import tallestred.numismaticoverhaul.item.MoneyBagItem;
import tallestred.numismaticoverhaul.villagers.data.NumismaticTradeOfferExtensions;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;

import java.util.List;

public class ShopOffer {

    private final ItemStack sell;
    private final long price;

    public ShopOffer(ItemStack sell, long price) {

        if (sell.isEmpty()) throw new IllegalArgumentException("Sell Stack must not be empty");
        if (price == 0) throw new IllegalArgumentException("Price must not be null");

        this.sell = sell;
        this.price = price;
    }

    @SuppressWarnings("ConstantConditions")
    public MerchantOffer toTradeOffer(ShopBlockEntity shop, boolean inexhaustible) {
        boolean isPocketChange = CurrencyConverter.getRequiredCurrencyTypes(price) == 1;
        var buyStack = isPocketChange ? CurrencyConverter.getAsItemStackList(price).getFirst() : MoneyBagItem.fromRawValue(price);
        int maxUses = inexhaustible ? Integer.MAX_VALUE : count(shop.getItems(), sell) / sell.getCount();
        var tradedItem = isPocketChange ? new ItemCost(buyStack.getItem(), (int) price) : new ItemCost(buyStack.getItemHolder(), 1, DataComponentPredicate.EMPTY, buyStack);
        return new MerchantOffer(tradedItem, sell, maxUses, 0, 0);
    }

    public long getPrice() {
        return price;
    }

    public ItemStack getSellStack() {
        return sell.copy();
    }

    public static CompoundTag writeAll(CompoundTag tag, List<ShopOffer> offers, HolderLookup.Provider access) {

        ListTag offerList = new ListTag();

        for (ShopOffer offer : offers) {
            offerList.add(offer.toNbt(access));
        }

        tag.put("Offers", offerList);

        return tag;
    }

    public static void readAll(CompoundTag tag, List<ShopOffer> offers, HolderLookup.Provider access) {
        offers.clear();

        ListTag offerList = tag.getList("Offers", Tag.TAG_COMPOUND);

        for (Tag offerTag : offerList) {
            offers.add(fromNbt(access, (CompoundTag) offerTag));
        }
    }

    public CompoundTag toNbt(HolderLookup.Provider access) {
        var nbt = new CompoundTag();
        nbt.putLong("Price", this.price);

        var itemNbt = new CompoundTag();
        this.sell.save(access, itemNbt);

        nbt.put("Item", itemNbt);
        return nbt;
    }

    public static ShopOffer fromNbt(HolderLookup.Provider access, CompoundTag nbt) {
        var item = ItemStack.parseOptional(access, nbt.getCompound("Item"));
        return new ShopOffer(item, nbt.getLong("Price"));
    }

    public static int count(NonNullList<ItemStack> stacks, ItemStack testStack) {
        int count = 0;
        for (var stack : stacks) {
            if (!ItemStack.matches(stack, testStack)) continue;
            count += stack.getCount();
        }
        return count;
    }

    public static int remove(NonNullList<ItemStack> stacks, ItemStack removeStack) {
        int toRemove = removeStack.getCount();
        for (var stack : stacks) {
            if (!ItemStack.matches(stack, removeStack)) continue;

            int removed = stack.getCount();
            stack.shrink(toRemove);

            toRemove -= removed;
            if (toRemove < 1) break;
        }
        return removeStack.getCount() - toRemove;
    }

    @Override
    public String toString() {
        return this.sell + "@" + this.price + "coins";
    }
}
