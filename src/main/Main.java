package main;

import retriver.ResultRetriever;
import scanners.*;

import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static BlockingQueue<Job> jobs = new LinkedBlockingQueue<>();
    public static ResultRetriever resultRetriever;
    public static JobDispatcher jobDispatcher;
    public static DirectoryCrawler directoryCrawler;
    public static ForkJoinPool fileScannerPool;
    public static List<WebJob> cachedWebJobs;
    public static ScheduledExecutorService scheduledWebService;
    public static ExecutorService WebService;

    public static  final Object xLock = new Object();

    public static void main(String[] args) throws Exception {



        directoryCrawler = new DirectoryCrawler();
        directoryCrawler.start();

        jobDispatcher = new JobDispatcher();
        jobDispatcher.start();

        fileScannerPool = new ForkJoinPool();

        resultRetriever = new ResultRetriever();

        scheduledWebService = Executors.newScheduledThreadPool(1);
        cachedWebJobs = new CopyOnWriteArrayList<>();
        WebService = Executors.newCachedThreadPool();

        Scanner bufferReader = new Scanner(System.in);

        while(true){
            String input = bufferReader.nextLine();
            String params = null;

            if(input.split(" ").length == 2)
            {
                params = input.split(" ")[1];
            }
            String command  = input.split(" ")[0];

            if(input.equals("stop"))
            {
                directoryCrawler.stopCrawler();
                break;
            }

            else if(command.equals("ad")) {
                if(params==null){
                    System.out.println("Please make sure you type in everything correctly (missing parameters or extra spaces");
                    continue;
                }

                try {
                    directoryCrawler.getDirectories().addIfAbsent(params);

                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }

            else if(command.equals("aw")){
                if(params==null){
                    System.out.println("Please make sure you type in everything correctly (missing parameters or extra spaces");
                    continue;
                }
                try {
                    WebJob job = new WebJob(Type.WEB,false,params,AppProperties.getInstance().getHop_count());
                    if (!cachedWebJobs.contains(job)){
                        jobs.put(job);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            else if(command.equals("get")){
                if(params==null){
                    System.out.println("Please make sure you type in everything correctly (missing parameters or extra spaces");
                    continue;
                }
                if(params.equals("web|summary")){
                    try{
                        Map<String, Map<String, Integer>> r = resultRetriever.getWebSummary();
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if(params.equals("file|summary")){
                    try{
                        Map<String, Map<String, Integer>> r = resultRetriever.getFileSummary();
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if(params.contains("file|")){
                    String file = params.split("\\|")[1];
                    try{
                        Map<String, Integer> r = resultRetriever.getFileResult(file);
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if(params.contains("web|")){
                    String file = params.split("\\|")[1];
                    try{
                        Map<String, Integer> r = resultRetriever.getWebResult(file);
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }

            else if(command.equals("query")){
                if(params==null){
                    System.out.println("Please make sure you type in everything correctly (missing parameters or extra spaces");
                    continue;
                }
                if(params.equals("web|summary")){
                    try{
                        Map<String, Map<String, Integer>> r = resultRetriever.queryWebSummary();
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if(params.equals("file|summary")){
                    try{
                        Map<String, Map<String, Integer>> r = resultRetriever.queryFileSummary();
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if(params.contains("file|")){
                    String file = params.split("\\|")[1];
                    try{
                        Map<String, Integer> r = resultRetriever.queryFileResult(file);
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                else if(params.contains("web|")){
                    String file = params.split("\\|")[1];
                    try{
                        Map<String, Integer> r = resultRetriever.queryWebResult(file);
                        if(r != null)
                            System.out.println(r.toString());
                        else
                            System.err.println("error");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }else if (command.equals("cfs")||command.equals("cws")) {
                resultRetriever.clearSummary(command);
            }else {
                System.out.println("Not a command");
            }
        }
        bufferReader.close();
    }
}