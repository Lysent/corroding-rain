package net.reversteam.weathering;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.reversteam.weathering.registry.ModBlocks;
import net.reversteam.weathering.registry.ModItems;
import org.slf4j.Logger;

@Mod(WeatheringMod.MOD_ID)
public class WeatheringMod {
    public static final String MOD_ID = "weathering";
    public static final Logger LOGGER = LogUtils.getLogger();

    public WeatheringMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        modBus.addListener(this::addCreativeTabContents);
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModBlocks.RUSTED_MACHINE_LIGHT);
            event.accept(ModBlocks.RUSTED_MACHINE);
            event.accept(ModBlocks.RUSTED_MACHINE_HEAVY);
        }
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.MACHINE_SCRAP);
        }
    }
}
