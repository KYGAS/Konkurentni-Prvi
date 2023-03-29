package main;

import config.Config;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // ---------------------------------- Loading Config
        Config config;
        System.out.println("Loading app config...");
        try{
            config = new Config();
            config.loadProperties();
        }
        catch (IOException | NullPointerException e) {
            config = null;
            System.out.println("Config doesn't exist.");
        }
        if(config == null){
            System.exit(0);
        }
        else {
            System.out.println("App config loaded...");
        }
        // ---------------------------------- Loaded Config

    }
}