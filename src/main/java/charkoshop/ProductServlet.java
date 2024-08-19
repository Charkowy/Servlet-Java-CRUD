package charkoshop;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com.google.gson.Gson;

public class ProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    public void init() throws ServletException {
        try {
            productDAO = new ProductDAO();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                List<Product> products = productDAO.getAllProducts();
                String json = gson.toJson(products);
                response.getWriter().write(json);
            } else {
                String[] pathParts = pathInfo.split("/");
                int id = Integer.parseInt(pathParts[1]);
                Product product = productDAO.getProductById(id);
                if (product != null) {
                    String json = gson.toJson(product);
                    response.getWriter().write(json);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("{\"error\":\"Product not found\"}");
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}