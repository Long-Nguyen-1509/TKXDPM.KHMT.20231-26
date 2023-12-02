package com.example.Repository;

import com.example.Entity.DeliveryInfo;
import com.example.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class DeliveryInfoRepository {
    public Optional<DeliveryInfo> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(DeliveryInfo.class, id));
        }
    }

    public List<DeliveryInfo> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM DeliveryInfo", DeliveryInfo.class).list();
        }
    }

    public void save(DeliveryInfo deliveryInfo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(deliveryInfo);
            transaction.commit();
        }
    }

    public void merge(DeliveryInfo deliveryInfo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.merge(deliveryInfo);
            transaction.commit();
        }
    }

    public void delete(DeliveryInfo deliveryInfo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.remove(deliveryInfo);
            transaction.commit();
        }
    }
}
