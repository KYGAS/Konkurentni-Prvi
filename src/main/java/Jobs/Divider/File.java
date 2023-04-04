package Jobs.Divider;

import ResultRetriever.Result.FileScanResult;
import config.Config;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class File extends RecursiveTask<HashMap<String, Integer>>{
    private static int THRESHOLD = new Config().getFileScanningSizeLimit(); // maksimalan file size
    private final String text;
    private final List<String> keywords;

    public static volatile boolean isRunning = true;

    public File(String text, List<String> keywords) {
        this.text = text;
        this.keywords = keywords;
    }

    @Override
    protected HashMap<String, Integer> compute() {
        if(!isRunning) return null;
        if (text.length() <= THRESHOLD) {
            return countKeywords(text, keywords);
        } else {
            int splitIndex = text.length() / 2;
            File leftTask = new File(text.substring(0, splitIndex), keywords);
            File rightTask = new File(text.substring(splitIndex), keywords);
            if(!isRunning) return null;
                invokeAll(leftTask, rightTask);
            return FileScanResult.sumResults(leftTask.join(), rightTask.join());
        }
    }

    private HashMap<String, Integer> countKeywords(String text, List<String> keywords) {
        if(!isRunning) return null;
        HashMap<String, Integer> results = new HashMap<>();
        for (String keyword : keywords) {
            if(!isRunning) break;
            int count = 0;
            int index = text.indexOf(keyword);
            while (index != -1) {
                count++;
                index = text.indexOf(keyword, index + 1);
            }
            results.put(keyword, count);
        }
        if(!isRunning) return null;
        return results;
    }

    public void stop(){
        isRunning = false;
    }

}
