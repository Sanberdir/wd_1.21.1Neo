package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.BlocksWD;
import ru.imaginaerum.wd.common.init.blocks.custom.MagicSoilFarmland;

import java.util.List;

public class IronWateringCan extends Item {

    private static final int MAX_CAPACITY = 1000;
    private static final int WATER_USAGE = 50;
    private static final String WATER_KEY = "water_amount";
    public IronWateringCan(Properties properties) {
        super(properties);
    }
    public static int getWaterAmount(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.copyTag().getInt(WATER_KEY);
    }

    public static void setWaterAmount(ItemStack stack, int amount) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(WATER_KEY, Math.min(amount, MAX_CAPACITY));

        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    public static boolean isFull(ItemStack stack) {
        return getWaterAmount(stack) >= MAX_CAPACITY;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal(getWaterAmount(stack) + " / " + MAX_CAPACITY + " mb"));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

        if (hit.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        }

        BlockPos pos = hit.getBlockPos();

        if (!level.mayInteract(player, pos)) {
            return InteractionResultHolder.pass(stack);
        }

        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            if (getWaterAmount(stack) < MAX_CAPACITY) {

                setWaterAmount(stack, MAX_CAPACITY);

                level.playSound(null, player.blockPosition(),
                        SoundEvents.BOTTLE_FILL,
                        SoundSource.NEUTRAL, 1.0F, 1.0F);

                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);

                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (player == null) return InteractionResult.PASS;

        if (!level.getBlockState(pos).is(BlocksWD.MAGIC_SOIL_FARMLAND.get())) {
            return InteractionResult.PASS;
        }

        if (getWaterAmount(stack) < WATER_USAGE) {
            return InteractionResult.PASS;
        }

        BlockState state = level.getBlockState(pos);

        if (!state.hasProperty(MagicSoilFarmland.MOIST) || state.getValue(MagicSoilFarmland.MOIST)) {
            return InteractionResult.PASS;
        }

        level.setBlock(pos, state.setValue(MagicSoilFarmland.MOIST, true), 3);

        setWaterAmount(stack, getWaterAmount(stack) - WATER_USAGE);

        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);

        if (!level.isClientSide) {
            for (int i = 0; i < 10; i++) {
                level.addParticle(
                        ParticleTypes.SPLASH,
                        pos.getX() + 0.5 + (level.random.nextDouble() - 0.5),
                        pos.getY() + 1.0,
                        pos.getZ() + 0.5 + (level.random.nextDouble() - 0.5),
                        0.0, 0.1, 0.0
                );
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        int water = getWaterAmount(stack);
        return water > 0 && water < MAX_CAPACITY;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round((float) getWaterAmount(stack) / MAX_CAPACITY * 13);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 0x3366CC;
    }
}