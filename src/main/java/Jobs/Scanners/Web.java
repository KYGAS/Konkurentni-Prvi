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

public class Web implements Runnable{
    String path;
    Integer hops;
    Web parentJob;
    Phaser phaser;
    HashMap<String, Integer> webScanResult;

    public static List<String> visitedList = new ArrayList<>();


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
        String[] keywords = new Config().getKeywords();
        Document doc = null;
        String text = null;

        try {
            doc = Jsoup.connect(path).get();
            text = doc.body().text();
        } catch (IOException  e) {
            if(parentJob != null) {
                parentJob.finishChildJob();
                return;
            }
        }

        for(String keyword : keywords){
            countWord(text, keyword);
        }

        if(hops > 0) createWebJobs(doc);
        else {
            if(parentJob != null) {
                compileResult(false);
                parentJob.finishChildJob();
            }
            else compileResult(true);
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
        for(Element element : elements){
            String url = element.attr("abs:href");
            if(url.startsWith("http")) {
                if(visitedList.contains(url)) continue;
                visitedList.add(url);
                System.out.println("Adding url : " + url);
                phaser.register();
                JobQueue.addJob(new WebJob(url, this.hops - 1, this));
            }
            System.out.println(phaser.getUnarrivedParties());
        }

        phaser.arriveAndAwaitAdvance();

        if(parentJob != null) {
            compileResult(false);
            parentJob.finishChildJob();
        }
        else compileResult(true);
        // All jobs on this domain have completed if parent == null;
    }

    private void compileResult(boolean complete){

        String domain = Web.getDomain(this.path);
        WebScanResult webScanResult_Object = new WebScanResult(
                webScanResult,
                complete
        );

        WebRetriever.addResult(domain, webScanResult_Object);

        if(complete){
            System.out.println("Web Job complete!");
            for(String keys : WebRetriever.results.keySet()){
                System.out.println(keys);
            }
            for(String keyword : new Config().getKeywords()) {
                System.out.println(keyword + "-" + WebRetriever.results.get(domain).result.get(keyword));
            }
        }
    }

    public void finishChildJob(){
        phaser.arriveAndDeregister();
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
