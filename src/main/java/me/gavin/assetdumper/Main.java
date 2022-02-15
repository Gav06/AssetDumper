package me.gavin.assetdumper;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            new Dumper(new Config());
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find config.toml file, download default config? (y/n)");
            String in = new Scanner(System.in).nextLine();
            if (in.equalsIgnoreCase("y")) {
                downloadDefaultConfig();
            }
        }
    }

    private static void downloadDefaultConfig() {
        System.out.println("Downloading config...");
        final String url
    }
}
