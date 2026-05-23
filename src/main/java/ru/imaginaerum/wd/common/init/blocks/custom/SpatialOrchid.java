package ru.imaginaerum.wd.common.init.blocks.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;


public class SpatialOrchid extends Block {

    private static final int TELEPORT_RADIUS = 300;
    private static final String TAG_KEY = "wd_spatial_orchid_pending";

    public SpatialOrchid(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide) {
            entity.getPersistentData().putBoolean(TAG_KEY, true);
            level.scheduleTick(pos, this, 1);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Ищем тегированных в широком радиусе — они могли убежать
        AABB searchArea = new AABB(pos).inflate(16.0);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, searchArea);

        for (Entity entity : entities) {
            if (entity.getPersistentData().getBoolean(TAG_KEY)) {
                entity.getPersistentData().remove(TAG_KEY);
                teleportEntityRandomly(level, entity);
            }
        }

        if (!state.canSurvive(level, pos)) {
            level.destroyBlock(pos, true);
        }
    }

    private void teleportEntityRandomly(ServerLevel level, Entity entity) {
        RandomSource random = level.getRandom();
        int x = entity.blockPosition().getX() + random.nextInt(TELEPORT_RADIUS * 2) - TELEPORT_RADIUS;
        int z = entity.blockPosition().getZ() + random.nextInt(TELEPORT_RADIUS * 2) - TELEPORT_RADIUS;
        BlockPos candidatePos = new BlockPos(x, level.getHeight() - 1, z);

        while (candidatePos.getY() > level.getMinBuildHeight()) {
            if (isValidTeleportLocation(level, candidatePos)) {
                entity.teleportTo(
                        candidatePos.getX() + 0.5,
                        candidatePos.getY() + 1,
                        candidatePos.getZ() + 0.5
                );
                return;
            }
            candidatePos = candidatePos.below();
        }
    }

    private boolean isValidTeleportLocation(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolid() &&
                level.getBlockState(pos.above()).isAir() &&
                level.getBlockState(pos.above(2)).isAir();
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        VoxelShape voxelshape = this.getShape(state, level, pos, CollisionContext.empty());
        Vec3 vec3 = voxelshape.bounds().getCenter();
        double d0 = pos.getX() + vec3.x;
        double d1 = pos.getZ() + vec3.z;
        for (int i = 0; i < 3; ++i) {
            if (source.nextBoolean()) {
                level.addParticle(ParticleTypes.PORTAL,
                        d0 + source.nextDouble() / 5.0,
                        pos.getY() + (0.5 - source.nextDouble()),
                        d1 + source.nextDouble() / 5.0,
                        0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState currentBlockState, Direction direction,
                                  BlockState neighborBlockState, LevelAccessor world,
                                  BlockPos currentPos, BlockPos neighborPos) {
        if (!currentBlockState.canSurvive(world, currentPos)) {
            world.scheduleTick(currentPos, this, 1);
        }
        return super.updateShape(currentBlockState, direction, neighborBlockState, world, currentPos, neighborPos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos blockPos) {
        BlockState below = levelReader.getBlockState(blockPos.below());
        return below.is(Blocks.END_STONE) || below.is(Blocks.END_STONE_BRICKS);
    }
}
