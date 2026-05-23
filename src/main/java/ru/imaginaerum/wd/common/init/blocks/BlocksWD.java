package ru.imaginaerum.wd.common.init.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.*;
import ru.imaginaerum.wd.common.init.items.ItemsWD;

import java.util.function.Supplier;

public class BlocksWD {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(WD.MOD_ID);
    // Ягоды
    public static final DeferredBlock<Block> POISON_BERRY = BLOCKS.register("poison_berry",
            () -> new PoisonBerries(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));
    public static final DeferredBlock<Block> CHARMING_BERRIES_BLOCK = BLOCKS.register("charming_berries_block",
            () -> new CharmingBerries(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.SWEET_BERRY_BUSH).noCollission()));
    public static final DeferredBlock<Block> FREEZE_BERRIES = BLOCKS.register("freeze_berries",
            () -> new FreezeBerries(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.SWEET_BERRY_BUSH).noCollission()));
    // Растения
    public static final DeferredBlock<Block> FIRE_STEM = BLOCKS.register("fire_stem",
            () -> new FireRod(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> COASTAL_STEEP = BLOCKS.register("coastal_steep",
            () -> new CoastalSteepBlock(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> ROSE_OF_GHOSTY_TEARS = BLOCKS.register("rose_of_ghosty_tears",
            () -> new SoulRose(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> ROSE_OF_THE_MURDERER = BLOCKS.register("rose_of_the_murderer",
            () -> new RoseMurderer(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> SPATIAL_ORCHID = BLOCKS.register("spatial_orchid",
            () -> new SpatialOrchid(BlockBehaviour.Properties.of().noCollission()
                    .randomTicks().instabreak().sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> WARPED_WART = BLOCKS.register("warped_wart",
            () -> new WarpedWartBlock(BlockBehaviour.Properties.of().noCollission()
                    .randomTicks().instabreak().sound(SoundType.GRASS)));

    // Варенье
    public static final DeferredBlock<Block> POISON_BERRY_JAM = BLOCKS.register("poison_berry_jam",
            () -> new PoisonJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.POISON_BERRY_JAM));
    public static final DeferredBlock<Block> FREEZE_JAM = BLOCKS.register("freeze_jam",
            () -> new FreezeJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.FREEZE_JAM));
    public static final DeferredBlock<Block> CHARMING_JAM = BLOCKS.register("charming_jam",
            () -> new CharmingJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.CHARMING_JAM));
    public static final DeferredBlock<Block> SWEET_JAM = BLOCKS.register("sweet_jam",
            () -> new SweetJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.SWEET_JAM));
    public static final DeferredBlock<Block> APPLE_JAM = BLOCKS.register("apple_jam",
            () -> new AppleJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.APPLE_JAM));
    public static final DeferredBlock<Block> JAM_TONIC = BLOCKS.register("jam_tonic",
            () -> new TonicJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.JAM_TONIC));
    public static final DeferredBlock<Block> JAM_INVISIBILITY = BLOCKS.register("jam_invisibility",
            () -> new InvisibilityJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.JAM_INVISIBILITY));
    public static final DeferredBlock<Block> LEVITAN_JAM = BLOCKS.register("levitan_jam",
            () -> new LevitanJamBlock(BlockBehaviour.Properties.of()
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion(), ItemsWD.LEVITAN_JAM));
    public static final DeferredBlock<Block> GLOWING_JAM = BLOCKS.register("glowing_jam",
            () -> new GlowingJamBlock(BlockBehaviour.Properties.of().lightLevel((i) -> 7)
                    .strength(0.2F).sound(SoundType.GLASS).noOcclusion()));

    // Вафли
    public static final DeferredBlock<Block> WAFFLES = BLOCKS.register("waffles",
            () -> new WafflesBlock(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.WAFFLES));
    public static final DeferredBlock<Block> BERRIES_WAFFLES = BLOCKS.register("berries_waffles",
            () -> new BerriesWaffles(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.BERRIES_WAFFLES, BerriesWaffles.WaffleType.BERRIES));
    public static final DeferredBlock<Block> APPLE_WAFFLES = BLOCKS.register("apple_waffles",
            () -> new BerriesWaffles(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.APPLE_WAFFLES, BerriesWaffles.WaffleType.APPLE));
    public static final DeferredBlock<Block> ICE_WAFFLES = BLOCKS.register("ice_waffles",
            () -> new BerriesWaffles(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.ICE_WAFFLES, BerriesWaffles.WaffleType.ICE));
    public static final DeferredBlock<Block> POISON_WAFFLES = BLOCKS.register("poison_waffles",
            () -> new BerriesWaffles(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.POISON_WAFFLES, BerriesWaffles.WaffleType.POISON));
    public static final DeferredBlock<Block> CHARMING_WAFFLES = BLOCKS.register("charming_waffles",
            () -> new BerriesWaffles(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.CHARMING_WAFFLES, BerriesWaffles.WaffleType.CHARMING));
    public static final DeferredBlock<Block> GLOW_BERRIES_WAFFLES = BLOCKS.register("glow_berries_waffles",
            () -> new BerriesWaffles(BlockBehaviour.Properties.of().randomTicks().sound(SoundType.WOOL),
                    ItemsWD.GLOW_BERRIES_WAFFLES, BerriesWaffles.WaffleType.GLOW_BERRIES));
    // Сундуки
    public static final DeferredBlock<Block> THE_PILLAGERS_CHEST = BLOCKS.register("the_pillagers_chest",
            () -> new PillagerChestBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(1F).noOcclusion()));
    public static final DeferredBlock<Block> GOLDEN_CHEST_KING_PILLAGER = BLOCKS.register("golden_chest_king_pillager",
            () -> new PillagerChestBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(1F).noOcclusion()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ItemsWD.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
