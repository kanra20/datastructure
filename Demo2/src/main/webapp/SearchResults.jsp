<%@ page import="java.util.List" %>
<%@ page import="tasteexplorer.SearchServlet" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*" %>
<html>
<head>
    <title>Search Results</title>
    <!-- Link to your CSS file -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
    <!-- Link to Google Fonts for Roboto -->
 <!--    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">-->
</head>
<body>

<!-- Search form at the top of the results page -->
<form action="${pageContext.request.contextPath}/Search" method="POST" class="top-form">
    <label for="city">City:</label>
    <input type="text" id="city" name="city" required>

    <label for="cuisineType">Cuisine type:</label>
    <input type="text" id="cuisineType" name="cuisineType" required>

    <input type="submit" value="Search">
</form>

<h2>Search Results</h2>
<ul class="search-results">
    <%
    List<Map.Entry<String, Integer>> results = (List<Map.Entry<String, Integer>>) request.getAttribute("results");
    if (results != null && !results.isEmpty()) {
        for (Map.Entry<String, Integer> result : results) {
            String url = result.getKey();
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url; // Prepend with https:// if not present
            }
            out.println("<li><a href='" + url + "' target='_blank'>" + url + "</a></li>");
        }
    } else {
        out.println("<li>No results found</li>");
    }
%>
</ul>
<a href="${pageContext.request.contextPath}/Search">New Search</a>

</body>
</html>
