package retriver;

import main.Main;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class FileJobsSumThread implements Callable<Map<String, Map<String, Integer>>> {

    @Override
    public Map<String, Map<String, Integer>> call() throws Exception {

        Map<String, Map<String, Integer>> res = new ConcurrentHashMap<>();

        for (Map.Entry<String, Future<Map<String, Integer>>> entry: Main.resultRetriever.getFileResults().entrySet()){
            res.put(entry.getKey(),entry.getValue().get());
        }

        return res;
    }
}
