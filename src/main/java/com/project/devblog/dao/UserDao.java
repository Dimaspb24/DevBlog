package com.project.devblog.dao;

import com.project.devblog.model.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class UserDao {
    @Autowired
    private SessionFactory sessionFactory;

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public UserEntity findEntity(Integer id) {
        return getCurrentSession().find(UserEntity.class, id);
    }
    //
}
