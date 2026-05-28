package ru.imaginaerum.wd.common.init.blocks.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.DragoliteCageBlockEntity;
import ru.imaginaerum.wd.common.init.blocks.custom.entity.ModBlockEntities;
import ru.imaginaerum.wd.common.init.items.ItemsWD;
import ru.imaginaerum.wd.common.init.items.custom.SoulStone;
import ru.imaginaerum.wd.common.init.patricles.ModParticles;

public class DragoliteCage extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty SPARKING = IntegerProperty.create("sparking", 0, 5);
    public static final IntegerProperty SOULS = IntegerProperty.create("souls", 0, 2);
    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(DragoliteCage::new);
    }
    public DragoliteCage(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(SPARKING, 0)
                .setValue(SOULS, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SPARKING, SOULS);
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) return null;
        return createTickerHelper(type, ModBlockEntities.DRAGOLITE_CAGE_ENTITY.get(),
                (lvl, pos, blockState, blockEntity) -> blockEntity.tick(blockState, (ServerLevel) lvl, pos, lvl.random));
    }
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public void stepOn(Level world, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(world, pos, state, entity);
        // MobType удалён в 1.21.1 — используем entity type tag из NeoForge
        if (entity instanceof LivingEntity && entity.getType().is(EntityTypeTags.UNDEAD)) {
            entity.hurt(entity.damageSources().generic(), 8);
        }
    }

    public static void onEntityDeath(LivingEntity entity, DamageSource source) {
        if (source.getMsgId().equals("generic") && entity.getType().is(EntityTypeTags.UNDEAD)) {
            Level world = entity.level();
            if (!world.isClientSide && world instanceof ServerLevel serverWorld) {
                int xp = 7 + world.random.nextInt(4);
                net.minecraft.world.entity.ExperienceOrb.award(serverWorld, entity.position(), xp);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof DragoliteCageBlockEntity cage)) return;

        if (!cage.shouldTick()) return;

        CompoundTag blockTag = blockEntity.getPersistentData();
        String soulFirst = blockTag.getString("soul_first");
        String soulSecond = blockTag.getString("soul_second");

        if (!soulFirst.isEmpty() && soulFirst.equals(soulSecond)) {
            if (state.getValue(SPARKING) > 0) {
                double x = pos.getX() + (random.nextDouble() * 10 - 5);
                double y = pos.getY() + (random.nextInt(11) - 5);
                double z = pos.getZ() + (random.nextDouble() * 10 - 5);
                BlockPos spawnPos = new BlockPos((int) x, (int) y, (int) z);

                EntityType<?> entityType = EntityType.byString(soulFirst).orElse(null);
                if (entityType != null) {
                    Entity entity = entityType.create(level);
                    if (entity != null) {
                        if (entity instanceof WaterAnimal) {
                            if (level.getBlockState(spawnPos).getFluidState().isSource()) {
                                spawnEntityInWater(level, spawnPos, entity, state, pos, random);
                            }
                        } else {
                            if (level.getBlockState(spawnPos.below()).isSolid()) {
                                spawnEntityOnLand(level, spawnPos, entity, state, pos, random);
                            }
                        }
                    }
                }
            }
        }
    }

    private void spawnEntityOnLand(ServerLevel level, BlockPos spawnPos, Entity entity, BlockState state, BlockPos blockPos, RandomSource random) {
        int highestY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnPos.getX(), spawnPos.getZ());
        BlockPos topPos = new BlockPos(spawnPos.getX(), highestY, spawnPos.getZ());
        entity.moveTo(topPos.getX(), topPos.getY(), topPos.getZ(), 0, 0);

        if (level.getBlockState(topPos).isAir() && level.getBlockState(topPos.below()).isSolid()) {
            level.addFreshEntity(entity);
            if (random.nextInt(100) < 45) {
                int currentSparking = state.getValue(SPARKING);
                if (currentSparking > 0) {
                    level.setBlock(blockPos, state.setValue(SPARKING, currentSparking - 1), 3);
                    level.playSound(null, blockPos, SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1, 1);
                }
            }
        }
    }

    private void spawnEntityInWater(ServerLevel level, BlockPos spawnPos, Entity entity, BlockState state, BlockPos blockPos, RandomSource random) {
        entity.moveTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5, 0, 0);
        level.addFreshEntity(entity);
        if (random.nextInt(100) < 45) {
            int currentSparking = state.getValue(SPARKING);
            if (currentSparking > 0) {
                level.setBlock(blockPos, state.setValue(SPARKING, currentSparking - 1), 3);
                level.playSound(null, blockPos, SoundEvents.SAND_BREAK, SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        super.animateTick(state, level, pos, source);
        int souls = state.getValue(SOULS);
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (blockEntity != null) {
            // В 1.21.1 getUpdateTag() принимает HolderLookup.Provider (registryAccess)
            CompoundTag blockTag = blockEntity.getUpdateTag(level.registryAccess());
            String soulFirst = blockTag.getString("soul_first");
            String soulSecond = blockTag.getString("soul_second");

            if (souls == 2 && !soulFirst.isEmpty() && !soulSecond.isEmpty() && soulFirst.equals(soulSecond)) {
                if (source.nextInt(100) == 0) {
                    level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                            SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                double d0 = pos.getX() + 0.5 + (0.5 - source.nextDouble());
                double d1 = pos.getY() + 0.3;
                double d2 = pos.getZ() + 0.5 + (0.5 - source.nextDouble());
                double d3 = source.nextFloat() * 0.04;
                level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0.0, d3, 0.0);
            }
        }

        if (state.getValue(SPARKING) > 0) {
            if (source.nextInt(100) == 0) {
                level.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                        SoundEvents.RESPAWN_ANCHOR_AMBIENT, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            double d0 = pos.getX() + 0.5 + (0.5 - source.nextDouble());
            double d1 = pos.getY() + 1.0;
            double d2 = pos.getZ() + 0.5 + (0.5 - source.nextDouble());
            double d3 = source.nextFloat() * 0.04;
            level.addParticle(ModParticles.ROBIN_STAR_PARTICLES_PROJECTILE.get(), d0, d1, d2, 0.0, d3, 0.0);
        }
    }

    // В 1.21.1 метод use() разбит на useItemOn() и useWithoutItem()
    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level,
                                           BlockPos pos, Player player, InteractionHand hand,
                                           BlockHitResult hitResult) {
        if (stack.is(ItemsWD.SPARKLING_POLLEN.get())) {
            handleSparklingPollen(state, level, pos, player, hand);
            level.sendBlockUpdated(pos, state, state, 3);
            return ItemInteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof SoulStone) {
            handleSoulStone(state, level, pos, player, hand);
            level.sendBlockUpdated(pos, state, state, 3);
            return ItemInteractionResult.SUCCESS;
        }
        if (stack.is(ItemsWD.ROBIN_STICK.get())) {
            handleRobinStick(state, level, pos, player, hand);
            player.swing(hand);
            level.sendBlockUpdated(pos, state, state, 3);
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private void handleRobinStick(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                CompoundTag blockTag = blockEntity.getPersistentData();
                String soulFirst = blockTag.getString("soul_first");
                String soulSecond = blockTag.getString("soul_second");
                int currentSouls = state.getValue(SOULS);

                if (currentSouls == 2) {
                    level.setBlock(pos, state.setValue(SOULS, 1), 3);
                    player.getInventory().add(createSoulStoneStack(level, soulSecond));
                    blockTag.remove("soul_second");
                    if (blockEntity instanceof DragoliteCageBlockEntity cage) {
                        cage.setSoulSecond("");
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);

                } else if (currentSouls == 1) {
                    level.setBlock(pos, state.setValue(SOULS, 0), 3);
                    player.getInventory().add(createSoulStoneStack(level, soulFirst));
                    blockTag.remove("soul_first");
                    if (blockEntity instanceof DragoliteCageBlockEntity cage) {
                        cage.setSoulFirst("");
                    }
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                CompoundTag blockTag = blockEntity.getPersistentData();
                String soulFirst = blockTag.getString("soul_first");
                String soulSecond = blockTag.getString("soul_second");
                int currentSouls = state.getValue(SOULS);

                if (currentSouls == 2) {
                    dropSoulStone(level, pos, soulFirst, blockTag, "soul_first", blockEntity, true);
                    dropSoulStone(level, pos, soulSecond, blockTag, "soul_second", blockEntity, false);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                } else if (currentSouls == 1) {
                    dropSoulStone(level, pos, soulFirst, blockTag, "soul_first", blockEntity, true);
                    level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);
                }
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    private void dropSoulStone(Level level, BlockPos pos, String soul, CompoundTag blockTag,
                               String tagKey, BlockEntity blockEntity, boolean isFirst) {
        if (!soul.isEmpty()) {
            level.addFreshEntity(new ItemEntity(level,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    createSoulStoneStack(level, soul)));
            blockTag.remove(tagKey);
            if (blockEntity instanceof DragoliteCageBlockEntity cage) {
                if (isFirst) cage.setSoulFirst("");
                else cage.setSoulSecond("");
            }
        }
    }

    // Вспомогательный метод — убирает дублирование при создании SoulStone
    private ItemStack createSoulStoneStack(Level level, String entityType) {
        ItemStack stack = new ItemStack(ItemsWD.SOUL_STONE.get());
        CompoundTag nbt = new CompoundTag();
        nbt.putString("entity_type", entityType);
        nbt.putString("entity_name", getEntityName(level, entityType));
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(nbt));
        chargeSoulStone(stack);
        return stack;
    }

    // getOrCreateTag() / putInt("CustomModelData") → DataComponents
    public static void chargeSoulStone(ItemStack stack) {
        stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, existing -> {
            CompoundTag tag = existing.copyTag();
            tag.putBoolean("Charged", true);
            return CustomData.of(tag);
        });
        stack.set(DataComponents.CUSTOM_MODEL_DATA, new CustomModelData(1));
    }

    private String getEntityName(Level level, String entityType) {
        EntityType<?> type = EntityType.byString(entityType).orElse(null);
        if (type != null) {
            Entity entity = type.create(level);
            if (entity != null) return entity.getName().getString();
        }
        return "Unknown Entity";
    }

    private void handleSparklingPollen(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        if (!level.isClientSide) {
            int currentSparking = state.getValue(SPARKING);
            if (currentSparking < 5) {
                level.setBlock(pos, state.setValue(SPARKING, currentSparking + 1), 3);
                if (!player.isCreative()) player.getItemInHand(hand).shrink(1);
                level.playSound(null, pos, SoundEvents.SAND_PLACE, SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    private void handleSoulStone(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand) {
        SoulStone soulStone = (SoulStone) player.getItemInHand(hand).getItem();
        if (soulStone.isCharged(player.getItemInHand(hand)) && !level.isClientSide) {
            int currentSoul = state.getValue(SOULS);
            if (currentSoul < 2) {
                // Читаем entity_type из DataComponents.CUSTOM_DATA
                String entityType = "";
                CustomData customData = player.getItemInHand(hand).get(DataComponents.CUSTOM_DATA);
                if (customData != null) {
                    entityType = customData.copyTag().getString("entity_type");
                }

                level.setBlock(pos, state.setValue(SOULS, currentSoul + 1), 3);
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) handleSoulStorage(blockEntity, currentSoul, entityType);

                if (!player.isCreative()) player.getItemInHand(hand).shrink(1);
                level.playSound(null, pos, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    private void handleSoulStorage(BlockEntity blockEntity, int currentSoul, String entityType) {
        CompoundTag blockTag = blockEntity.getPersistentData();
        if (currentSoul == 0) {
            blockTag.putString("soul_first", entityType);
            if (blockEntity instanceof DragoliteCageBlockEntity cage) cage.setSoulFirst(entityType);
        } else if (currentSoul == 1) {
            blockTag.putString("soul_second", entityType);
            if (blockEntity instanceof DragoliteCageBlockEntity cage) cage.setSoulSecond(entityType);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(SPARKING, 0)
                .setValue(SOULS, 0);
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new DragoliteCageBlockEntity(pPos, pState);
    }
}