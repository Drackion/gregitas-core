package com.allthemods.gravitas2.block;

import com.allthemods.gravitas2.GregitasCore;
import com.allthemods.gravitas2.item.PressurePipeBlockItem;
import com.allthemods.gravitas2.pipelike.pressure.PressurePipeType;
import com.allthemods.gravitas2.registry.GregitasCreativeModeTabs;
import com.gregtechceu.gtceu.api.block.IFilterType;
import com.gregtechceu.gtceu.api.block.RendererBlock;
import com.gregtechceu.gtceu.api.item.RendererBlockItem;
import com.gregtechceu.gtceu.api.item.tool.GTToolType;
import com.gregtechceu.gtceu.client.renderer.block.TextureOverrideRenderer;
import com.gregtechceu.gtceu.data.recipe.CustomTags;
import com.lowdragmc.lowdraglib.Platform;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

import java.util.Map;

import static com.allthemods.gravitas2.registry.GregitasRegistry.GREGITAS_REGISTRATE;

@SuppressWarnings("removal")
public class GregitasBlocks {


    static {
        GREGITAS_REGISTRATE.creativeModeTab(() -> GregitasCreativeModeTabs.GREGITAS_CORE);
    }

    @SuppressWarnings("unchecked")
    public static final BlockEntry<PressurePipeBlock>[] PRESSURE_PIPE_BLOCKS = new BlockEntry[PressurePipeType.values().length];

    private static void generatePressurePipeBlocks() {
        for (int i = 0; i < PressurePipeType.values().length; ++i) {
            var type = PressurePipeType.values()[i];
            PRESSURE_PIPE_BLOCKS[i] = GREGITAS_REGISTRATE.block("%s_pipe".formatted(type.getSerializedName()), (p) -> new PressurePipeBlock(p, type))
                    .initialProperties(() -> Blocks.IRON_BLOCK)
                    .properties(p -> p.dynamicShape().noOcclusion())
                    .blockstate(NonNullBiConsumer.noop())
                    .addLayer(() -> RenderType::cutoutMipped)
                    .item(PressurePipeBlockItem::new)
                    .model(NonNullBiConsumer.noop())
                    .build()
                    .register();
        }
    }

    public static final BlockEntry<Block> ULTRA_STERILIZING_FILTER_CASING = createCleanroomFilter(CleanerroomFilterType.INSTANCE);


    private static BlockEntry<Block> createCleanroomFilter(IFilterType filterType) {
        return GREGITAS_REGISTRATE.block(filterType.getSerializedName(), p -> (Block) new RendererBlock(p,
                        Platform.isClient() ? new TextureOverrideRenderer(new ResourceLocation("block/cube_all"),
                                Map.of("all", GregitasCore.id("block/casings/cleanroom/" + filterType.getSerializedName()))) : null))
                .initialProperties(() -> Blocks.IRON_BLOCK)
                .properties(properties -> properties.strength(2.0f, 8.0f).sound(SoundType.METAL).isValidSpawn((blockState, blockGetter, blockPos, entityType) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(NonNullBiConsumer.noop())
                .tag(GTToolType.WRENCH.harvestTags.get(0), CustomTags.TOOL_TIERS[1])
                .item(RendererBlockItem::new)
                .model(NonNullBiConsumer.noop())
                .build()
                .register();
    }

    public static void init() {
        generatePressurePipeBlocks();
    }
}
