package main;

import main.Main;
import scanners.*;

import java.util.Map;
import java.util.concurrent.Future;

import static main.Main.jobs;

public class JobDispatcher extends Thread{

    @Override
    public void run() {

        while (true){
            try {

                Job job = jobs.take();

                if (job.isStop()){
                    System.out.println("-- Shutting down JobDispatcher");
                    System.out.println("-- Shutting down FileScanner pool");
                    Main.fileScannerPool.shutdown();
                    System.out.println("-- Shutting down resultRetriever pool");
                    Main.resultRetriever.getService().shutdown();
                    System.out.println("-- Shutting down webService");
                    Main.scheduledWebService.shutdownNow();
                    Main.WebService.shutdown();
                    return;
                }


                if(job.getType() == Type.FILE){

                    FileJob fileJob = (FileJob)job;
                    System.out.println("Starting file scan for: " + fileJob.getCorpusName());
                    Future<Map<String, Integer>> res = job.initiate(new FileScannerThread(fileJob.getFiles()));
                    Main.resultRetriever.addCorpusResult(((FileJob) job).getCorpusName(),res);
                }

                if (job.getType() == Type.WEB){

                    WebJob webJob = (WebJob)job;
                    System.out.println("Starting web scan for: " + webJob.getUrl());
                    Future<Map<String, Integer>> res = ((WebJob) job).initiateWeb(new WebScannerThread(webJob));
                    Main.resultRetriever.addWebResult(((WebJob) job).getUrl(),res);


                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }
}
