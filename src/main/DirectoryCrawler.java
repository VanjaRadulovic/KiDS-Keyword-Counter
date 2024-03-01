package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import scanners.FileJob;
import scanners.Type;

import static main.Main.jobs;

public class DirectoryCrawler extends Thread{

    private CopyOnWriteArrayList<String> directories = new CopyOnWriteArrayList<>();
    private HashMap<String, Long> lastModifiedMap = new HashMap<>();
    private long timeout = Integer.parseInt(AppProperties.getInstance().getDir_crawler_sleep_time());
    private List<File> jobFiles = new CopyOnWriteArrayList<>();
    private boolean startJobFlag = false;

    public void stopCrawler(){
        this.directories.addIfAbsent("stop");
    }

    public void crawlDir(File[] files) {

        for(File file: files){
            if(file.isDirectory()){
                if(startJobFlag){
                    try {
                        List<File> filesForScan= new ArrayList<>();
                        filesForScan.addAll(jobFiles);
                        jobs.put(new FileJob(Type.FILE, filesForScan,false,jobFiles.get(0).getParentFile().getName()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                startJobFlag = false;
                jobFiles.clear();
                crawlDir(file.listFiles());
            }else if (!file.isDirectory() && file.getParentFile().getName().startsWith(AppProperties.getInstance().getPrefix())) {
                Long lm = lastModifiedMap.put(file.getAbsolutePath(), file.lastModified());
                jobFiles.add(file);

                if (lm == null || !lm.equals(file.lastModified())) {

                        if (Main.resultRetriever.getFileSummaryC() != null) {
                            Main.resultRetriever.setFileSummaryC(null);
                        }

                    startJobFlag = true;
                }


            }
        }

    }
    public void run() {
        while(true){

            for(String str : directories){
                if(str.equals("stop")){
                    System.out.println("Directory crawler stopped");
                    try {
                    jobs.put(new FileJob(Type.FILE,null,true,null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                        return;
                }

                File dir = new File(str);
                if (!dir.exists() || !dir.canRead()){
                    System.out.println("Can not open or find file with path " + str);
                    continue;
                }

                crawlDir(dir.listFiles());
            }

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    public CopyOnWriteArrayList<String> getDirectories() {
        return directories;
    }

}




