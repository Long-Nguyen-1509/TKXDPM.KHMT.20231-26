package com.example.Repository;

import com.example.Entity.Media;
import com.example.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class MediaRepository {
    public Optional<Media> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Media.class, id));
        }
    }

    public List<Media> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Media", Media.class).list();
        }
    }

    public void save(Media media) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(media);
            transaction.commit();
        }
    }

    public void merge(Media media) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.merge(media);
            transaction.commit();
        }
    }

    public void delete(Media media) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.remove(media);
            transaction.commit();
        }
    }
}
