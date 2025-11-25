package net.reversteam.weathering.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reversteam.weathering.WeatheringMod;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WeatheringMod.MOD_ID);

    public static final RegistryObject<Block> RUSTED_MACHINE_LIGHT = BLOCKS.register("rusted_machine_light",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .strength(2.0F, 4.0F)
                    .sound(SoundType.COPPER)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> RUSTED_MACHINE = BLOCKS.register("rusted_machine",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.COPPER)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> RUSTED_MACHINE_HEAVY = BLOCKS.register("rusted_machine_heavy",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GRAY)
                    .strength(5.0F, 10.0F)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .requiresCorrectToolForDrops()));

    private ModBlocks() {
    }
}
