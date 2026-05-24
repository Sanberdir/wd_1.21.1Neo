package ru.imaginaerum.wd.common.init.blocks;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.*;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.blocks.custom.MagicSoilFarmland;

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
    public static final DeferredBlock<Block> MEADOW_GOLDEN_FLOWER = BLOCKS.register("meadow_golden_flower",
            () -> new GoldenRose(BlockBehaviour.Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.GRASS)));

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

    // Особая еда
    public static final DeferredBlock<Block> WIZARD_PIE = BLOCKS.register("wizard_pie",
            () -> new WizardPie(BlockBehaviour.Properties.of().strength(0.5F).sound(SoundType.WOOL).randomTicks()));
    public static final DeferredBlock<Block> ROTTEN_PIE = BLOCKS.register("rotten_pie",
            () -> new RottenPie(BlockBehaviour.Properties.of().randomTicks().strength(0.5F).sound(SoundType.WOOL)));
    // Торты со свечами
    public static final DeferredBlock<Block> CANDLE_WIZARD_PIE = BLOCKS.register("candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> WHITE_CANDLE_WIZARD_PIE = BLOCKS.register("white_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.WHITE_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> BLACK_CANDLE_WIZARD_PIE = BLOCKS.register("black_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.BLACK_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> YELLOW_CANDLE_WIZARD_PIE = BLOCKS.register("yellow_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.YELLOW_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> RED_CANDLE_WIZARD_PIE = BLOCKS.register("red_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.RED_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> ORANGE_CANDLE_WIZARD_PIE = BLOCKS.register("orange_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.ORANGE_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> PINK_CANDLE_WIZARD_PIE = BLOCKS.register("pink_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.PINK_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> PURPLE_CANDLE_WIZARD_PIE = BLOCKS.register("purple_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.PURPLE_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> MAGENTA_CANDLE_WIZARD_PIE = BLOCKS.register("magenta_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.MAGENTA_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> GRAY_CANDLE_WIZARD_PIE = BLOCKS.register("gray_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.GRAY_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> CYAN_CANDLE_WIZARD_PIE = BLOCKS.register("cyan_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.CYAN_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> BLUE_CANDLE_WIZARD_PIE = BLOCKS.register("blue_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.BLUE_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> BROWN_CANDLE_WIZARD_PIE = BLOCKS.register("brown_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.BROWN_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> GREEN_CANDLE_WIZARD_PIE = BLOCKS.register("green_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.GREEN_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> LIME_CANDLE_WIZARD_PIE = BLOCKS.register("lime_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.LIME_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> LIGHT_BLUE_CANDLE_WIZARD_PIE = BLOCKS.register("light_blue_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.LIGHT_BLUE_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));
    public static final DeferredBlock<Block> LIGHT_GRAY_CANDLE_WIZARD_PIE = BLOCKS.register("light_gray_candle_wizard_pie",
            () -> new CandleWizardPie(Blocks.LIGHT_GRAY_CANDLE,BlockBehaviour.Properties.ofFullCopy(Blocks.CANDLE_CAKE)));

    // Сундуки
    public static final DeferredBlock<Block> THE_PILLAGERS_CHEST = BLOCKS.register("the_pillagers_chest",
            () -> new PillagerChestBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(1F).noOcclusion()));
    public static final DeferredBlock<Block> GOLDEN_CHEST_KING_PILLAGER = BLOCKS.register("golden_chest_king_pillager",
            () -> new PillagerChestBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOD).strength(1F).noOcclusion()));

    public static final DeferredBlock<Block> SUGAR_SACK = BLOCKS.register("sugar_sack",
            () -> new FacingBlock(BlockBehaviour.Properties.of().sound(SoundType.WOOL).noOcclusion()));
    // Драколит
    public static final DeferredBlock<Block> DRAGOLIT_GRID = BLOCKS.register("dragolit_grid",
            () -> new DragolitGrid(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(2.5F, 12000F)
                    .noOcclusion().requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> DRAGOLIT_BLOCK = BLOCKS.register("dragolit_block",
            () -> new DragolitBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(5F,12000F)
                    .noOcclusion().requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> STRANGE_CHIP = BLOCKS.register("strange_chip",
            () -> new DragolitBlock(BlockBehaviour.Properties.of().sound(SoundType.ANCIENT_DEBRIS).strength(30F, 1200F)
                    .requiresCorrectToolForDrops().noOcclusion()));

    // Другие блоки
    public static final DeferredBlock<Block> A_BLOCK_OF_SPARKLING_POLLEN = BLOCKS.register("a_block_of_sparkling_pollen",
            () -> new WDFallingBlock(BlockBehaviour.Properties.of().strength(0.2F, 120000F)
                    .sound(SoundType.SAND)));
    // Горшки для шашлыка
    public static final DeferredBlock<Block> POT = BLOCKS.register("pot",
            () -> new Pot(BlockBehaviour.Properties.of().sound(SoundType.DECORATED_POT).strength(0.7F).noOcclusion()));
    public static final DeferredBlock<Block> POT_FROM_MEAT_GOAT = BLOCKS.register("pot_from_meat_goat",
            () -> new PotWithKebab(BlockBehaviour.Properties.of().sound(SoundType.DECORATED_POT).strength(0.7F).noOcclusion()));
    public static final DeferredBlock<Block> POT_FROM_MEAT_CAMEL = BLOCKS.register("pot_from_meat_camel",
            () -> new PotWithKebab(BlockBehaviour.Properties.of().sound(SoundType.DECORATED_POT).strength(0.7F).noOcclusion()));
    public static final DeferredBlock<Block> MARINADED_POT_FROM_MEAT_GOAT = BLOCKS.register("marinaded_pot_from_meat_goat",
            () -> new MarinadedPot(BlockBehaviour.Properties.of().sound(SoundType.DECORATED_POT).strength(0.7F).noOcclusion()));
    public static final DeferredBlock<Block> MARINADED_POT_FROM_MEAT_CAMEL = BLOCKS.register("marinaded_pot_from_meat_camel",
            () -> new MarinadedPot(BlockBehaviour.Properties.of().sound(SoundType.DECORATED_POT).strength(0.7F).noOcclusion()));
    // Волшебная почва и растения
    public static final DeferredBlock<Block> MAGIC_SOIL = BLOCKS.register("magic_soil",
            () -> new MagicSoil(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).instrument(NoteBlockInstrument.BASS).strength(0.5F)
                    .sound(SoundType.CROP)));
    public static final DeferredBlock<Block> MAGIC_SOIL_FARMLAND = BLOCKS.register("magic_soil_farmland",
            () -> new MagicSoilFarmland(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final DeferredBlock<Block> MAGIC_SOIL_GRASS = BLOCKS.register("magic_soil_grass",
            () -> new MagicSoilGrass(BlockBehaviour.Properties.ofFullCopy(Blocks.FARMLAND)));
    public static final DeferredBlock<Block> BRIGHT_PEPPER_SEEDS = BLOCKS.register("bright_pepper_seeds",
            () -> new BrightPepperSeeds(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).instrument(NoteBlockInstrument.BASS).instabreak()
                    .sound(SoundType.CROP).randomTicks().noCollission().noOcclusion()));


    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ItemsWD.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
