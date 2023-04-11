package ResultRetriever;

import Jobs.Scanners.Web;
import ResultRetriever.Gather.ResultGather;
import ResultRetriever.Result.Result;
import ResultRetriever.Retriever.FileRetriever;
import ResultRetriever.Retriever.WebRetriever;
import config.Config;
import utils.Notifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResultRetriever implements Runnable{

    public FileRetriever fileRetriever;
    public WebRetriever webRetriever;

    ArrayList<ResultGather> resultGatherArrayList;
    public volatile boolean isRunning = true;

    public static Map<String, Result> cache = new HashMap<>();

    public static Result checkHash(String key){
        return cache.getOrDefault(key, null);
    }
    public static void addHash(String key, Result result){
        cache.put(key, result);
    }

    public ResultRetriever() {
        resultGatherArrayList = new ArrayList<>();
        fileRetriever = new FileRetriever();
        webRetriever = new WebRetriever();
    }

    public void fetch(String pool, String source){
        //non blocking

        ResultGather resultGatherer = new ResultGather(pool, source);
        Thread resultGathererThread = new Thread(resultGatherer);
        resultGatherArrayList.add(resultGatherer);
        resultGathererThread.start();

    }

    public Result get(String pool, String source){
        ResultGather resultGatherer = new ResultGather(pool, source);
        return resultGatherer.gather();
    }

    @Override
    public void run() {
        Long lastRefreshTime = new Date().getTime();
        while(isRunning){
            Long currentTime = new Date().getTime();
            if(Math.abs(currentTime - lastRefreshTime) > new Config().getUrlRefreshTime()){
                lastRefreshTime = currentTime;
                Web.visitedList.clear();
            }
        };
        Notifier.notifyObjectStopped(this);
    }

    public void stop(){
        isRunning = false;
        for(ResultGather resultGather : resultGatherArrayList) resultGather.stop();
    }
}
