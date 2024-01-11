package tasteexplorer;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import javax.servlet.RequestDispatcher;

@WebServlet(name = "SearchServlet", urlPatterns = {"/Search"})

public class SearchServlet extends HttpServlet {

    // Serve the search form via a GET request. No processing logic here.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/SearchForm.jsp");
        dispatcher.forward(request, response);
    }

    // Process the search form submission via a POST request.
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        String city = request.getParameter("city");
        String cuisineType = request.getParameter("cuisineType");

        // Log or print the values of city and cuisineType
        System.out.println("City: " + city);
        System.out.println("Cuisine Type: " + cuisineType);

        try {
            // Validate inputs
            if (isInvalidInput(city) || isInvalidInput(cuisineType)) {
                request.setAttribute("error", "Invalid input: City and Cuisine Type must be provided and must not contain invalid characters.");
                request.getRequestDispatcher("/SearchForm.jsp").forward(request, response);
                return;
            }

            // Instantiate necessary classes
            KeywordCountClass keywordCounter = new KeywordCountClass();
            TreesClass treesClass = new TreesClass();
            SearchClass searchClass = new SearchClass();

            // Combine city and cuisine type in user preferences
            List<String> userPreferences = Arrays.asList(city, cuisineType);

            // Perform search
            List<Map.Entry<String, Integer>> results = searchClass.performSearch(city, cuisineType, userPreferences);

            System.out.println("Search Results: " + results);

            // Set the search results as a request attribute
            request.setAttribute("results", results);

            // Forward to the search results page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/SearchResults.jsp");
            dispatcher.forward(request, response);


        } catch (Exception e) {
            e.printStackTrace(); // Log the exception stack trace
            request.setAttribute("error", "An error occurred during the search: " + e.getMessage());
            request.getRequestDispatcher("/SearchForm.jsp").forward(request, response);
        }
    }

    // Helper method for input validation
    private boolean isInvalidInput(String input) {
        // Basic input validation
        if (input == null || input.trim().isEmpty()) {
            return true;
        }
        // Regex to check for unwanted characters, adjust regex as needed
        return !input.matches("[a-zA-Z0-9\\s]+");
    }
}
