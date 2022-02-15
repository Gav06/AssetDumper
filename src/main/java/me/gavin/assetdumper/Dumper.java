package me.gavin.assetdumper;

import com.google.gson.*;

import java.io.*;
import java.util.Map;

public class Dumper {

    private final File versionJson;
    private final Config config;
    private final File minecraftDir;
    private final File outputDir;

    private File assetIndex;

    public Dumper(Config config) throws FileNotFoundException, IOException {
        this.config = config;

        final String mcDirPath = config.getToml().getString("minecraft-folder")
                .replace("%username%", System.getProperty("user.name"));

        minecraftDir = new File(mcDirPath);
        outputDir = new File(config.getToml().getString("output-dir")
                .replace("%username%", System.getProperty("user.name")));

        if (!outputDir.exists())
            outputDir.mkdir();

        if (!minecraftDir.exists()) {
            throw new FileNotFoundException(".minecraft folder");
        } else {
            System.out.println("Found .minecraft directory");
        }

        final String versionJsonName = config.getToml().getString("version");
        versionJson = new File(minecraftDir, "/versions/" + versionJsonName + "/" + versionJsonName + ".json");

        if (!versionJson.exists()) {
            System.err.println(versionJson.getPath());
            throw new FileNotFoundException("file " + versionJsonName);
        } else {
            System.out.println("Found version " + versionJsonName);
        }

        // reading asset index name from version.json
        final FileReader reader = new FileReader(versionJson);
        final JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();

        final String assetIndexName = obj.get("assetIndex").getAsJsonObject().get("id").getAsString();
        assetIndex = new File(minecraftDir, "/assets/indexes/" + assetIndexName + ".json");

        if (!assetIndex.exists()) {
            throw new FileNotFoundException("asset index " + assetIndexName + ".json");
        } else {
            System.out.println("Parsing asset index \"" + assetIndexName + ".json\"");
        }

        // parsing asset index and dumping files

        final JsonObject assetsObject = JsonParser.parseReader(new FileReader(assetIndex))
                .getAsJsonObject().get("objects").getAsJsonObject();

        System.out.println("Ready to copy " + assetsObject.entrySet().size() + " assets");

        for (Map.Entry<String, JsonElement> entry : assetsObject.entrySet()) {
            final JsonObject assetObj = entry.getValue().getAsJsonObject();
            final String hashedName = assetObj.get("hash").getAsString();
            final String objFolder = hashedName.substring(0, 2);
            final String actualName = entry.getKey();
            System.out.println(objFolder + "/" + hashedName + " -> " + actualName);

            // file copying
            final File hashedAsset = new File(minecraftDir + "/assets/objects/" + objFolder + "/" + hashedName);

            if (!hashedAsset.exists()) {
                System.err.println("Skipping " + hashedName + " (not found)");
            }

            final File dumpedAsset = new File(outputDir, "/" + actualName);

            if (!dumpedAsset.exists()) {
                dumpedAsset.getParentFile().mkdirs();
                dumpedAsset.createNewFile();
            }
            copyFile(hashedAsset, dumpedAsset);
        }

        System.out.println("\nDumped files to " + outputDir.getPath());
    }

    public Config getConfig() {
        return config;
    }

    public File getAssetIndex() {
        return assetIndex;
    }

    public File getVersionJson() {
        return versionJson;
    }

    public File getMinecraftDir() {
        return minecraftDir;
    }

    private void copyFile(File src, File dst) throws IOException {
        InputStream is = new FileInputStream(src);
        OutputStream os = new FileOutputStream(dst);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(buffer, 0, 1024)) != -1) {
            os.write(buffer, 0, bytesRead);
        }

        is.close();
        os.close();
    }
}