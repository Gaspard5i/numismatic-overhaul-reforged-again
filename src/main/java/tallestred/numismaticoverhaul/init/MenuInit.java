package tallestred.numismaticoverhaul.init;

import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.block.PiggyBankScreenHandler;
import tallestred.numismaticoverhaul.block.ShopScreenHandler;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MenuInit {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, NumismaticOverhaul.MODID);
    public static final RegistryObject<MenuType<ShopScreenHandler>> SHOP = MENU_TYPES.register("shop", () -> IForgeMenuType.create(ShopScreenHandler::new));
    public static final RegistryObject<MenuType<PiggyBankScreenHandler>> PIGGY_BANK = MENU_TYPES.register("piggy_bank", () -> new MenuType<>(PiggyBankScreenHandler::new, FeatureFlags.DEFAULT_FLAGS));
}
