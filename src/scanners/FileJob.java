package scanners;

import main.Main;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class FileJob implements Job {

    private Type type;
    private List<File> files;
    private boolean stop;
    private String corpusName;
    private Future<Map<String,Integer>> jobResult;

    public FileJob(Type file, List<File> files, boolean stop, String name) {
        this.type = file;
        this.files = files;
        this.stop =  stop;
        this.corpusName = name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Future<Map<String, Integer>> initiate(RecursiveTask task) {
        this.jobResult =  Main.fileScannerPool.submit(task);
        return jobResult;
    }

    public List<File> getFiles() {
        return files;
    }

    public String getCorpusName() {
        return corpusName;
    }

    public Future<Map<String, Integer>> getJobResult() {
        return jobResult;
    }

    public boolean isStop() {
        return stop;
    }
}