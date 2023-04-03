package ResultRetriever.Result;

import java.util.HashMap;

public class WebScanResult implements Result{
    public HashMap<String, Integer> result;
    public boolean complete;

    public WebScanResult(HashMap<String, Integer> result, boolean complete) {
        this.result = result;
        this.complete = complete;
    }

    public static WebScanResult sumResults(WebScanResult webScanResult1, WebScanResult webScanResult2){
        HashMap<String, Integer> result = new HashMap<>();

        for(String key : webScanResult1.result.keySet()){
            result.put(key, webScanResult1.result.get(key));
        }

        for(String key : webScanResult2.result.keySet()){
            if(result.containsKey(key)){
                result.put(key, webScanResult2.result.get(key) + result.get(key));
            }
            else result.put(key, webScanResult1.result.get(key));
        }

        return new WebScanResult(
                result,
                webScanResult1.complete || webScanResult2.complete
        );
    }

}
