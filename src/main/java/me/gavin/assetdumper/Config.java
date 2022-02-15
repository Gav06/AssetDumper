package me.gavin.assetdumper;

import com.moandjiezana.toml.Toml;

import java.io.File;
import java.io.FileNotFoundException;

public class Config {

    private final File configFile;
    private final Toml toml;

    public Config() throws FileNotFoundException {
        configFile = new File("config.toml");

        if (!configFile.exists())
            throw new FileNotFoundException("config.toml");

        toml = new Toml().read(configFile);

    }

    public File getConfigFile() {
        return configFile;
    }

    public Toml getToml() {
        return toml;
    }
}
