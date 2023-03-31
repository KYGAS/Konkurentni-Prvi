package cli;

import DirCrawler.DirectoryCrawler;
import JobQueue.*;

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
                    System.out.println("Directory added : " + path);
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
                    System.out.println("Website added : " + path);
                    JobQueue.addJob(new WebJob());
                }
                else {
                    System.out.println("Invalid parameter length for aw command");
                }
            }
            case "get" -> {
                // TODO: Implement blocking retrieve result
            }
            case "query" -> {
                // TODO: Implement non blocking retrieve result
            }
            case "cws" -> {
                // TODO: Implement clear web cache
            }
            case "cfs" -> {
                // TODO: Implement clear file cache
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
