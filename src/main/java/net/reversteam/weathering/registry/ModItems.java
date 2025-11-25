package net.reversteam.weathering.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.reversteam.weathering.WeatheringMod;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WeatheringMod.MOD_ID);

    public static final RegistryObject<Item> RUSTED_MACHINE_LIGHT = ITEMS.register("rusted_machine_light",
            () -> new BlockItem(ModBlocks.RUSTED_MACHINE_LIGHT.get(), new Item.Properties()));

    public static final RegistryObject<Item> RUSTED_MACHINE = ITEMS.register("rusted_machine",
            () -> new BlockItem(ModBlocks.RUSTED_MACHINE.get(), new Item.Properties()));

    public static final RegistryObject<Item> RUSTED_MACHINE_HEAVY = ITEMS.register("rusted_machine_heavy",
            () -> new BlockItem(ModBlocks.RUSTED_MACHINE_HEAVY.get(), new Item.Properties()));

    public static final RegistryObject<Item> MACHINE_SCRAP = ITEMS.register("machine_scrap",
            () -> new Item(new Item.Properties()));

    private ModItems() {
    }
}
