package scanners;

import main.AppProperties;
import main.Main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class WebScannerThread implements Callable<Map<String,Integer>> {

    private WebJob job;

    public WebScannerThread(WebJob webJob) {
        this.job = webJob;
    }

    @Override
    public Map<String, Integer> call() throws Exception {

        Document doc;
        try {
            doc = Jsoup.connect(job.getUrl()).get();
        }catch (Exception e){
            System.out.println("Inaccessible url " + job.getUrl() + " -----");
            Main.resultRetriever.getWebResults().remove(job.getUrl());
            return null;
        }

        Elements links = doc.select("a[href]");

        List<String> urls = new ArrayList<>();

        for (Element link : links) {
            urls.add(link.attr("abs:href"));
        }

       // System.out.println(job.getHopCount());
        if(job.getHopCount() > 0){
            for (String url : urls) {
                if (url != null || !url.isEmpty() || !url.isBlank()){
                    try {
                        url = url.replaceAll(" ", "%20");
//                        synchronized (Main.xLock) {
                            WebJob webJob = new WebJob(Type.WEB, false, url, job.getHopCount() - 1);

                            if (!Main.cachedWebJobs.contains(webJob)) {
                                Main.cachedWebJobs.add(webJob);
                                Main.jobs.put(webJob);
                                //System.out.println(webJob.getHopCount());
                            }
//                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

        Main.scheduledWebService.schedule(new WebRemoveThread(job), AppProperties.getInstance().getUrl_refresh_time(), TimeUnit.MILLISECONDS);

        return countWords(job.getUrl());

    }

    private static Map<String, Integer>  countWords(String url)
    {
        Map<String,Integer> wordCountMap = new HashMap<>();
        Scanner myReader= null;
        try {
            myReader = new Scanner(Jsoup.connect(url).get().text());
        }  catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            assert myReader != null;
            if (!myReader.hasNext()) break;
            String word=myReader.next();
            word = word.replaceAll("[^a-zA-Z]", "").toLowerCase();
            if(AppProperties.getInstance().getKeywords().contains(word)){
                if (wordCountMap.get(word) != null)
                    wordCountMap.put(word, wordCountMap.get(word) + 1);
                else
                    wordCountMap.put(word, 1);
            }

        }
        myReader.close();
        return wordCountMap;
    }

}
