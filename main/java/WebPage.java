package com.tasteexplorer;
import com.tasteexplorer.Main; 
import com.tasteexplorer.CombinedSearch;
import com.tasteexplorer.SearchEngine;
import com.tasteexplorer.SearchServlet;
public class WebPage {
    private String title;
    private String url;
    private int ranking;
    private int score;

    public WebPage(String title, String url, int ranking, int score) {
        this.title = title;
        this.url = url;
        this.ranking = ranking;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public int getRanking() {
        return ranking;
    }

    @Override
    public String toString() {
        return "WebPage{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", ranking=" + ranking +
                '}';
    }

    public int getScore() {
        return score;
    }
}