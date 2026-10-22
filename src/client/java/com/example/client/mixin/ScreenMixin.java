package com.example.client.mixin;

import com.example.client.AltAsCtrlClient;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Vanilla asks Screen.hasControlDown() for every "Ctrl" action: Ctrl+Q (drop stack, in world and in
 * inventories), Ctrl+click, Ctrl+A/C/V/X (isSelectAll/isCopy/isPaste/isCut call it internally).
 * Making it return true while Alt is held moves all of them to Alt.
 */
@Mixin(Screen.class)
public abstract class ScreenMixin {
	@Inject(method = "hasControlDown", at = @At("HEAD"), cancellable = true)
	private static void altAsCtrl$hasControlDown(CallbackInfoReturnable<Boolean> cir) {
		if (AltAsCtrlClient.isAltDown()) {
			cir.setReturnValue(true);
		} else if (AltAsCtrlClient.altOnly) {
			cir.setReturnValue(false);
		}
	}
}
