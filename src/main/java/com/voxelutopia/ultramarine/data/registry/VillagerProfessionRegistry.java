package com.voxelutopia.ultramarine.data.registry;

import com.google.common.collect.ImmutableSet;
import com.voxelutopia.ultramarine.Ultramarine;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class VillagerProfessionRegistry {

    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Ultramarine.MOD_ID);

    public static final RegistryObject<VillagerProfession> COOK = PROFESSIONS.register("cook",
            () -> new VillagerProfession("cook", (a) -> {
                if (PoiTypeRegistry.COOKING_POI.getKey() != null) {
                    return a.is(PoiTypeRegistry.COOKING_POI.getKey());
                }
                return false;
            }, PoiType.NONE, ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_BUTCHER));
}
