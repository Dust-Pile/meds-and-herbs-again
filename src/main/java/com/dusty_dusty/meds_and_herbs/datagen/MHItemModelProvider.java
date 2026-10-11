package com.dusty_dusty.meds_and_herbs.datagen;

import com.dusty_dusty.meds_and_herbs.MedsAndHerbs;
import com.dusty_dusty.meds_and_herbs.item.Medicine;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class MHItemModelProvider extends ItemModelProvider {

    public MHItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MedsAndHerbs.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        List<String> types = findMedicineTypes();
        ResourceLocation state = MedsAndHerbs.id("medicine_state");

        ItemModelBuilder base = withExistingParent("medicine", mcLoc("item/generated"))
                .texture("layer0", modLoc("item/medicine/medicine_bottle_2"));

        for (int i = 0; i < types.size(); i++) {
            for (int uses = 1; uses <= Medicine.MAX_USES; uses++) {
                String name = types.get(i) + "_bottle_" + (Medicine.MAX_USES - uses);
                ResourceLocation tex = modLoc("item/medicine/" + name);

                if (!existingFileHelper.exists(tex.withPrefix("textures/").withSuffix(".png"), PackType.CLIENT_RESOURCES)) {
                    continue;
                }

                base.override()
                        .predicate(state, i + uses / 10.0f)
                        .model(withExistingParent("medicine/" + name, mcLoc("item/generated")).texture("layer0", tex))
                        .end();
            }
        }
    }

    private static List<String> findMedicineTypes() {
        Path dir = Path.of("").toAbsolutePath().getParent()
                .resolve("src/main/resources/data")
                .resolve(MedsAndHerbs.MODID)
                .resolve("medicine_types");

        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .map(p -> p.getFileName().toString().replaceFirst("\\.json$", ""))
                    .filter(name -> !name.endsWith(".json"))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            return List.of();
        }
    }
}