package com.example.Repository;

import com.example.Entity.Role;
import com.example.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class RoleRepository {
    public Optional<Role> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Role.class, id));
        }
    }

    public List<Role> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Role", Role.class).list();
        }
    }

    public void save(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(role);
            transaction.commit();
        }
    }

    public void merge(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.merge(role);
            transaction.commit();
        }
    }

    public void delete(Role role) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.remove(role);
            transaction.commit();
        }
    }
}
