package Jobs.Scanners;

import JobQueue.JobQueue;
import JobQueue.WebJob;
import ResultRetriever.Result.WebScanResult;
import ResultRetriever.Retriever.WebRetriever;
import config.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicInteger;

public class Web implements Runnable{
    String path;
    Integer hops;
    Web parentJob;
    Phaser phaser;
    HashMap<String, Integer> webScanResult;

    private static volatile boolean isRunning = true;
    private static ArrayList<Phaser> phaserArrayList = new ArrayList<>();

    public static List<String> visitedList = new ArrayList<>();

    public Web() {}

    public Web(String path, Integer hops) {
        this.path = path;
        this.hops = hops;
    }

    public Web(WebJob webJob) {
        this.path = webJob.path;
        this.hops = webJob.hops;
        this.parentJob = webJob.parentJob;
        this.webScanResult = new HashMap<>();
    }

    @Override
    public void run() {
        if(isRunning) {
            String[] keywords = new Config().getKeywords();
            Document doc = null;
            String text = null;

            try {
                System.out.println("Starting web scan for\nweb|" + this.path);
                doc = Jsoup.connect(path).get();
                text = doc.body().text();
            } catch (IOException e) {
                if (parentJob != null) {
                    parentJob.finishChildJob();
                    return;
                }
            }

            if(isRunning) {
                for (String keyword : keywords) {
                    countWord(text, keyword);
                }
            }

            if(isRunning) {
                if (hops > 0) createWebJobs(doc);
                else {
                    if (parentJob != null) {
                        compileResult(false);
                        parentJob.finishChildJob();
                    } else compileResult(true);
                }
            }
            else {
                if (parentJob != null) {
                    parentJob.phaser.bulkRegister(parentJob.phaser.getUnarrivedParties());
                }
            }
        }
        else if (parentJob != null) {
            parentJob.phaser.bulkRegister(parentJob.phaser.getUnarrivedParties());
        }
    }

    public void countWord(String text, String keyword){
        int count = 0;
        int index = 0;

        while (index != -1) {
            index = text.indexOf(keyword, index);
            if (index != -1) {
                count++;
                index += keyword.length();
            }
        }
        webScanResult.put(keyword, count);
    }

    public void createWebJobs(Document doc){
        Elements elements = doc.select("a[href]");

        phaser = new Phaser(1);
        phaserArrayList.add(phaser);
        for(Element element : elements){
            if(!isRunning){
                break;
            }
            String url = element.attr("abs:href");
            if(url.startsWith("http")) {
                if(visitedList.contains(url)) continue;
                visitedList.add(url);
                phaser.register();
                JobQueue.addJob(new WebJob(url, this.hops - 1, this));
            }
        }

        phaser.arriveAndAwaitAdvance();

        if(parentJob != null) {
            compileResult(false);
            parentJob.finishChildJob();
        }
        else compileResult(true);
    }

    private void compileResult(boolean complete){
        if(!isRunning) return;

        String domain = Web.getDomain(this.path);
        WebScanResult webScanResult_Object = new WebScanResult(
                webScanResult,
                complete
        );
        WebRetriever.addResult(domain, webScanResult_Object);
    }

    public void finishChildJob(){
        phaser.arriveAndDeregister();
    }

    public void stop(){
        isRunning = false;
        for(Phaser phaser1 : phaserArrayList){
            int parties = phaser1.getUnarrivedParties();
            for(int i = 0; i< parties; i++) phaser1.arriveAndDeregister();
        }
    }

    public static String getDomain(String path){
        String domain = "www.raf.edu.rs";
        try {
            URL parsedUrl = new URL(path);
            domain = parsedUrl.getHost();
        } catch (MalformedURLException e) {
            System.out.println("The URL is invalid.");
        }
        return domain;
    }
}
