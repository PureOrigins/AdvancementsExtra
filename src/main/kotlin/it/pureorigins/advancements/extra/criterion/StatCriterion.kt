package it.pureorigins.advancements.extra.criterion

import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException
import it.pureorigins.advancements.extra.Advancements.identifier
import net.minecraft.advancement.criterion.AbstractCriterion
import net.minecraft.advancement.criterion.AbstractCriterionConditions
import net.minecraft.predicate.NumberRange
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer
import net.minecraft.predicate.entity.EntityPredicate
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.stat.Stat
import net.minecraft.stat.Stats
import net.minecraft.util.Identifier
import net.minecraft.util.JsonHelper

object StatCriterion : AbstractCriterion<StatCriterion.Conditions>() {
    override fun getId() = identifier("stat")
    
    override fun conditionsFromJson(
        json: JsonObject,
        predicate: EntityPredicate.Extended,
        deserializer: AdvancementEntityPredicateDeserializer
    ): Conditions {
        val statId = Identifier(JsonHelper.asString(json, "stat"))
        val stat = if (Stats.CUSTOM.hasStat(statId)) Stats.CUSTOM.getOrCreateStat(statId) else throw JsonSyntaxException("Unknown stat '$statId'")
        return Conditions(id, predicate, stat, NumberRange.IntRange.fromJson(json.get("value")))
    }

    fun trigger(player: ServerPlayerEntity, stat: Stat<*>, value: Int) {
        trigger(player) { it.test(stat, value) }
    }
    
    class Conditions(id: Identifier, predicate: EntityPredicate.Extended, val stat: Stat<*>, val value: NumberRange.IntRange) : AbstractCriterionConditions(id, predicate) {
        fun test(stat: Stat<*>, value: Int): Boolean {
            return this.stat == stat && this.value.test(value)
        }
        
        override fun toJson(serializer: AdvancementEntityPredicateSerializer): JsonObject {
            val jsonObject = super.toJson(serializer)
            jsonObject.add("stat", JsonPrimitive(stat.toString()))
            jsonObject.add("value", value.toJson())
            return jsonObject
        }
    }
}