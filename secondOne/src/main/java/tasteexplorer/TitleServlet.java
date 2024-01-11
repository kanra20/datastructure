// TitleServlet.java
package tasteexplorer;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@WebServlet(name = "TitleServlet", urlPatterns = {"/getTitle"})
public class TitleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            String url = request.getParameter("url");
            String title = getTitleFromUrl(url);
            out.print(title);
        }
    }

    private String getTitleFromUrl(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        } catch (Exception e) {
            e.printStackTrace();
            return "Title not available";
        }
    }
}
