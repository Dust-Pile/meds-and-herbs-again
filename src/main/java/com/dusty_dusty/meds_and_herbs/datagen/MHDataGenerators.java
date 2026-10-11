package com.dusty_dusty.meds_and_herbs.datagen;

import com.dusty_dusty.meds_and_herbs.MedsAndHerbs;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MedsAndHerbs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MHDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new MHItemModelProvider(packOutput, existingFileHelper));
    }
}