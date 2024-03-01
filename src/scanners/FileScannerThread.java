package scanners;

import main.AppProperties;
import main.Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

public class FileScannerThread extends RecursiveTask<Map<String,Integer>> {

    private List<File> files;
    private Map<String, Integer> wordCountMap;

    private String corpusName;

//    public FileScannerThread(List<File> filesToScan, String corpusName) {
//        this.files = new ArrayList<>(filesToScan);
//        this.wordCountMap = new HashMap<>();
//        this.corpusName = corpusName;
//    }

    public FileScannerThread(List<File> filesToScan) {
        this.files = new ArrayList<>(filesToScan);
        this.wordCountMap = new HashMap<>();
    }




    @Override
    protected Map<String, Integer> compute() {

       // System.out.println("computing for" + files.toString());
        List<File> dividedFiles = divideFiles(files);


        if(files.size() > 0) {


            FileScannerThread left = new FileScannerThread(files);
            FileScannerThread right = new FileScannerThread(dividedFiles);
            left.fork();

            Map<String, Integer> rightResult = right.join();
            left.compute();

            wordCountMap.putAll(rightResult);

            return null;

        } else {
            scanFiles(dividedFiles);
           //Main.resultRetriever.addCorpusResult(corpusName, (Future<Map<String, Integer>>) wordCountMap);
            return wordCountMap;
        }


    }

    private void scanFiles(List<File> filesToScan) {

        for (File file : filesToScan) {

            Scanner myReader = null;
            try {
                myReader = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            while (myReader.hasNext()) {
                String word = myReader.next();
                word = word.replaceAll("[^a-zA-Z]", "");


                if (AppProperties.getInstance().getKeywords().contains(word)) {
                    if (wordCountMap.get(word) != null)
                        wordCountMap.put(word, wordCountMap.get(word) + 1);
                    else
                        wordCountMap.put(word, 1);
                }
            }
            myReader.close();

        }

    }

    private List<File> divideFiles(List<File> files) {

        int fileLengthSum = 0;
        List<File> dividedFiles = new ArrayList<>();

        for (File file : files) {
            fileLengthSum += file.length();

            dividedFiles.add(file);

            if (fileLengthSum > AppProperties.getInstance().getFile_scanning_size_limit()) {
                break;
            }
        }

        files.removeAll(dividedFiles);


        return dividedFiles;

    }

}