package com.codegym.controller.order;

import com.codegym.model.*;
import com.codegym.service.order.IOrderService;
import com.codegym.service.order.OrderService;
import com.codegym.service.orderItem.IOrderItemService;
import com.codegym.service.orderItem.OrderItemService;
import com.codegym.service.payment.IPaymentService;
import com.codegym.service.payment.PaymentService;
import com.codegym.service.shipment.IShipmentService;
import com.codegym.service.shipment.ShipmentService;
import com.codegym.service.status.IStatusService;
import com.codegym.service.status.StatusService;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderServlet", value = "/order")
public class OrderServlet extends HttpServlet {
    private IOrderService orderService = new OrderService();

    private IShipmentService shipmentService = new ShipmentService();

    private IPaymentService paymentService = new PaymentService();

    private IOrderItemService orderItemService = new OrderItemService();

    private IStatusService statusService = new StatusService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "create":
                showCreate(request, response);
                break;
            case "edit":
                showEdit(request, response);
                break;
            case "detail":
                showDetail(request, response);
                break;
            case "delete":
                showDelete(request, response);
                break;
            case "userList":
                showUserOrderList(request, response);
                break;
            case "customerEdit":
                showCustomerEdit(request, response);
                break;
            case "customerDelete":
                showCustomerDelete(request, response);
                break;
            default:
                showList(request, response);
                break;
        }

    }

    private void showCustomerDelete(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/customerDelete.jsp");
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.findById(orderId);
        request.setAttribute("order", order);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showCustomerEdit(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/customerEdit.jsp");
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        Order order = orderService.findById(orderId);
        request.setAttribute("order", order);
        List<Shipment> shipments = shipmentService.getAll();
        request.setAttribute("shipments", shipments);
        List<Payment> payments = paymentService.getAll();
        request.setAttribute("payments", payments);
        List<Status> statuses = statusService.getAll();
        request.setAttribute("statuses", statuses);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showUserOrderList(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        int userId = Integer.parseInt(session.getAttribute("userId").toString());
        List<Order> orders = orderService.getOrdersByUserId(userId);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/userList.jsp");
        request.setAttribute("orders", orders);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEdit(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/edit.jsp");
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderService.findById(id);
        request.setAttribute("order", order);
        List<Shipment> shipments = shipmentService.getAll();
        request.setAttribute("shipments", shipments);
        List<Payment> payments = paymentService.getAll();
        request.setAttribute("payments", payments);
        List<Status> statuses = statusService.getAll();
        request.setAttribute("statuses", statuses);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderService.findById(id);
        List<OrderItem> orderItems = orderItemService.getOrderItemById(id);
        double totalMoney = 0;
        for (OrderItem orderItem : orderItems) {
            totalMoney += orderItem.getPrice() * (1 - (double) orderItem.getPercentage() / 100) * orderItem.getQuantity();
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/detail.jsp");
        request.setAttribute("order", order);
        request.setAttribute("orderItems", orderItems);
        request.setAttribute("totalMoney", totalMoney);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showCreate(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/order/create.jsp");
        int userId = Integer.parseInt(request.getParameter("userId"));
        request.setAttribute("userId", userId);
        int productId = Integer.parseInt(request.getParameter("productId"));
        request.setAttribute("productId", productId);
        List<Payment> payments = paymentService.getAll();
        request.setAttribute("payments", payments);
        List<Shipment> shipments = shipmentService.getAll();
        request.setAttribute("shipments", shipments);
        try {
            requestDispatcher.forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    private void showDelete(HttpServletRequest request, HttpServletResponse response) {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/delete.jsp");
        int id = Integer.parseInt(request.getParameter("id"));
        Order order = orderService.findById(id);
        request.setAttribute("order", order);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response) {
        List<Order> orders = null;
        String name = request.getParameter("q");
        if (name == null) {
            orders = orderService.getAll();
        } else if (name.equals("")) {
            orders = null;
        } else {
            orders = orderService.findByName(name);
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/order/list.jsp");
        request.setAttribute("orders", orders);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
            case "create":
                createOrder(request, response);
                break;
            case "edit":
                editOrder(request, response);
                break;
            case "customerEdit":
                customerEditOrder(request, response);
                break;
            case "delete":
                deleteOrder(request, response);
                break;
            case "customerDelete":
                customerDeleteOrder(request, response);
                break;
        }
    }

    private void customerDeleteOrder(HttpServletRequest request, HttpServletResponse response) {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        orderService.delete(orderId);
        try {
            response.sendRedirect("/order?action=userList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void customerEditOrder(HttpServletRequest request, HttpServletResponse response) {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        int statusId = Integer.parseInt(request.getParameter("statusId"));
        Order order = new Order(orderId, paymentId, shipmentId, statusId);
        orderService.update(orderId, order);
        try {
            response.sendRedirect("/order?action=userList");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void editOrder(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        int statusId = Integer.parseInt(request.getParameter("statusId"));
        Order order = new Order(id, paymentId, shipmentId, statusId);
        orderService.update(id, order);
        try {
            response.sendRedirect("/order");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        int userId = Integer.parseInt(session.getAttribute("userId").toString());
        int paymentId = Integer.parseInt(request.getParameter("paymentId"));
        int shipmentId = Integer.parseInt(request.getParameter("shipmentId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        Order order = new Order(userId, paymentId, shipmentId);
        orderService.save(order);
        int orderId = orderService.getMaxId();
        OrderItem orderItem = new OrderItem(orderId, productId, quantity);
        orderItemService.save(orderItem);
        try {
            response.sendRedirect("/ecommerce");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrder(HttpServletRequest request, HttpServletResponse response) {
        int id = Integer.parseInt(request.getParameter("id"));
        orderService.delete(id);
        try {
            response.sendRedirect("/order");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
