package com.postyizhan.shieldmite.mixin;

import net.minecraft.ChatAllowedCharacters;
import net.minecraft.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * MITE's legacy font width table has 256 entries, while system-locale text
 * such as the date shown in the world list can contain wider Unicode chars.
 */
@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    @Inject(method = "getCharWidth(C)I", at = @At("HEAD"), cancellable = true)
    private void shieldMite$guardCharWidth(char character, CallbackInfoReturnable<Integer> cir) {
        int allowedIndex = ChatAllowedCharacters.allowedCharacters.indexOf(character);
        if (character >= 256 || (allowedIndex >= 0 && allowedIndex + 32 >= 256)) {
            cir.setReturnValue(8);
        }
    }

    @Inject(method = "renderCharAtPos(ICZ)F", at = @At("HEAD"), cancellable = true)
    private void shieldMite$skipUnsupportedGlyph(
        int allowedIndex,
        char character,
        boolean italic,
        CallbackInfoReturnable<Float> cir) {
        if (allowedIndex >= 0 && allowedIndex + 32 >= 256) {
            cir.setReturnValue(8.0F);
        }
    }
}
