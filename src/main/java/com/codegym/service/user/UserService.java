package com.codegym.service.user;

import com.codegym.dao.user.IUserDao;
import com.codegym.dao.user.UserDao;
import com.codegym.model.User;

import java.util.List;

public class UserService implements IUserService {
    private IUserDao userDao = new UserDao();

    @Override
    public List<User> getAll() {

        return userDao.getAll();
    }

    @Override
    public boolean save(User user) {
        return userDao.save(user);
    }

    @Override
    public boolean update(int id, User user) {
        return userDao.update(id, user);
    }

    @Override
    public boolean delete(int id) {
        return userDao.delete(id);
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public int findIdByUser(User user) {
        return userDao.findIdByUser(user);
    }

    @Override
    public List<User> findByName(String name) {
        name="%"+name+"%";
      return userDao.findByName(name);
    }

}
