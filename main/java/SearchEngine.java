package com.tasteexplorer;
import com.tasteexplorer.WebPage; 
import com.tasteexplorer.CombinedSearch;
import com.tasteexplorer.Main;
import com.tasteexplorer.SearchServlet;
import java.util.List;
//Import the WebPage class from the correct package
import com.tasteexplorer.WebPage; 
public interface SearchEngine {
    List<WebPage> search(String city, String cuisineType, List<String> additionalKeywords);
}