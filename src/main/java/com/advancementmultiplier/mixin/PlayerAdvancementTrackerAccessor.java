package com.advancementmultiplier.mixin;

import java.util.Map;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementProgress;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerAdvancementTracker.class)
public interface PlayerAdvancementTrackerAccessor {
    @Accessor("progress")
    Map<AdvancementEntry, AdvancementProgress> advancementMultiplier$getProgress();

    @Accessor("owner")
    ServerPlayerEntity advancementMultiplier$getOwner();
}
