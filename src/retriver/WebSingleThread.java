package retriver;

import main.Main;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class WebSingleThread implements Callable<Map<String, Integer>> {

    private String domain;

    public WebSingleThread(String query) {
        this.domain = query;
    }

    @Override
    public Map<String, Integer> call() throws Exception {

        Map<String, Map<String, Integer>> toReturn = new ConcurrentHashMap<>();
        for (Map.Entry<String, Future<Map<String, Integer>>> entry: Main.resultRetriever.getWebResults().entrySet()){
            if(getDomainName(entry.getKey()).equals(domain)){
                if (toReturn.containsKey(getDomainName(entry.getKey()))){
                    toReturn.put(getDomainName(entry.getKey()), sumMaps(entry.getValue().get(), toReturn.get(getDomainName(entry.getKey()))));
                }else {
                    toReturn.put(getDomainName(entry.getKey()), entry.getValue().get());
                }
            }
        }
        return toReturn.get(domain);
    }

    public Map<String,Integer> sumMaps (Map<String,Integer> mapA,Map<String,Integer> mapB){

        for (Map.Entry<String,Integer> entry : mapB.entrySet()) {
            if(mapA.containsKey(entry.getKey())){
                Integer sum = mapA.get(entry.getKey()) + entry.getValue();
                mapA.put(entry.getKey(),sum);
            }else {
                mapA.put(entry.getKey(),entry.getValue());
            }
        }
        return mapA;


    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
