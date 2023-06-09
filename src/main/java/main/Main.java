package main;

import DirCrawler.DirectoryCrawler;
import JobDispatcher.JobDispatcher;
import Jobs.Scanners.File;
import Jobs.Scanners.Web;
import ResultRetriever.ResultRetriever;
import ResultRetriever.Retriever.FileRetriever;
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


        // ---------------------------------- Loading Result Retriever
        Thread resultRetrieverThread;
        ResultRetriever resultRetriever;
        System.out.println("Loading result retriever...");
        try{
            resultRetriever = new ResultRetriever();
            resultRetrieverThread = new Thread(resultRetriever);
            resultRetrieverThread.start();
        }
        catch (Exception ignored){
            resultRetriever = null;
        }
        if(jobDispatcher == null){
            exitApp("Result retriever error!", true);
        }
        else {
            System.out.println("Result retreiver loaded...");
        }
        // ---------------------------------- Loaded Result Retriever


        // ---------------------------------- Creating a command handler
        CommandHandler commandHandler;
        System.out.println("Loading command handler...");
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
            commandHandler.addRunningObject(jobDispatcher.innitWebPool());
            commandHandler.addRunningObject(jobDispatcher.innitFilePool());
            commandHandler.addRunningObject(jobDispatcher.getWebScanner());
            commandHandler.addRunningObject(jobDispatcher.getFileScanner());
            commandHandler.addRunningObject(resultRetriever);
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