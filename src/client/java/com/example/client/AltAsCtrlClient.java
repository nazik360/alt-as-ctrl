package com.example.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

/**
 * Alt as Ctrl. Client-only: nothing here touches the server, so other players
 * can join your world (LAN / dedicated server) without installing the mod.
 *
 * 1) GUI (inventory, chests, text fields, ...): the GLFW key and mouse-button
 *    callbacks are wrapped and the "modifiers" bitmask is rewritten, so
 *    Alt+click, Alt+Q, Alt+A/C/V ... behave exactly like Ctrl+... did.
 * 2) In the world: Alt+Q drops the whole stack (vanilla Ctrl+Q).
 */
public final class AltAsCtrlClient implements ClientModInitializer {

    private static boolean altOnly;

    @Override
    public void onInitializeClient() {
        altOnly = Config.load().altOnly;

        // Minecraft installs its own GLFW callbacks during startup; wrap them afterwards.
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> installCallbacks());

        ClientTickEvents.START_CLIENT_TICK.register(AltAsCtrlClient::onTick);
    }

    /** Rewrites GLFW modifier bits: Alt -> Ctrl (and optionally drops the real Ctrl). */
    static int remap(int mods) {
        boolean alt = (mods & GLFW.GLFW_MOD_ALT) != 0;
        int out = mods;
        if (alt) {
            out |= GLFW.GLFW_MOD_CONTROL;
        } else if (altOnly) {
            out &= ~GLFW.GLFW_MOD_CONTROL;
        }
        return out;
    }

    private static void installCallbacks() {
        long window = GLFW.glfwGetCurrentContext();
        if (window == 0L) {
            return;
        }

        GLFWKeyCallback[] prevKey = new GLFWKeyCallback[1];
        prevKey[0] = GLFW.glfwSetKeyCallback(window, (w, key, scancode, action, mods) -> {
            if (prevKey[0] != null) {
                prevKey[0].invoke(w, key, scancode, action, remap(mods));
            }
        });

        GLFWMouseButtonCallback[] prevMouse = new GLFWMouseButtonCallback[1];
        prevMouse[0] = GLFW.glfwSetMouseButtonCallback(window, (w, button, action, mods) -> {
            if (prevMouse[0] != null) {
                prevMouse[0].invoke(w, button, action, remap(mods));
            }
        });
    }

    private static boolean isAltDown() {
        long window = GLFW.glfwGetCurrentContext();
        if (window == 0L) {
            return false;
        }
        return GLFW.glfwGetKey(window, GLFW.GLFW_KEY_LEFT_ALT) == GLFW.GLFW_PRESS
                || GLFW.glfwGetKey(window, GLFW.GLFW_KEY_RIGHT_ALT) == GLFW.GLFW_PRESS;
    }

    /** In-world Alt+Q: runs before vanilla handles keybinds, consumes the drop key and drops the full stack. */
    private static void onTick(Minecraft client) {
        if (client.player == null || client.screen != null || !isAltDown()) {
            return;
        }
        while (client.options.keyDrop.consumeClick()) {
            if (!client.player.isSpectator() && client.player.drop(true)) {
                client.player.swing(InteractionHand.MAIN_HAND);
            }
        }
    }
}
