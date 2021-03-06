package com.codegym.dao.user;

import com.codegym.dao.IGeneralDAO;
import com.codegym.model.User;

import java.util.List;

public interface IUserDao extends IGeneralDAO<User> {
    int findIdByUser(User user);
    List<User> findByName(String name);
}
