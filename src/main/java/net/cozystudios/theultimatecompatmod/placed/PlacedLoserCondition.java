package net.cozystudios.theultimatecompatmod.placed;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.JsonSerializer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class PlacedLoserCondition implements LootCondition {

    public static LootConditionType TYPE;

    @Override
    public LootConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext context) {
        Vec3d origin = context.get(LootContextParameters.ORIGIN);
        if (origin == null) return false;
        ServerWorld world = context.getWorld();
        if (world == null) return false;
        BlockPos pos = BlockPos.ofFloored(origin);
        return PlacedLoserTracker.isMarked(world, pos);
    }

    public static final class Serializer implements JsonSerializer<PlacedLoserCondition> {
        @Override
        public void toJson(JsonObject json, PlacedLoserCondition value, JsonSerializationContext ctx) {
        }

        @Override
        public PlacedLoserCondition fromJson(JsonObject json, JsonDeserializationContext ctx) {
            return new PlacedLoserCondition();
        }
    }
}
