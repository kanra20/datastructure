<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Taste Explorer</title>
    <!-- Link to your CSS file -->
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/styles.css">
   
</head>
<body>

<h2>Taste Explorer</h2>
<!-- The action should point to the URL pattern mapped to SearchServlet -->
<form action="${pageContext.request.contextPath}/Search" method="POST" class="middle-form">
    <label for="city"></label>
    <input type="text" id="city" name="city" placeholder="City" required>

    <label for="cuisineType"></label>
    <input type="text" id="cuisineType" name="cuisineType" placeholder="Cuisine type" required>

    <input type="submit" value="Search">
</form>

</body>
</html>
