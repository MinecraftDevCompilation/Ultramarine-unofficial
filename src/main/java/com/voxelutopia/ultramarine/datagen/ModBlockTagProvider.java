package com.voxelutopia.ultramarine.datagen;

import com.voxelutopia.ultramarine.data.registry.BlockRegistry;
import com.voxelutopia.ultramarine.world.block.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, DataGenerators.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        BlockRegistry.BLOCKS.getEntries().stream().filter(blockRegistryObject -> blockRegistryObject.get() instanceof BaseBlockPropertyHolder)
                        .forEach(blockRegistryObject -> {
                            Block block = blockRegistryObject.get();
                            BaseBlockProperty property = ((BaseBlockPropertyHolder)block).getProperty();
                            tag(property.getMaterial().getTool()).add(block);
                        });
        BlockRegistry.BLOCKS.getEntries().stream().filter(blockRegistryObject -> blockRegistryObject.get() instanceof RoofTiles)
                .forEach(blockRegistryObject -> tag(BlockTags.MINEABLE_WITH_PICKAXE).add(blockRegistryObject.get()));
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(BlockRegistry.JADE_ORE.get())
                .add(BlockRegistry.DEEPSLATE_JADE_ORE.get())
                .add(BlockRegistry.MAGNESITE_ORE.get())
                .add(BlockRegistry.DEEPSLATE_MAGNESITE_ORE.get())
                .add(BlockRegistry.HEMATITE_ORE.get())
                .add(BlockRegistry.DEEPSLATE_HEMATITE_ORE.get())
                .add(BlockRegistry.NETHER_COBALT_ORE.get());
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(BlockRegistry.MAGNESITE_ORE.get())
                .add(BlockRegistry.DEEPSLATE_MAGNESITE_ORE.get())
                .add(BlockRegistry.HEMATITE_ORE.get())
                .add(BlockRegistry.DEEPSLATE_HEMATITE_ORE.get());
        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(BlockRegistry.JADE_ORE.get())
                .add(BlockRegistry.DEEPSLATE_JADE_ORE.get())
                .add(BlockRegistry.NETHER_COBALT_ORE.get());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(BlockRegistry.OCTAGONAL_PALACE_LANTERN.get())
                .add(BlockRegistry.SQUARE_PALACE_LANTERN.get())
                .add(BlockRegistry.STANDING_LAMP.get())
                .add(BlockRegistry.SMALL_STANDING_LAMP.get())
                .add(BlockRegistry.WHITE_SKY_LANTERN.get())
                .add(BlockRegistry.RED_SKY_LANTERN.get())
                .add(BlockRegistry.YELLOW_SKY_LANTERN.get());
        tag(BlockTags.PLANKS)
                .add(BlockRegistry.ROSEWOOD_PLANKS.get());
        BlockRegistry.BLOCKS.getEntries().stream().filter((blockRegistryObject -> blockRegistryObject.get() instanceof BaseWall))
                .forEach(blockRegistryObject -> tag(BlockTags.WALLS).add(blockRegistryObject.get()));
        BlockRegistry.BLOCKS.getEntries().stream().filter((blockRegistryObject -> blockRegistryObject.get() instanceof BaseFence))
                .forEach(blockRegistryObject -> tag(BlockTags.FENCES).add(blockRegistryObject.get()));
    }

    @Override
    public @NotNull String getName() {
        return DataGenerators.MOD_ID + " Block Tags";
    }

}