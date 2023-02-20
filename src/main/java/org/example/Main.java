package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello hibernate!");

        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(Worker.class);
        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Department hr = new Department("HR");
        logger.info("HR before persist {}", hr );
        session.persist(hr);
        logger.info("HR after persist {}", hr );

        session.persist(new Department("Staffing"));

        Department it = new Department("IT");
        session.persist(it);

        Department department = session.get(Department.class, it.getDepartmentId());
        logger.info("Department in session {}", department);

        transaction.commit();
        session.close();



    }
}