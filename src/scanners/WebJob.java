package scanners;

import main.Main;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class WebJob implements Job {

    private String url;
    private Type type;
    private int hopCount;
    private Future<Map<String,Integer>> jobResult;
    private boolean stop;


    public WebJob(Type file,  boolean b, String name,int hopCount) {
        this.type = file;
        this.stop =  b;
        this.url = name;
        this.hopCount = hopCount;
    }


    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Future<Map<String, Integer>> initiate(RecursiveTask<?> task) {
        return null;
    }

    @Override
    public boolean isStop() {
        return stop;
    }


    public Future<Map<String, Integer>> initiateWeb(Callable webTask) {
        this.jobResult = Main.WebService.submit(webTask);
        return jobResult;
    }

    public String getUrl() {
        return url;
    }

    public int getHopCount() {
        return hopCount;
    }

    public Future<Map<String, Integer>> getJobResult() {
        return jobResult;
    }



    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setHopCount(int hopCount) {
        this.hopCount = hopCount;
    }

    public void setJobResult(Future<Map<String, Integer>> jobResult) {
        this.jobResult = jobResult;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }


}