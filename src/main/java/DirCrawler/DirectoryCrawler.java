package DirCrawler;

import JobQueue.*;
import config.Config;
import utils.Notifier;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DirectoryCrawler implements Runnable {
    private final String corpusDirectoryPrefix;
    private final long scanInterval;
    private final Config config;
    private static final Object lock1 = new Object();


    private final ArrayList<String> directoriesToScan;
    private volatile boolean isRunning;

    private HashMap<String, Long> lastModifiedMap;

    public DirectoryCrawler() {
        config = new Config();
        corpusDirectoryPrefix = config.getFileCorpusPrefix();
        scanInterval = config.getDirCrawlerSleepTime();
        directoriesToScan = new ArrayList<>();
        lastModifiedMap = new HashMap<>();
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            for (String directory : new ArrayList<>(directoriesToScan)) {
                traverseDirectory(new File(directory));
            }

            try {
                Thread.sleep(scanInterval);
            }
            catch (InterruptedException e) {
                Notifier.notifyObjectStopped(this);
                return;
            }
        }
        Notifier.notifyObjectStopped(this);
    }

    public void stop(){
        isRunning = false;
    }

    public void addDirectory(String path){
        synchronized (lock1) {
            directoriesToScan.add(path);
        }
    }

    public void removeDirectory(String path){
        synchronized (lock1) {
            directoriesToScan.remove(path);
        }
    }

    private void traverseDirectory(File directory) {
        if (directory == null) {
            System.out.println("Path can't be null.");
            removeDirectory(null);
            return;
        }
        if (!directory.isDirectory()) {
            System.out.println("Path is not a directory.");
            removeDirectory(directory.getName());
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory() && file.getName().startsWith(corpusDirectoryPrefix)) {

                if(lastModifiedMap.containsKey(file.getPath()))
                    if(lastModifiedMap.get(file.getPath()) == file.lastModified()) continue;

                JobQueue.addJob(new FileJob(file.getPath()));
                lastModifiedMap.put(file.getPath(), file.lastModified());

            }
            else if (file.isFile()) {
                // zabelezi zadnje menjanje ( datum )
            }
        }

        for (File subdirectory : directory.listFiles(File::isDirectory)) {
            traverseDirectory(subdirectory);
        }
    }
}
