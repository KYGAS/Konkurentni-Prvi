package ResultRetriever.Retriever;

import ResultRetriever.Result.FileScanResult;
import ResultRetriever.Result.WebScanResult;

import java.util.concurrent.ConcurrentHashMap;

public class FileRetriever implements Retriever {

    public static ConcurrentHashMap<String, FileScanResult> results = null;

    public FileRetriever() {
        if(results == null) results = new ConcurrentHashMap<>();
    }

    public static void addResult(String corpus, FileScanResult result){
        if (results.containsKey(corpus)) {
            results.put(corpus,
                    FileScanResult.sumResults(
                            result,
                            results.get(
                                    corpus
                            )
                    )
            );
        } else {
            results.put(corpus, result);
        }
    }

}
