package com.qxzap.zoomers_aether_compat_forge.mixin;

import com.aetherteam.aether.blockentity.ChestMimicBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import com.aetherteam.aether.client.renderer.blockentity.ChestMimicRenderer;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import noobanidus.mods.lootr.config.ConfigManager;

@Mixin(ChestMimicRenderer.class)
public class ChestMimicRendererMixin {

    @Shadow
    private boolean xmasTextures;

    private static final Material OLD_LOOTR_MATERIAL = new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "old_chest"));

    @Inject(method = "getMaterial", at = @At("HEAD"), remap = false, cancellable = true)
    private void injectCustomMaterial(ChestMimicBlockEntity blockEntity, CallbackInfoReturnable<Material> cir) {
        if (ModList.get().isLoaded("lootr")) {
            if (!ConfigManager.isVanillaTextures()) {
                if (ConfigManager.isOldTextures()) {
                    cir.setReturnValue(OLD_LOOTR_MATERIAL);
                    cir.cancel();
                } else {
                    cir.setReturnValue(new Material(Sheets.CHEST_SHEET, new ResourceLocation("lootr", "chest")));
                    cir.cancel();
                }
                return;
            }
        }
        // Default behavior if no conditions match
        cir.setReturnValue(Sheets.chooseMaterial(blockEntity, ChestType.SINGLE, this.xmasTextures));
        cir.cancel();
    }
}