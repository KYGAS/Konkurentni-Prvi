package DirCrawler;

import config.Config;

import java.io.File;
import java.util.ArrayList;

public class DirectoryCrawler implements Runnable {
    private final String corpusDirectoryPrefix;
    private final long scanInterval;
    private final Config config;


    private ArrayList<String> directoriesToScan;
    private volatile boolean isRunning;

    public DirectoryCrawler() {
        config = new Config();
        corpusDirectoryPrefix = config.getFileCorpusPrefix();
        scanInterval = config.getDirCrawlerSleepTime();
        directoriesToScan = new ArrayList<>();
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            for (String directory : directoriesToScan) {
                traverseDirectory(new File(directory));
            }

            try {
                Thread.sleep(scanInterval);
            }
            catch (InterruptedException e) {
                System.out.println("Directory Crawler prekinut.");
                return;
            }
        }
        System.out.println("Stopped!");
    }

    public void stop(){
        isRunning = false;
    }

    private void traverseDirectory(File directory) {
        if (directory == null) return;
        if (!directory.isDirectory()) {
            return;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory() && file.getName().startsWith(corpusDirectoryPrefix)) {
                // dodaj u jobQueue
            } else if (file.isFile()) {
                // zabelezi zadnje menjanje ( datum )
            }
        }

        for (File subdirectory : directory.listFiles(File::isDirectory)) {
            traverseDirectory(subdirectory);
        }
    }
}
