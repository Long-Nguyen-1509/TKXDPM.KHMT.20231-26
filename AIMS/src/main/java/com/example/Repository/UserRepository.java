package com.example.Repository;

import com.example.Entity.User;
import com.example.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    public Optional<User> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(User.class, id));
        }
    }

    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM User", User.class).list();
        }
    }

    public void save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        }
    }

    public void merge(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();
        }
    }

    public void delete(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.remove(user);
            transaction.commit();
        }
    }
}
