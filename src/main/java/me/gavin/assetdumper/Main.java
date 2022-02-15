package me.gavin.assetdumper;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            new Dumper(new Config());
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find " + e.getMessage());
            if (e.getMessage().equals("config.toml")) {
                System.out.println("Download the default config? (y/n)");
                String in = new Scanner(System.in).nextLine();
                if (in.equalsIgnoreCase("y")) {
                    downloadDefaultConfig();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void downloadDefaultConfig() {
        System.out.println("Downloading config...");
        final String url = "https://raw.githubusercontent.com/Gav06/AssetDumper/master/config.toml";

        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            FileOutputStream os = new FileOutputStream("config.toml");
            final byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            in.close();
            os.flush();
            os.close();

            System.out.println("File downloaded successfully");
        } catch (IOException e) {
            System.err.println("Error downloading default config, make one yourself idk");
        }
    }
}
