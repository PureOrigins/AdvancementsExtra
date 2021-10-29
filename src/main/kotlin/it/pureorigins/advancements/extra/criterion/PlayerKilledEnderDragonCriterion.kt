package it.pureorigins.advancements.extra.criterion

import com.google.gson.JsonObject
import it.pureorigins.advancements.extra.Advancements.identifier
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.loot.context.LootContext
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.util.Identifier
import java.util.*
import kotlin.collections.HashSet

object PlayerKilledEnderDragonCriterion : AbstractCriterion<PlayerKilledEnderDragonCriterion.Conditions>() {
    private val damages = HashMap<EnderDragonEntity, MutableSet<UUID>>()
    
    override fun getId() = identifier("player_killed_ender_dragon")
    
    override fun conditionsFromJson(
        json: JsonObject,
        predicate: EntityPredicate.Extended,
        deserializer: AdvancementEntityPredicateDeserializer
    ): Conditions {
        return Conditions(id, predicate, EntityPredicate.Extended.getInJson(json, "entity", deserializer))
    }
    
    fun registerDamage(entity: EnderDragonEntity, player: PlayerEntity) {
        damages.computeIfAbsent(entity) { HashSet() }.add(player.uuid)
    }
    
    fun trigger(entity: EnderDragonEntity) {
        damages.remove(entity)?.forEach { uuid ->
            val player = entity.server?.playerManager?.getPlayer(uuid) ?: return@forEach
            val lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity)
            trigger(player) { it.test(lootContext) }
        }
    }
    
    class Conditions(id: Identifier, predicate: EntityPredicate.Extended, val entity: EntityPredicate.Extended) : AbstractCriterionConditions(id, predicate) {
        fun test(killedEntityContext: LootContext): Boolean {
            return entity.test(killedEntityContext)
        }
        
        override fun toJson(serializer: AdvancementEntityPredicateSerializer): JsonObject {
            val jsonObject = super.toJson(serializer)
            jsonObject.add("entity", entity.toJson(serializer))
            return jsonObject
        }
    }
}