package me.gavin.assetdumper;

import java.io.File;

public class Dumper {

//    private final File versionJson;
//    private final File assetIndex;
    private final Config config;

    public Dumper(Config config) {
        this.config = config;
        System.out.println(config.getToml().getString("minecraft-folder"));
    }



    public Config getConfig() {
        return config;
    }
//
//    public File getAssetIndex() {
//        return assetIndex;
//    }
//
//    public File getVersionJson() {
//        return versionJson;
//    }
}