package ResultRetriever.Gather;

import ResultRetriever.Result.FileScanResult;
import ResultRetriever.Result.Result;
import ResultRetriever.Result.WebScanResult;
import ResultRetriever.ResultRetriever;
import ResultRetriever.Retriever.FileRetriever;
import ResultRetriever.Retriever.WebRetriever;

import java.util.HashMap;

public class ResultGather implements Runnable{

    String pool;
    String source;

    public static volatile boolean isRunning = true;
    public Result result;

    public ResultGather(String pool, String source) {
        this.pool = pool;
        this.source = source;
    }

    @Override
    public void run() {
        if(!isRunning) return;

        switch (pool) {
            case "web" -> {
                WebScanResult webScanResult = new WebScanResult(new HashMap<>(), false);

                if(source.equals("summary")){
                    if(ResultRetriever.checkHash(pool) != null){
                        System.out.println("Found cache!");
                        this.result = ResultRetriever.checkHash(pool);
                        ((WebScanResult) this.result).printObject();
                        return;
                    }
                    for(String key : WebRetriever.results.keySet()){
                        if(!isRunning) return;
                        webScanResult = WebScanResult.sumResults(webScanResult, WebRetriever.results.get(key));
                    }
                    this.result = webScanResult;
                    ResultRetriever.addHash("web", webScanResult);
                    webScanResult.printObject();
                }
                else if(WebRetriever.results.keySet().contains(source)){
                    while(!WebRetriever.results.get(source).complete);
                    this.result = WebRetriever.results.get(source);
                    WebRetriever.results.get(source).printObject();
                }
            }
            case "file" -> {
                FileScanResult fileScanResult = new FileScanResult(new HashMap<>(), false);
                if(source.equals("summary")){
                    if(ResultRetriever.checkHash(pool) != null){
                        System.out.println("Found cache!");
                        this.result = ResultRetriever.checkHash(pool);
                        ((FileScanResult) this.result).printObject();
                        return;
                    }
                    for(String key : FileRetriever.results.keySet()){
                        if(!isRunning) return;
                        fileScanResult = FileScanResult.sumResults(fileScanResult, FileRetriever.results.get(key));
                    }
                    this.result = fileScanResult;
                    ResultRetriever.addHash("web", fileScanResult);
                    fileScanResult.printObject();
                }
                else if(FileRetriever.results.keySet().contains(source)){
                    while(!FileRetriever.results.get(source).complete);
                    this.result = FileRetriever.results.get(source);
                    FileRetriever.results.get(source).printObject();
                }
            }
            default -> {
                System.out.println("Invalid pool.");
            }
        }
    }

    public Result gather(){
        if(!isRunning) return null;

        switch (pool) {
            case "web" -> {
                WebScanResult webScanResult = new WebScanResult(new HashMap<>(), false);

                if(source.equals("summary")){
                    if(ResultRetriever.checkHash(pool) != null){
                        System.out.println("Found cahce!");
                        return ResultRetriever.checkHash(pool);
                    }
                    for(String key : WebRetriever.results.keySet()){
                        if(!isRunning) return null;
                        webScanResult = WebScanResult.sumResults(webScanResult, WebRetriever.results.get(key));
                    }
                    this.result = webScanResult;
                    ResultRetriever.addHash("web", webScanResult);
                    webScanResult.printObject();
                }
                else if(WebRetriever.results.keySet().contains(source)){
                    while(!WebRetriever.results.get(source).complete);
                    this.result = WebRetriever.results.get(source);
                    WebRetriever.results.get(source).printObject();
                }
            }
            case "file" -> {
                FileScanResult fileScanResult = new FileScanResult(new HashMap<>(), false);
                if(source.equals("summary")){
                    if(ResultRetriever.checkHash(pool) != null){
                        System.out.println("Found cache!");
                        return ResultRetriever.checkHash(pool);
                    }
                    for(String key : FileRetriever.results.keySet()){
                        if(!isRunning) return null;
                        fileScanResult = FileScanResult.sumResults(fileScanResult, FileRetriever.results.get(key));
                    }
                    this.result = fileScanResult;
                    ResultRetriever.addHash("web", fileScanResult);
                    fileScanResult.printObject();
                }
                else if(FileRetriever.results.keySet().contains(source)){
                    while(!FileRetriever.results.get(source).complete);
                    this.result = FileRetriever.results.get(source);
                    FileRetriever.results.get(source).printObject();
                }
            }
            default -> {
                System.out.println("Invalid pool.");
            }
        }

        return this.result;
    }

    public void stop(){
        isRunning = false;
    }
}
