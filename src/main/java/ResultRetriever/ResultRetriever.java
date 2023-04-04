package ResultRetriever;

import ResultRetriever.Gather.ResultGather;
import ResultRetriever.Result.Result;
import ResultRetriever.Retriever.FileRetriever;
import ResultRetriever.Retriever.WebRetriever;
import utils.Notifier;

import java.util.ArrayList;

public class ResultRetriever implements Runnable{

    public FileRetriever fileRetriever;
    public WebRetriever webRetriever;

    ArrayList<ResultGather> resultGatherArrayList;
    public volatile boolean isRunning = true;

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
        while(isRunning);
        Notifier.notifyObjectStopped(this);
    }

    public void stop(){
        isRunning = false;
        for(ResultGather resultGather : resultGatherArrayList) resultGather.stop();
    }
}
