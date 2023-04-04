package Jobs.Scanners;

import JobQueue.FileJob;
import ResultRetriever.Result.FileScanResult;
import ResultRetriever.Retriever.FileRetriever;
import config.Config;
import utils.Notifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Phaser;

public class File implements Runnable{
    // TODO : Contain File Job SCAN logic here
    public String path;
    private static volatile boolean isRunning = true;
    private ForkJoinPool pool;


    private Jobs.Divider.File divider;
    private static ArrayList<Phaser> phaserArrayList = new ArrayList<>();



    public File() {}

    public File(String path) {
        this.path = path;
    }

    public File(FileJob fileJob) {
        this.path = fileJob.path;
        this.pool = new ForkJoinPool();
    }

    @Override
    public void run() {
        if(!isRunning) return;
            StringBuilder corpus_content = new StringBuilder();
            java.io.File directory = new java.io.File(path);


            if (directory.exists() && directory.isDirectory()) {
                java.io.File[] files = directory.listFiles();
                if (isRunning) {
                    for (java.io.File file : files) {
                        if (file.isFile()) {
                            if(isRunning) {
                                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                                    String line;
                                    while ((line = reader.readLine()) != null && isRunning) {
                                        corpus_content.append(line);
                                    }
                                } catch (IOException e) {
                                    System.err.println("Error reading file " + file.getAbsolutePath() + ": " + e.getMessage());
                                }
                            }
                        }
                    }
                }
            }
            else {
                System.err.println("Invalid directory path");
                return;
            }

            // we have content in <corpus_content>

        if(!isRunning) return;
            String corpus = directory.getName();
            List<String> keywords = List.of(new Config().getKeywords());
            divider = new Jobs.Divider.File(corpus_content.toString(), keywords);
            HashMap<String, Integer> results = pool.invoke(divider);

        if(!isRunning) return;
            //FileRetriever.results.remove(path);
            FileRetriever.addResult(corpus, new FileScanResult(results, true));
    }

    public void stop(){
        isRunning = false;
        divider.stop();
        pool.shutdown();
    }
}
