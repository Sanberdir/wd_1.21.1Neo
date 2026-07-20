package ru.imaginaerum.wd.common.init.items.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ModElytra extends ElytraItem implements Equipable {

    protected final ArmorItem.Type type;
    // material теперь Holder<ArmorMaterial>, как в ArmorItem
    protected final Holder<ArmorMaterial> material;

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOR = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(BlockSource source, ItemStack stack) {
            return ArmorItem.dispenseArmor(source, stack) ? stack : super.execute(source, stack);
        }
    };

    public ModElytra(Holder<ArmorMaterial> material, ArmorItem.Type type, Properties properties) {
        super(properties); // просто передаём properties без перезаписи durability
        this.material = material;
        this.type = type;
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOR);
    }
    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility ability) {
        // Разрешаем все действия экипировки для слота груди
        return true;
    }
    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers() {
        return buildModifiers(this.material, this.type);
    }

    // Выделено в статический метод для удобства
    public static ItemAttributeModifiers buildModifiers(Holder<ArmorMaterial> material, ArmorItem.Type type) {
        ArmorMaterial mat = material.value();
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();
        EquipmentSlotGroup slotGroup = EquipmentSlotGroup.bySlot(type.getSlot());

        int defense = mat.defense().getOrDefault(type, 0);
        float toughness = mat.toughness();
        float kbResistance = mat.knockbackResistance();

        ResourceLocation baseId = ResourceLocation.withDefaultNamespace("armor." + type.getName());

        builder.add(
                Attributes.ARMOR,
                new AttributeModifier(baseId, (double) defense, AttributeModifier.Operation.ADD_VALUE),
                slotGroup
        );
        builder.add(
                Attributes.ARMOR_TOUGHNESS,
                new AttributeModifier(
                        ResourceLocation.withDefaultNamespace("armor." + type.getName() + ".toughness"),
                        (double) toughness,
                        AttributeModifier.Operation.ADD_VALUE
                ),
                slotGroup
        );
        if (kbResistance > 0.0F) {
            builder.add(
                    Attributes.KNOCKBACK_RESISTANCE,
                    new AttributeModifier(
                            ResourceLocation.withDefaultNamespace("armor." + type.getName() + ".knockback_resistance"),
                            (double) kbResistance,
                            AttributeModifier.Operation.ADD_VALUE
                    ),
                    slotGroup
            );
        }

        return builder.build();
    }

    @Override
    public int getEnchantmentValue() {
        // ArmorMaterial record — поле enchantmentValue()
        return this.material.value().enchantmentValue();
    }

    public Holder<ArmorMaterial> getMaterial() {
        return this.material;
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairItem) {
        // repairIngredient() — record-компонент, возвращает Supplier<Ingredient>
        return this.material.value().repairIngredient().get().test(repairItem)
                || super.isValidRepairItem(stack, repairItem);
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        EquipmentSlot slot = EquipmentSlot.CHEST;
        ItemStack equipped = player.getItemBySlot(slot);

        if (equipped.isEmpty()) {
            // Если слот пуст - просто надеваем
            player.setItemSlot(slot, itemstack.copy());
            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            itemstack.setCount(0);
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        } else {
            // Если в слоте что-то есть - меняем местами
            player.setItemSlot(slot, itemstack.copy());
            player.setItemInHand(hand, equipped);
            if (!level.isClientSide()) {
                player.awardStat(Stats.ITEM_USED.get(this));
            }
            return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
        }
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return isUseable(stack);
    }

    public static boolean isUseable(ItemStack stack) {
        return stack.getDamageValue() < stack.getMaxDamage() - 1;
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        if (!entity.level().isClientSide && (flightTicks + 1) % 25 == 0) {
            stack.hurtAndBreak(1, entity, EquipmentSlot.CHEST);
        }
        return true;
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        // equipSound() — record-компонент, возвращает Holder<SoundEvent>
        return this.material.value().equipSound();
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }
}