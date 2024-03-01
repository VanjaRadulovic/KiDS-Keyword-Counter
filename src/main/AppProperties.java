package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppProperties {

    private static AppProperties instance = null;
    private final Properties properties;
    private String prefix;
    private String dir_crawler_sleep_time;
    private List<String> keywords;
    private long file_scanning_size_limit;
    private int hop_count;
    private long url_refresh_time;

    private AppProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("application.properties"));

        } catch (IOException ioex) {
            Logger.getLogger(getClass().getName()).log(Level.ALL, "IOException Occured while loading properties file::::" +ioex.getMessage());
        }
        loadData();
    }

    public static AppProperties getInstance(){
        {
            if (instance == null)
                instance = new AppProperties();

            return instance;
        }
    }

    public String readProperty(String keyName) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Reading Property " + keyName +" = " +properties.getProperty(keyName));
        return properties.getProperty(keyName, "There is no key in the properties file");
    }

    public void loadData(){
        prefix = new String();
        dir_crawler_sleep_time = new String();
        prefix = readProperty("file_corpus_prefix");
        dir_crawler_sleep_time = readProperty("dir_crawler_sleep_time");
        String keyw = readProperty("keywords");
        keywords = new ArrayList<>(Arrays.asList(keyw.split(",")));
        file_scanning_size_limit = Long.parseLong(readProperty("file_scanning_size_limit"));
        hop_count = Integer.parseInt(readProperty("hop_count"));
        url_refresh_time = Long.parseLong(readProperty("url_refresh_time"));
    }

    public Properties getProperties() {
        return properties;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDir_crawler_sleep_time() {
        return dir_crawler_sleep_time;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public long getFile_scanning_size_limit() {
        return file_scanning_size_limit;
    }

    public int getHop_count() {
        return hop_count;
    }

    public long getUrl_refresh_time() {
        return url_refresh_time;
    }

}
