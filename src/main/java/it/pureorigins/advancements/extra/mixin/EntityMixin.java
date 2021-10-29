package it.pureorigins.advancements.extra.mixin;

import it.pureorigins.advancements.extra.criterion.PlayerKilledEnderDragonCriterion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
  @Inject(method = "setRemoved", at = @At("HEAD"))
  private void setRemoved(Entity.RemovalReason reason, CallbackInfo callback) {
    if ((Entity) (Object) this instanceof EnderDragonEntity enderDragon) {
      PlayerKilledEnderDragonCriterion.INSTANCE.trigger(enderDragon);
    }
  }
}
