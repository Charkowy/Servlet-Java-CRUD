package charkoshop;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class CreateProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    public void init() throws ServletException {
        try {
            productDAO = new ProductDAO();
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        Product product = gson.fromJson(request.getReader(), Product.class);

        try {
            boolean success = productDAO.createProduct(product);
            JsonObject jsonResponse = new JsonObject();
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Product created successfully");
            } else {
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Failed to create product");
            }
            response.getWriter().write(jsonResponse.toString());
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}