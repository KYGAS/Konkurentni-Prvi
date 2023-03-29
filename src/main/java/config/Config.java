package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private final String FILENAME = "app.properties";
    private final String KEYWORDS_PROPERTY = "keywords";
    private final String FILE_CORPUS_PREFIX_PROPERTY = "file_corpus_prefix";
    private final String DIR_CRAWLER_SLEEP_TIME_PROPERTY = "dir_crawler_sleep_time";
    private final String FILE_SCANNING_SIZE_LIMIT_PROPERTY = "file_scanning_size_limit";
    private final String HOP_COUNT_PROPERTY = "hop_count";
    private final String URL_REFRESH_TIME_PROPERTY = "url_refresh_time";

    private static String[] keywords;
    private static String fileCorpusPrefix;
    private static long dirCrawlerSleepTime;
    private static int fileScanningSizeLimit;
    private static int hopCount;
    private static long urlRefreshTime;

    public void loadProperties() throws IOException, NullPointerException {
        Properties props = new Properties();
        InputStream input = Config.class.getClassLoader().getResourceAsStream(FILENAME);
        props.load(input);

        String keywordsStr = props.getProperty(KEYWORDS_PROPERTY);
        if (keywordsStr != null) {
            keywords = keywordsStr.split(",");
        }

        fileCorpusPrefix = props.getProperty(FILE_CORPUS_PREFIX_PROPERTY);

        String dirCrawlerSleepTimeStr = props.getProperty(DIR_CRAWLER_SLEEP_TIME_PROPERTY);
        if (dirCrawlerSleepTimeStr != null) {
            dirCrawlerSleepTime = Long.parseLong(dirCrawlerSleepTimeStr);
        }

        String fileScanningSizeLimitStr = props.getProperty(FILE_SCANNING_SIZE_LIMIT_PROPERTY);
        if (fileScanningSizeLimitStr != null) {
            fileScanningSizeLimit = Integer.parseInt(fileScanningSizeLimitStr);
        }

        String hopCountStr = props.getProperty(HOP_COUNT_PROPERTY);
        if (hopCountStr != null) {
            hopCount = Integer.parseInt(hopCountStr);
        }

        String urlRefreshTimeStr = props.getProperty(URL_REFRESH_TIME_PROPERTY);
        if (urlRefreshTimeStr != null) {
            urlRefreshTime = Long.parseLong(urlRefreshTimeStr);
        }

        input.close();
    }

    // getters for the loaded properties
    public String[] getKeywords() {
        return keywords;
    }

    public String getFileCorpusPrefix() {
        return fileCorpusPrefix;
    }

    public long getDirCrawlerSleepTime() {
        return dirCrawlerSleepTime;
    }

    public int getFileScanningSizeLimit() {
        return fileScanningSizeLimit;
    }

    public int getHopCount() {
        return hopCount;
    }

    public long getUrlRefreshTime() {
        return urlRefreshTime;
    }

    // For testing
    public static void main(String[] args) throws IOException {
        Config reader = new Config();
        reader.loadProperties();

        System.out.println("Keywords: " + String.join(", ", reader.getKeywords()));
        System.out.println("File corpus prefix: " + reader.getFileCorpusPrefix());
        System.out.println("Directory crawler sleep time: " + reader.getDirCrawlerSleepTime());
        System.out.println("File scanning size limit: " + reader.getFileScanningSizeLimit());
        System.out.println("Hop count: " + reader.getHopCount());
        System.out.println("URL refresh time: " + reader.getUrlRefreshTime());
    }

    public Config() {

    }
}
