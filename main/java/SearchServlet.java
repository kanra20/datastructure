package com.tasteexplorer;
import com.tasteexplorer.WebPage; 
import com.tasteexplorer.CombinedSearch;
import com.tasteexplorer.SearchEngine;
import com.tasteexplorer.Main;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SearchServlet extends HttpServlet {

    private CombinedSearch combinedSearch;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize CombinedSearch here or via dependency injection
        combinedSearch = new CombinedSearch();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Extracting request parameters
        String city = request.getParameter("city");
        String cuisineType = request.getParameter("cuisineType");

        // Perform search
        List<WebPage> searchResults = combinedSearch.search(city, cuisineType, Arrays.asList("food", "restaurants"));

        // Set the search results as a request attribute
        request.setAttribute("searchResults", searchResults); // Attribute name is lowercase

        // Forward to JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/SearchResults.jsp"); // Presuming the JSP is at the webapp root
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Call doGet to handle POST requests the same way as GET requests
        doGet(request, response);
    }
}
