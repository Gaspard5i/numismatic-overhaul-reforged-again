package tallestred.numismaticoverhaul.init;

import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.currency.Currency;
import tallestred.numismaticoverhaul.item.CoinItem;
import tallestred.numismaticoverhaul.item.MoneyBagItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static tallestred.numismaticoverhaul.init.CreativeTabInit.addToTab;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NumismaticOverhaul.MODID);
    public static final RegistryObject<Item> BRONZE_COIN = addToTab(ITEMS.register("bronze_coin", () -> new CoinItem(Currency.BRONZE)));
    public static final RegistryObject<Item> SILVER_COIN = addToTab(ITEMS.register("silver_coin", () -> new CoinItem(Currency.SILVER)));
    public static final RegistryObject<Item> GOLD_COIN = addToTab(ITEMS.register("gold_coin", () -> new CoinItem(Currency.GOLD)));
    public static final RegistryObject<Item> MONEY_BAG = addToTab(ITEMS.register("money_bag", MoneyBagItem::new));

}