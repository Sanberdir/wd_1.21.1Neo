package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.WD;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.RoseMurdererBlockEntity;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;

public class RoseMurderer extends BaseEntityBlock {

    public static final MapCodec<RoseMurderer> CODEC = simpleCodec(RoseMurderer::new);
    public static final BooleanProperty IS_SOUL = BooleanProperty.create("is_soul");

    public RoseMurderer(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_SOUL, false));
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @SubscribeEvent
    public void onEntityDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();

        if (!(entity instanceof Player)) {
            BlockPos pos = entity.blockPosition();
            Level level = entity.level();
            BlockState blockState = level.getBlockState(pos);

            if (blockState.getBlock() instanceof RoseMurderer) {
                event.getDrops().clear();
                event.setCanceled(true);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(IS_SOUL);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(IS_SOUL, false);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && !world.getBlockState(pos.below()).is(Blocks.SOUL_SOIL)) {
            world.destroyBlock(pos, true);
        }
        super.onRemove(state, world, pos, newState, isMoving);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }

        if (!state.getValue(IS_SOUL)) {
            return;
        }

        level.setBlock(pos, state.setValue(IS_SOUL, false), 3);

        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof RoseMurdererBlockEntity roseBE) {
            roseBE.clearEntityInfo();
            level.sendBlockUpdated(pos, state, state, 3);
        }
    }

    @Override
    protected BlockState updateShape(BlockState currentBlockState, Direction direction, BlockState neighborBlockState,
                                     LevelAccessor world, BlockPos currentPos, BlockPos neighborPos) {
        if (!currentBlockState.canSurvive(world, currentPos)) {
            world.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(currentBlockState, direction, neighborBlockState, world, currentPos, neighborPos);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        return levelReader.getBlockState(pos.below()).is(Blocks.SOUL_SOIL);
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return false;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        VoxelShape voxelShape = this.getShape(state, level, pos, CollisionContext.empty());
        Vec3 center = voxelShape.bounds().getCenter();

        double x = pos.getX() + center.x;
        double z = pos.getZ() + center.z;

        for (int i = 0; i < 3; ++i) {
            if (random.nextBoolean() && !state.getValue(IS_SOUL)) {
                level.addParticle(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        x + random.nextDouble() / 5.0D,
                        pos.getY() + (0.5D - random.nextDouble()),
                        z + random.nextDouble() / 5.0D,
                        0.0D, 0.0D, 0.0D
                );
            } else if (random.nextBoolean() && state.getValue(IS_SOUL)) {
                level.addParticle(
                        ModParticles.STOMBLE_ROSE.get(),
                        x + random.nextDouble() / 5.0D,
                        pos.getY() + (0.5D - random.nextDouble()),
                        z + random.nextDouble() / 5.0D,
                        0.0D, 0.0D, 0.0D
                );
            }
        }
    }

    private void saveEntityInfoToBlockEntity(LevelAccessor world, BlockPos pos, Entity entity) {

        if (!world.isClientSide()) {

            BlockEntity blockEntity = world.getBlockEntity(pos);

            if (blockEntity instanceof RoseMurdererBlockEntity roseBE) {

                roseBE.setEntityInfo(
                        String.valueOf(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType())),
                        entity.getDisplayName().getString()
                );

                roseBE.setChanged();

                if (world instanceof Level level) {
                    level.sendBlockUpdated(
                            pos,
                            roseBE.getBlockState(),
                            roseBE.getBlockState(),
                            3
                    );
                }
            }
        }
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof LivingEntity livingEntity && !state.getValue(IS_SOUL)) {
            handleEntityDeath(level, pos, state, livingEntity);
        }
    }

    private void handleEntityDeath(Level level, BlockPos pos, BlockState state, LivingEntity entity) {
        entity.hurt(entity.damageSources().magic(), 2);

        if (entity.isDeadOrDying()
                && !(entity instanceof IronGolem)) {
            BlockState newState = state.setValue(IS_SOUL, true);
            level.setBlock(pos, newState, 3);

            saveEntityInfoToBlockEntity(level, pos, entity);
            level.scheduleTick(pos, this, 120);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RoseMurdererBlockEntity(pos, state);
    }
}