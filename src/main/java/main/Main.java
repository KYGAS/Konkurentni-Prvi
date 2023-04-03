package main;

import DirCrawler.DirectoryCrawler;
import JobDispatcher.JobDispatcher;
import ResultRetriever.Retriever.WebRetriever;
import cli.CommandHandler;
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
            exitApp("Config loader error!", true);
        }
        else {
            System.out.println("App config loaded...");
        }
        // ---------------------------------- Loaded Config

        // ---------------------------------- Loading Directory Crawler
        Thread directoryCrawlerThread;
        DirectoryCrawler directoryCrawler;
        System.out.println("Loading directory crawler...");
        try{
            directoryCrawler = new DirectoryCrawler();
            directoryCrawlerThread = new Thread(directoryCrawler);
            directoryCrawlerThread.start();
        }
        catch (Exception ignored){
            directoryCrawler = null;
        }
        if(directoryCrawler == null){
            exitApp("Directory crawler error!", true);
        }
        else {
            System.out.println("Directory crawler loaded...");
        }
        // ---------------------------------- Loaded Directory Crawler

        // ---------------------------------- Loading Job Dispatcher
        Thread jobDispatcherThread;
        JobDispatcher jobDispatcher;
        System.out.println("Loading job dispatcher...");
        try{
            jobDispatcher = new JobDispatcher();
            jobDispatcherThread = new Thread(jobDispatcher);
            jobDispatcherThread.start();
        }
        catch (Exception ignored){
            jobDispatcher = null;
        }
        if(jobDispatcher == null){
            exitApp("Job Dispatcher error!", true);
        }
        else {
            System.out.println("Job Dispatcher loaded...");
        }
        // ---------------------------------- Loaded Job Dispatcher


        WebRetriever webRetriever = new WebRetriever();


        // ---------------------------------- Creating a command handler
        CommandHandler commandHandler;
        System.out.println("Loading directory crawler...");
        try{
            commandHandler = new CommandHandler();
        }
        catch (Exception ignored){
            commandHandler = null;
        }
        if(commandHandler == null){
            exitApp("Command handler couldn't be created!", true);
        }
        else {
            System.out.println("Command handler created!");
            commandHandler.addRunningObject(directoryCrawler);
            commandHandler.addRunningObject(jobDispatcher);
            commandHandler.beginInputRead();
        }
        // ---------------------------------- Created a command handler
        exitApp("Application closed!", false);
    }


    private static void exitApp(String message, boolean forceStop){
        System.out.println(message);
        if(forceStop) System.exit(0);
    }
}