package com.dusty_dusty.meds_and_herbs;

import com.mojang.logging.LogUtils;
import com.dusty_dusty.meds_and_herbs.client.gui.screen.DistilleryApparatusGUIScreen;
import com.dusty_dusty.meds_and_herbs.client.gui.screen.ExtractApparatusGUIScreen;
import com.dusty_dusty.meds_and_herbs.client.gui.screen.IncubatorGUIScreen;
import com.dusty_dusty.meds_and_herbs.init.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod("meds_and_herbs")
public class MedsAndHerbs {
    public static final String MODID = "meds_and_herbs";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public MedsAndHerbs(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        MHSounds.REGISTRY.register(modEventBus);
        MHBlocks.REGISTRY.register(modEventBus);
        MHBlockEntities.REGISTRY.register(modEventBus);
        MHRecipes.SERIALIZERS.register(modEventBus);
        MHRecipes.TYPES.register(modEventBus);
        MHItems.REGISTRY.register(modEventBus);
        MHItems.ITEMS.register(modEventBus);
        MHEffects.REGISTRY.register(modEventBus);
        MHVillagerProfessions.POI_TYPES.register(modEventBus);
        MHVillagerProfessions.PROFESSIONS.register(modEventBus);
        MHMenus.REGISTRY.register(modEventBus);
        MHCreativeTabs.REGISTRY.register(modEventBus);
        modEventBus.register(this);
    }

    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(
                () -> {
                    MenuScreens.register(MHMenus.DISTILLERY_APPARATUS_GUI.get(), DistilleryApparatusGUIScreen::new);
                    MenuScreens.register(MHMenus.EXTRACTION_APPARATUS_GUI.get(), ExtractApparatusGUIScreen::new);
                    MenuScreens.register(MHMenus.INCUBATOR_GUI.get(), IncubatorGUIScreen::new);
                }
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent
    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            FlowerPotBlock flowerPot = (FlowerPotBlock) net.minecraft.world.level.block.Blocks.FLOWER_POT;
            flowerPot.addPlant(MHBlocks.VINCA.getId(), MHBlocks.POTTED_VINCA);
            flowerPot.addPlant(MHBlocks.BELLADONNA.getId(), MHBlocks.POTTED_BELLADONNA);
            flowerPot.addPlant(MHBlocks.CHAMOMILE.getId(), MHBlocks.POTTED_CHAMOMILE);
            flowerPot.addPlant(MHBlocks.ARTEMISIA.getId(), MHBlocks.POTTED_ARTEMISIA);
            flowerPot.addPlant(MHBlocks.OPIUM.getId(), MHBlocks.POTTED_OPIUM);
        });
    }
}