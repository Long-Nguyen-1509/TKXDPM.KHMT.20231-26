package com.example.Repository;

import com.example.Entity.Invoice;
import com.example.Utils.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class InvoiceRepository {
    public Optional<Invoice> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Invoice.class, id));
        }
    }

    public List<Invoice> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Invoice", Invoice.class).list();
        }
    }

    public void save(Invoice invoice) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.persist(invoice);
            transaction.commit();
        }
    }

    public void merge(Invoice invoice) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.merge(invoice);
            transaction.commit();
        }
    }

    public void delete(Invoice invoice) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            var transaction = session.beginTransaction();
            session.remove(invoice);
            transaction.commit();
        }
    }
}
