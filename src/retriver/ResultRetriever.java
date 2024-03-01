package retriver;

import main.Main;

import java.util.Map;
import java.util.concurrent.*;
public class ResultRetriever {

    private Future<Map<String, Map<String, Integer>>> webSummaryC;
    private Future<Map<String, Map<String, Integer>>> fileSummaryC;
    private Map<String, Future<Map<String, Integer>>> fileResults = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> webResults = new ConcurrentHashMap<>();
    private Map<String, Future<Map<String, Integer>>> webDomainResults = new ConcurrentHashMap<>();
    private ExecutorService service = Executors.newCachedThreadPool();

    public Map<String, Integer> getFileResult(String query) throws Exception{

        if(!fileResults.containsKey(query)){
            throw new Exception("Corpus not in jobs");
        }

        Future<Map<String, Integer>> res = fileResults.get(query);

        if(res != null){
            try {
                return res.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    public Map<String, Map<String, Integer>> getFileSummary() throws Exception {
        if(fileResults.isEmpty()){
            throw new Exception("no files to query");
        }
        if (fileSummaryC != null) {
            return fileSummaryC.get();
        } else {
            fileSummaryC = service.submit(new FileJobsSumThread());
            return fileSummaryC.get();
        }

    }

    public Map<String, Integer> queryFileResult(String query) throws Exception {
        if (!fileResults.containsKey(query)) {
            throw new Exception("Corpus not in jobs");
        }


        Future<Map<String, Integer>> res = fileResults.get(query);
        if(res != null){
            if(res.isDone()){

                try {
                    return res.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }else {
                throw new Exception("job not done yet");
            }
        }
        return null;
    }

    public Map<String, Map<String, Integer>> queryFileSummary() throws Exception {
        boolean done = true;
        if(fileResults.isEmpty()){
            throw new Exception("no files to query");
        }
        if (fileSummaryC != null && fileSummaryC.isDone()) {
            return fileSummaryC.get();
        }
        else{
            for(Map.Entry<String, Future<Map<String, Integer>>> entry: Main.resultRetriever.getFileResults().entrySet()){
                if(!entry.getValue().isDone())
                    done = false;
            }
            if(done) {
                fileSummaryC = service.submit(new FileJobsSumThread());
                return fileSummaryC.get();
            }
            else
                throw new Exception("job not finished");
        }

    }

    public Map<String, Map<String, Integer>> getWebSummary() throws Exception {
        if(webResults.isEmpty()){
            throw new Exception("no web pages to query");
        }
        if (webSummaryC != null) {
            return webSummaryC.get();
        } else {
            webSummaryC = service.submit(new WebJobsSumThread());
            return webSummaryC.get();
        }

    }

    public Map<String, Integer> getWebResult(String query) throws Exception{

        if(!webResults.containsKey(query)){
            Future<Map<String, Integer>> res = webDomainResults.get(query);

            if (res == null) {

                Future<Map<String, Integer>> domainRes = service.submit(new WebSingleThread(query));
                webDomainResults.put(query, domainRes);
                return domainRes.get();
            }else {

                return res.get();
            }


        }
        return null;
    }

    public Map<String, Integer> queryWebResult(String query) throws Exception {
        if (!webResults.containsKey(query)) {
            throw new Exception("Corpus not in jobs");
        }


        Future<Map<String, Integer>> res = webDomainResults.get(query);

        if (res == null) {

            Future<Map<String, Integer>> domainRes = service.submit(new WebSingleThread(query));

            webDomainResults.put(query, domainRes);
            throw new Exception("job not done yet");
        }else {
            if (res.isDone()){
                return res.get();
            }else {
                throw new Exception("job not done yet");
            }


        }
    }

    public Map<String, Map<String, Integer>> queryWebSummary() throws Exception {
        boolean done = true;

        if(webResults.isEmpty()){
            throw new Exception("no web pages to query");
        }
        if (webSummaryC != null && webSummaryC.isDone()) {
            return webSummaryC.get();
        }
        else{
            for(Map.Entry<String, Future<Map<String, Integer>>> entry: Main.resultRetriever.getWebResults().entrySet()){
                if(!entry.getValue().isDone())
                    done = false;
            }
            if(done) {
                fileSummaryC = service.submit(new WebJobsSumThread());
                return fileSummaryC.get();
            }
            else
                throw new Exception("job not finished");
        }

    }

    public void clearSummary(String query) {

        if(query.equals("cfs")){
            webSummaryC = null;
        }else if (query.equals("cws")){
            webSummaryC = null;
        }
        System.out.println("summaries cleared");

    }

    public void addCorpusResult(String corpusName, Future<Map<String, Integer>> corpusResult) {
        fileResults.put(corpusName,corpusResult);
       // fileSummaryC = service.submit(new FileJobsSumThread());

    }

    public void addWebResult(String url, Future<Map<String, Integer>> corpusResult) {

        webResults.put(url,corpusResult);

    }

    public Future<Map<String, Map<String, Integer>>> getWebSummaryC() {
        return webSummaryC;
    }

    public Future<Map<String, Map<String, Integer>>> getFileSummaryC() {
        return fileSummaryC;
    }

    public void setWebSummaryC(Future<Map<String, Map<String, Integer>>> webSummaryC) {
        this.webSummaryC = webSummaryC;
    }

    public void setFileSummaryC(Future<Map<String, Map<String, Integer>>> fileSummaryC) {
        this.fileSummaryC = fileSummaryC;
    }

    public void setFileResults(Map<String, Future<Map<String, Integer>>> fileResults) {
        this.fileResults = fileResults;
    }

    public void setWebResults(Map<String, Future<Map<String, Integer>>> webResults) {
        this.webResults = webResults;
    }

    public void setWebDomainResults(Map<String, Future<Map<String, Integer>>> webDomainResults) {
        this.webDomainResults = webDomainResults;
    }

    public Map<String, Future<Map<String, Integer>>> getFileResults() {
        return fileResults;
    }

    public Map<String, Future<Map<String, Integer>>> getWebResults() {
        return webResults;
    }

    public ExecutorService getService() {
        return service;
    }

    public Map<String, Future<Map<String, Integer>>> getWebDomainResults() {
        return webDomainResults;
    }
}
