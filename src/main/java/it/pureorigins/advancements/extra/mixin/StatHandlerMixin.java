package it.pureorigins.advancements.extra.mixin;

import it.pureorigins.advancements.extra.criterion.StatCriterion;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(StatHandler.class)
public class StatHandlerMixin {
  @Inject(method = "setStat", at = @At("TAIL"))
  public void setStat(PlayerEntity player, Stat<?> stat, int value, CallbackInfo callback) {
    if (player instanceof ServerPlayerEntity) {
      StatCriterion.INSTANCE.trigger((ServerPlayerEntity) player, stat, value);
    }
  }
}
