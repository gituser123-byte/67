package com.advancementmultiplier;

import com.advancementmultiplier.mixin.PlayerAdvancementTrackerAccessor;
import java.util.ArrayList;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class EnchantLogic {
    public static final int CAP = 32768;

    private EnchantLogic() {}

    public static int completedAdvancements(ServerPlayerEntity player) {
        PlayerAdvancementTrackerAccessor access =
            (PlayerAdvancementTrackerAccessor) player.getAdvancementTracker();
        return (int) access.advancementMultiplier$getProgress().values().stream()
            .filter(AdvancementProgress::isDone)
            .count();
    }

    public static int currentCraftLevel(ServerPlayerEntity player) {
        int count = Math.min(completedAdvancements(player), 15);
        return Math.min(1 << count, CAP);
    }

    public static void enchantCraftedItem(ServerPlayerEntity player, ItemStack stack) {
        int level = currentCraftLevel(player);
        Item item = stack.getItem();

        if (item instanceof PickaxeItem) {
            add(player, stack, Enchantments.EFFICIENCY, level);
            add(player, stack, Enchantments.UNBREAKING, level);
            add(player, stack, Enchantments.FORTUNE, level);
        } else if (item instanceof AxeItem) {
            add(player, stack, Enchantments.EFFICIENCY, level);
            add(player, stack, Enchantments.SHARPNESS, level);
            add(player, stack, Enchantments.UNBREAKING, level);
        } else if (item instanceof ShovelItem || item instanceof HoeItem) {
            add(player, stack, Enchantments.EFFICIENCY, level);
            add(player, stack, Enchantments.UNBREAKING, level);
        } else if (item instanceof SwordItem) {
            add(player, stack, Enchantments.SHARPNESS, level);
            add(player, stack, Enchantments.LOOTING, level);
            add(player, stack, Enchantments.UNBREAKING, level);
        } else if (item instanceof BowItem) {
            add(player, stack, Enchantments.POWER, level);
            add(player, stack, Enchantments.UNBREAKING, level);
        } else if (item instanceof CrossbowItem) {
            add(player, stack, Enchantments.QUICK_CHARGE, level);
            add(player, stack, Enchantments.PIERCING, level);
            add(player, stack, Enchantments.UNBREAKING, level);
        } else if (item instanceof TridentItem) {
            add(player, stack, Enchantments.IMPALING, level);
            add(player, stack, Enchantments.LOYALTY, level);
            add(player, stack, Enchantments.UNBREAKING, level);
        } else if (item instanceof ArmorItem armor) {
            add(player, stack, Enchantments.PROTECTION, level);
            add(player, stack, Enchantments.UNBREAKING, level);
            if (armor.getSlotType() == net.minecraft.entity.EquipmentSlot.HEAD) {
                add(player, stack, Enchantments.RESPIRATION, level);
                add(player, stack, Enchantments.AQUA_AFFINITY, level);
            }
            if (armor.getSlotType() == net.minecraft.entity.EquipmentSlot.FEET) {
                add(player, stack, Enchantments.FEATHER_FALLING, level);
                add(player, stack, Enchantments.DEPTH_STRIDER, level);
            }
        } else if (item instanceof ShieldItem || item instanceof FishingRodItem ||
                   item instanceof ShearsItem || item instanceof FlintAndSteelItem) {
            add(player, stack, Enchantments.UNBREAKING, level);
        }
    }

    public static void doubleAll(ServerPlayerEntity player) {
        for (int i = 0; i < player.getInventory().size(); i++) {
            doubleStack(player.getInventory().getStack(i));
        }
        player.sendMessage(Text.literal("Advancement earned — every enchantment doubled!"), true);
    }

    public static void doubleStack(ItemStack stack) {
        if (stack.isEmpty()) return;

        ItemEnchantmentsComponent current =
            stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        if (current.isEmpty()) return;

        ItemEnchantmentsComponent.Builder builder =
            new ItemEnchantmentsComponent.Builder(current);

        for (RegistryEntry<Enchantment> enchantment :
                new ArrayList<>(builder.getEnchantments())) {
            int oldLevel = builder.getLevel(enchantment);
            builder.set(enchantment, Math.min(CAP, Math.max(1, oldLevel * 2)));
        }

        stack.set(DataComponentTypes.ENCHANTMENTS, builder.build());
    }

    private static void add(ServerPlayerEntity player, ItemStack stack,
                            net.minecraft.registry.RegistryKey<Enchantment> key, int level) {
        RegistryEntry<Enchantment> enchantment = player.getRegistryManager()
            .getOrThrow(RegistryKeys.ENCHANTMENT)
            .getOrThrow(key);
        stack.addEnchantment(enchantment, level);
    }
}
