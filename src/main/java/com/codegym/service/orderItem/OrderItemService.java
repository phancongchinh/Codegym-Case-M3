package com.codegym.service.orderItem;

import com.codegym.dao.orderItem.IOrderItemDao;
import com.codegym.dao.orderItem.OrderItemDao;
import com.codegym.model.OrderItem;

import java.util.List;

public class OrderItemService implements IOrderItemService {
    private IOrderItemDao orderItemDao = new OrderItemDao();

    @Override
    public List<OrderItem> getAll() {
        return null;
    }

    @Override
    public boolean save(OrderItem orderItem) {
        return orderItemDao.save(orderItem);
    }

    @Override
    public boolean update(int id, OrderItem orderItem) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public OrderItem findById(int id) {
        return null;
    }
}
