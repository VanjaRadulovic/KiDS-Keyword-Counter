package scanners;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public interface Job {

    Type getType();
    Future<Map<String, Integer>> initiate(RecursiveTask<?> task);
    boolean isStop();

}
