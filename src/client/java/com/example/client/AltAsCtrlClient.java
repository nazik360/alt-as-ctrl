package com.example.client;

import net.fabricmc.api.ClientModInitializer;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;

/**
 * Alt as Ctrl (1.19.2). Client-only: servers and other players are unaffected,
 * so friends can join your world without the mod.
 */
public final class AltAsCtrlClient implements ClientModInitializer {

	public static boolean altOnly;

	@Override
	public void onInitializeClient() {
		altOnly = Config.load().altOnly;
	}

	public static boolean isAltDown() {
		long window = Minecraft.getInstance().getWindow().getWindow();
		return InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_ALT)
				|| InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_ALT);
	}
}
