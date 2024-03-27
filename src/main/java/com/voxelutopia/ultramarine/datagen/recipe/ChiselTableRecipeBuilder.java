package com.voxelutopia.ultramarine.datagen.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.voxelutopia.ultramarine.data.recipe.ChiselTableRecipe;
import com.voxelutopia.ultramarine.data.registry.RecipeSerializerRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class ChiselTableRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final Item result;
    private final Ingredient material;
    private final Ingredient template;
    protected final List<Ingredient> colors;
    private final Advancement.Builder advancementBuilder = Advancement.Builder.advancement();
    @Nullable private String group;
    private static final RecipeSerializer<ChiselTableRecipe> SERIALIZER = RecipeSerializerRegistry.CHISEL_TABLE_SERIALIZER.get();

    public ChiselTableRecipeBuilder(RecipeCategory category, ItemLike result, Ingredient material, Ingredient template, List<Ingredient> colors){
        this.category = category;
        this.result = result.asItem();
        this.material = material;
        this.template = template;
        this.colors = colors;
    }

    public static ChiselTableRecipeBuilder chiselTableRecipe(RecipeCategory category, Ingredient material, Ingredient template, Ingredient[] colors, ItemLike result){
        List<Ingredient> colorList = Arrays.stream(colors).filter(i -> !i.isEmpty()).toList();
        return new ChiselTableRecipeBuilder(category, result, material, template, colorList);
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pCriterionName, @NotNull CriterionTriggerInstance pCriterionTrigger) {
        this.advancementBuilder.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) {
        this.group = pGroupName;
        return this;
    }

    @Override
    public @NotNull Item getResult() {
        return this.result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        this.advancementBuilder.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId)).rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.group == null ? "" : this.group,
                this.material, this.template, this.colors, this.result, this.advancementBuilder,
                pRecipeId.withPrefix("recipes/" + this.category.getFolderName() + "/")));
    }

    private static class Result implements FinishedRecipe{
        private final ResourceLocation id;
        private final String group;
        private final Item result;
        private final Ingredient material;
        private final Ingredient template;
        protected final List<Ingredient> colors;
        private final Advancement.Builder advancementBuilder;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation pId, String pGroup, Ingredient material, Ingredient template, List<Ingredient> colors, Item result,
                      Advancement.Builder pAdvancement, ResourceLocation pAdvancementId){
            this.id = pId;
            this.group = pGroup;
            this.result = result;
            this.material = material;
            this.template = template;
            this.colors = colors;
            this.advancementBuilder = pAdvancement;
            this.advancementId = pAdvancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            if (!this.group.isEmpty()) {
                pJson.addProperty("group", this.group);
            }
            pJson.add("material", this.material.toJson());
            pJson.add("template", this.template.toJson());
            JsonArray colorsJson = new JsonArray();
            for (Ingredient color: this.colors){
                colorsJson.add(color.toJson());
            }
            pJson.add("colors", colorsJson);
            pJson.addProperty("result", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result)).toString());
        }

        @Override
        public @NotNull ResourceLocation getId() {
            return this.id;
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() {
            return ChiselTableRecipeBuilder.SERIALIZER;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancementBuilder.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
