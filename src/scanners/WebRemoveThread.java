package scanners;

import main.Main;

import java.net.URI;
import java.net.URISyntaxException;

public class WebRemoveThread implements Runnable {
    private WebJob webJob;

    public WebRemoveThread(WebJob job){
        this.webJob = job;
    }

    @Override
    public void run() {
        Main.cachedWebJobs.remove(webJob);
        try {
            Main.resultRetriever.getWebDomainResults().remove(getDomainName(webJob.getUrl()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
