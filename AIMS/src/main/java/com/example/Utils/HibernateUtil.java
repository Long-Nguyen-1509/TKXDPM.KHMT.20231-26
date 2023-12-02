package com.example.Utils;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

@Getter
public class HibernateUtil {

    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize Hibernate SessionFactory: " + ex.getMessage(), ex);
        }
    }

    public static void shutdown() {
        getSessionFactory().close();
    }
}
