package com.dusty_dusty.meds_and_herbs.client;

import com.dusty_dusty.meds_and_herbs.MedsAndHerbs;
import com.dusty_dusty.meds_and_herbs.data.MedicineDefinition;
import com.dusty_dusty.meds_and_herbs.data.MedicineTypeLoader;
import com.dusty_dusty.meds_and_herbs.init.MHItems;
import com.dusty_dusty.meds_and_herbs.item.Medicine;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = MedsAndHerbs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            if (tintIndex == 0) return 0xFFFFFFFF;
            else if (tintIndex == 1) {
                ResourceLocation typeId = Medicine.getType(stack);
                MedicineDefinition def = MedicineTypeLoader.get(typeId);
                return def != null ? def.color() : 0xFFFFFFFF;
            }
            return 0xFFFFFFFF;
        }, MHItems.MEDICINE.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(MHItems.MEDICINE.get(),
                    MedsAndHerbs.id("medicine_state"),
                    (stack, level, entity, seed) -> {
                        int typeIndex = MedicineTypeLoader.indexOf(Medicine.getType(stack));
                        int uses = Medicine.getUses(stack);
                        return typeIndex + uses / 10.0f;
                    });
        });
    }
}