package tallestred.numismaticoverhaul.init;

import tallestred.numismaticoverhaul.NumismaticOverhaul;
import tallestred.numismaticoverhaul.block.PiggyBankBlock;
import tallestred.numismaticoverhaul.block.PiggyBankBlockEntity;
import tallestred.numismaticoverhaul.block.ShopBlock;
import tallestred.numismaticoverhaul.block.ShopBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;
import java.util.function.Supplier;

import static tallestred.numismaticoverhaul.init.CreativeTabInit.addToTab;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NumismaticOverhaul.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, NumismaticOverhaul.MODID);

    public static final RegistryObject<Block> PIGGY_BANK = registerBlock("piggy_bank", PiggyBankBlock::new);
    // SHOP block
    public static final RegistryObject<Block> SHOP = registerBlock("shop", () -> new ShopBlock(false));
    //inexhastible shop block
    public static final RegistryObject<Block> INEXHAUSTIBLE_SHOP = registerBlock("inexhaustible_shop", () -> new ShopBlock(true));

    public static final RegistryObject<BlockEntityType<PiggyBankBlockEntity>> PIGGY_BANK_BE = BLOCK_ENTITIES.register("piggy_bank", () -> BlockEntityType.Builder.of(PiggyBankBlockEntity::new, PIGGY_BANK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ShopBlockEntity>> SHOP_BE = BLOCK_ENTITIES.register("shop", () -> BlockEntityType.Builder.of(ShopBlockEntity::new, SHOP.get(), INEXHAUSTIBLE_SHOP.get()).build(null));

    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return addToTab(registerBlock(name, block, b -> () -> new BlockItem(b.get(), new Item.Properties())));
    }

    protected static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<T>, Supplier<? extends BlockItem>> item) {
        var reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }
}


