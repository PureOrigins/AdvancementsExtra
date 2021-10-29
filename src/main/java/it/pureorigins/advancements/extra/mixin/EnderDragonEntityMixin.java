package it.pureorigins.advancements.extra.mixin;

import it.pureorigins.advancements.extra.criterion.PlayerKilledEnderDragonCriterion;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragonEntity.class)
public class EnderDragonEntityMixin {
  @Inject(method = "parentDamage", at = @At("RETURN"))
  private void parentDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callback) {
    if (callback.getReturnValue() && source.getAttacker() instanceof PlayerEntity attacker) {
      PlayerKilledEnderDragonCriterion.INSTANCE.registerDamage((EnderDragonEntity) (Object) this, attacker);
    }
  }
}
