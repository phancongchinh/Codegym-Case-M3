package com.codegym.controller.product;

import com.codegym.model.*;
import com.codegym.service.brand.BrandService;
import com.codegym.service.brand.IBrandService;
import com.codegym.service.category.CategoryService;
import com.codegym.service.category.ICategoryService;
import com.codegym.service.discount.DiscountService;
import com.codegym.service.discount.IDiscountService;
import com.codegym.service.image.IImageService;
import com.codegym.service.image.ImageService;
import com.codegym.service.product.IProductService;
import com.codegym.service.product.ProductService;
import com.codegym.service.vendor.IVendorService;
import com.codegym.service.vendor.VendorService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ProductServlet", value = "/products")
public class ProductServlet extends HttpServlet {

    private static final IProductService PRODUCT_SERVICE = new ProductService();
    private static final IImageService IMAGE_SERVICE = new ImageService();
    private static final IBrandService BRAND_SERVICE = new BrandService();
    private static final ICategoryService CATEGORY_SERVICE = new CategoryService();
    private static final IVendorService VENDOR_SERVICE = new VendorService();
    private static final IDiscountService DISCOUNT_SERVICE = new DiscountService();

    private static final List<Brand> brands = BRAND_SERVICE.getAll();
    private static final List<Category> categories = CATEGORY_SERVICE.getAll();
    private static final List<Vendor> vendors = VENDOR_SERVICE.getAll();
    private static final List<Discount> discounts = DISCOUNT_SERVICE.getAll();

    public static final String ERROR_404_JSP = "error-404.jsp";
    public static final String PRODUCT_EDIT_JSP = "/product/edit.jsp";
    public static final String PRODUCT_VIEW_JSP = "/product/view.jsp";
    public static final String PRODUCT_DELETE_JSP = "/product/delete.jsp";
    public static final String PRODUCT_CREATE_JSP = "/product/create.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create": {
                showCreateFrom(request, response);
                break;
            }
            case "view": {
                showViewFrom(request, response);
                break;
            }
            case "edit": {
                showUpdateFrom(request, response);
                break;
            }
            case "delete": {
                showDeleteFrom(request, response);
                break;
            }
            default: {
                listAllProduct(request, response);
                break;
            }
        }
    }

    private void showViewFrom(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = PRODUCT_SERVICE.findById(id);
        List<Image> images = IMAGE_SERVICE.findImageByProductId(id);
        RequestDispatcher dispatcher;
        if (product == null) {
            dispatcher = request.getRequestDispatcher(ERROR_404_JSP);
        } else {
            dispatcher = request.getRequestDispatcher(PRODUCT_VIEW_JSP);
            request.setAttribute("product", product);
            request.setAttribute("images", images);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showUpdateFrom(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));


        Product product = PRODUCT_SERVICE.findById(id);
        RequestDispatcher dispatcher;
        if (product == null) {
            dispatcher = request.getRequestDispatcher(ERROR_404_JSP);
        } else {
            request.setAttribute("product", product);
            request.setAttribute("brands", brands);
            request.setAttribute("categories", categories);
            request.setAttribute("vendors", vendors);
            request.setAttribute("discounts", discounts);
            dispatcher = request.getRequestDispatcher(PRODUCT_EDIT_JSP);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreateFrom(HttpServletRequest request, HttpServletResponse response) {
        //localhost:8080?action=create
        RequestDispatcher dispatcher = request.getRequestDispatcher(PRODUCT_CREATE_JSP);
        try {
            request.setAttribute("brands", brands);
            request.setAttribute("categories", categories);
            request.setAttribute("vendors", vendors);
            request.setAttribute("discounts", discounts);
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showDeleteFrom(HttpServletRequest request, HttpServletResponse response) {
        //localhost:8080?action=delete&id=1
        int id = Integer.parseInt(request.getParameter("id"));
        Product oldProduct = PRODUCT_SERVICE.findById(id);
        RequestDispatcher dispatcher;
        if (oldProduct == null) {
            dispatcher = request.getRequestDispatcher("error-404.jsp");
        } else {
            dispatcher = request.getRequestDispatcher(PRODUCT_DELETE_JSP);
            request.setAttribute("product", oldProduct);
        }
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void listAllProduct(HttpServletRequest request, HttpServletResponse response) {
        String keyword = request.getParameter("q");
        List<Product> products;
        if (keyword == null || keyword.equals("")) {
            products = PRODUCT_SERVICE.getAll();
        } else {
            products = PRODUCT_SERVICE.findByName(keyword);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/product/list.jsp");
        request.setAttribute("products", products);
        try {
            dispatcher.forward(request, response);
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create": {
                creatProduct(request, response);
                break;
            }
            case "edit": {
                editProduct(request, response);
                break;
            }
            case "delete": {
                deleteProduct(request, response);
                break;
            }
        }
    }

    private void creatProduct(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        String SKU = request.getParameter("SKU");
        int brandId = Integer.parseInt(request.getParameter("brandId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int vendorId = Integer.parseInt(request.getParameter("vendorId"));
        int discountId = Integer.parseInt(request.getParameter("discountId"));
        Product product = new Product(name, description, price, SKU, brandId, categoryId, vendorId, discountId);
        boolean isSaved = PRODUCT_SERVICE.save(product);

//        String message = (isSaved) ? "Product created successfully!" : "Product created fail!";
//
//        request.setAttribute("message", message);
        try {
            response.sendRedirect("/products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void editProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        double price = Double.parseDouble(request.getParameter("price"));
        String SKU = request.getParameter("SKU");
        int brandId = Integer.parseInt(request.getParameter("brandId"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        int vendorId = Integer.parseInt(request.getParameter("vendorId"));
        int discountId = Integer.parseInt(request.getParameter("discountId"));
        Product product = new Product(name, description, price, SKU, brandId, categoryId, vendorId, discountId);
        product.setLastModifiedAt(LocalDate.now());
        boolean isUpdated = PRODUCT_SERVICE.update(id, product);

//        String message = (isUpdated) ? "Product updated successfully!" : "Product updated fail!";

//        request.setAttribute("message", message);
        try {
            response.sendRedirect("/products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean isDeleted = PRODUCT_SERVICE.delete(id);

//        String message = (isDeleted) ? "Product deleted successfully!" : "Product deleted fail!";

//        request.setAttribute("message", message);
        try {
            response.sendRedirect("/products");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}