package ResultRetriever.Retriever;

import ResultRetriever.Result.WebScanResult;

import java.util.concurrent.ConcurrentHashMap;

public class WebRetriever implements Retriever{
    public static ConcurrentHashMap<String, WebScanResult> results;

    public WebRetriever() {
        results = new ConcurrentHashMap<>();
    }

    public static void addResult(String domain, WebScanResult result){
        if (results.containsKey(domain)) {
            results.put(domain,
                    WebScanResult.sumResults(
                            result,
                            results.get(domain)
                    )
            );
        } else {
            results.put(domain, result);
        }
    }

}
