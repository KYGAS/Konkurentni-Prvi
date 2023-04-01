package Jobs.Scanners;

import config.Config;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Web implements Runnable{
    // TODO : Contain Web Job SCAN logic here
    String path;
    Integer hops;

    public Web(String path, Integer hops) {
        this.path = path;
        this.hops = hops;
    }

    @Override
    public void run() {
        String keyword = new Config().getKeywords()[0];

        try {

            Document doc = Jsoup.connect(path).get();
            String text = doc.body().text();
            int count = 0;
            int index = 0;

            while (index != -1) {
                index = text.indexOf(keyword, index);
                if (index != -1) {
                    count++;
                    index += keyword.length();
                }
            }

            System.out.println("The keyword \"" + keyword + "\" appears " + count + " times on the page.");
        } catch (IOException e) {

            e.printStackTrace();

        }

        System.out.println("Count : " + keyword);

    }
}
