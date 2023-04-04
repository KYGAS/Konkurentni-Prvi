package ResultRetriever.Result;

import java.util.HashMap;

public class FileScanResult implements Result {

    public HashMap<String, Integer> result;
    public boolean complete;

    public FileScanResult(HashMap<String, Integer> result, boolean complete) {
        this.result = result;
        this.complete = complete;
    }

    public static HashMap<String, Integer> sumResults(HashMap<String, Integer> leftResults, HashMap<String, Integer> rightResults) {
        HashMap<String, Integer> mergedResults = new HashMap<>(leftResults);
        for (HashMap.Entry<String, Integer> entry : rightResults.entrySet()) {
            String keyword = entry.getKey();
            int count = entry.getValue();
            if (mergedResults.containsKey(keyword)) {
                count += mergedResults.get(keyword);
            }
            mergedResults.put(keyword, count);
        }
        return mergedResults;
    }

    public static FileScanResult sumResults(FileScanResult leftResults, FileScanResult rightResults) {
        HashMap<String, Integer> mergedResults = new HashMap<>(leftResults.result);
        if(rightResults != null) {
            for (HashMap.Entry<String, Integer> entry : rightResults.result.entrySet()) {
                String keyword = entry.getKey();
                int count = entry.getValue();
                if (mergedResults.containsKey(keyword)) {
                    count += mergedResults.get(keyword);
                }
                mergedResults.put(keyword, count);
            }

            return new FileScanResult(
                    mergedResults,
                    leftResults.complete || rightResults.complete
            );
        }else return leftResults;
    }

    public void printObject(){
        System.out.println("{");
        for (String keyword : result.keySet()){
            System.out.println("\"" + keyword + "\" : " + result.get(keyword) + ",");
        }
        System.out.println("}");
    }
}
