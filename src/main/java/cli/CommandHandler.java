package cli;

import DirCrawler.DirectoryCrawler;
import JobQueue.*;
import ResultRetriever.ResultRetriever;
import config.Config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Scanner;

public class CommandHandler {

    ArrayList<Object> runningObjects;
    public void handleCommand(String command) {
        String[] parts = command.split(" ");
        String cmd = parts[0];

        switch (cmd) {
            case "ad" -> {
                if (parts.length >= 2) {
                    String path = parseStringParameter(command.substring(cmd.length() + 1));
                    for(Object component : runningObjects){
                        if(component instanceof DirectoryCrawler){
                            ((DirectoryCrawler) component).addDirectory(path);
                        }
                    }
                }
                else {
                    System.out.println("Invalid parameter length for ad command");
                }
            }
            case "aw" -> {
                if (parts.length >= 2) {
                    String path = parseStringParameter(command.substring(cmd.length() + 1));
                    JobQueue.addJob(new WebJob(path, new Config().getHopCount(), null));
                }
                else {
                    System.out.println("Invalid parameter length for aw command");
                }
            }
            case "get" -> {
                try {
                    cmd = command.substring(cmd.length() + 1);
                    String pool = cmd.split("\\|")[0];
                    String source = cmd.substring(pool.length() + 1);

                    for(Object component : runningObjects){
                        if(component instanceof ResultRetriever){
                            ((ResultRetriever) component).get(pool, source);
                        }
                    }
                }catch (Exception e){
                    System.out.println("Query arguments were invalid.");
                }
            }
            case "query" -> {
                try {
                    cmd = command.substring(cmd.length() + 1);
                    String pool = cmd.split("\\|")[0];
                    String source = cmd.substring(pool.length() + 1);

                    for (Object component : runningObjects) {
                        if (component instanceof ResultRetriever) {
                            ((ResultRetriever) component).fetch(pool, source);
                        }
                    }
                }catch (Exception e){
                    System.out.println("Query arguments were invalid.");
                }
            }
            case "cws" -> {
                ResultRetriever.cache.remove("web");
            }
            case "cfs" -> {
                ResultRetriever.cache.remove("file");
            }
            case "stop" -> stopApplication();
            default -> System.out.println("Unknown command: " + cmd);
        }
    }

    public void beginInputRead(){
        Scanner scanner = new Scanner(System.in);

        String command = "";
        while (!command.startsWith("stop")) {
            System.out.print("Enter command: ");
            command = scanner.nextLine();
            handleCommand(command);
        }
        System.out.println("Stopping command handler!");
    }

    public CommandHandler() {
        runningObjects = new ArrayList<>();
    }

    public void addRunningObject(Object object){
        runningObjects.add(object);
    }

    private void stopApplication(){
        for(Object object: runningObjects){
            try {
                Method stopMethod = object.getClass().getMethod("stop");
                stopMethod.invoke(object);
                System.out.println("Stopping : " + object.getClass());
            }
            catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException ignored){
                System.out.println("No stop method found for : " + object.getClass());
            }
        }
    }

    private String parseStringParameter(String param) {
        if (param.startsWith("\"") && param.endsWith("\"")) {
            return param.substring(1, param.length() - 1);
        }
        else {
            return param;
        }
    }
}
