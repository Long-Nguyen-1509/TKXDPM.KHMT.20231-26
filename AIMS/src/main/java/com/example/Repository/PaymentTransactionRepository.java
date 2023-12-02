package com.example.Repository;

import com.example.Entity.PaymentTransaction;
import com.example.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class PaymentTransactionRepository {
    public Optional<PaymentTransaction> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(PaymentTransaction.class, id));
        }
    }

    public List<PaymentTransaction> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM PaymentTransaction", PaymentTransaction.class).list();
        }
    }

    public void save(PaymentTransaction paymentTransaction) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(paymentTransaction);
            transaction.commit();
        }
    }

    public void merge(PaymentTransaction paymentTransaction) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.merge(paymentTransaction);
            transaction.commit();
        }
    }

    public void delete(PaymentTransaction paymentTransaction) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.remove(paymentTransaction);
            transaction.commit();
        }
    }
}
