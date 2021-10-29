package it.pureorigins.advancements.extra

import it.pureorigins.advancements.extra.criterion.PlayerKilledEnderDragonCriterion
import it.pureorigins.advancements.extra.criterion.StatCriterion
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.mixin.`object`.builder.CriteriaAccessor.callRegister as registerCriterion
import net.minecraft.util.Identifier

object Advancements : ModInitializer {
    internal fun identifier(path: String) = Identifier("extra", path)
    
    override fun onInitialize() {
        registerExtraCriteria()
    }
    
    private fun registerExtraCriteria() {
        registerCriterion(PlayerKilledEnderDragonCriterion)
        registerCriterion(StatCriterion)
    }
}
