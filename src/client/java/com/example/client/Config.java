package com.example.client;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Tiny properties config: config/alt-as-ctrl.properties */
final class Config {

    /** true  = real Ctrl no longer acts as Ctrl in GUIs (strict "move to Alt").
     *  false = both Alt and Ctrl work (default, least surprising). */
    boolean altOnly = false;

    static Config load() {
        Config cfg = new Config();
        Path file = FabricLoader.getInstance().getConfigDir().resolve("alt-as-ctrl.properties");
        Properties props = new Properties();
        try {
            if (Files.exists(file)) {
                try (InputStream in = Files.newInputStream(file)) {
                    props.load(in);
                }
                cfg.altOnly = Boolean.parseBoolean(props.getProperty("altOnly", "false"));
            } else {
                props.setProperty("altOnly", "false");
                try (OutputStream out = Files.newOutputStream(file)) {
                    props.store(out, "altOnly=true -> real Ctrl stops acting as Ctrl in GUIs; Alt only");
                }
            }
        } catch (IOException ignored) {
            // fall back to defaults
        }
        return cfg;
    }
}
