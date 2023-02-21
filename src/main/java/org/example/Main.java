package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;

public class Main {

    //we need to declare static field type Logger form Simple Logging Facade for Java
    //in order to logg content. We are using static method from logger factory,
    // Main.class parameter is required to provide log information about origin class
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Hello hibernate!");

        //this is needed to configure Hibernate base on hibernate.properties
        //Hibernate will use jdbc as a DB connector
        Configuration configuration = new Configuration();
        //we need to add all annotated classes otherwise Hibernate will not be able to create tables or perform CRUD operations
        configuration.addAnnotatedClass(Department.class);
        configuration.addAnnotatedClass(Worker.class);
        ServiceRegistry serviceRegistry =
                new StandardServiceRegistryBuilder().build();

        //base on configuration we can create SessionFactory
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        /*SessionFactory creates session with allow us to perform operations on Entities such as
        - persist
        - get
        - remove
        - merge

        results of those operations will not be autmaticaly saved to DB.
        to save batch of operations to DB we need to perform commit on transaction
         */
        Session session = sessionFactory.openSession();

        //transaction allow us to group several operations
        Transaction transaction = session.beginTransaction();


        Department hr = new Department("HR");
        logger.info("HR before persist {}", hr );

        //persist method used to store Entity, persist operation will also modify hr object
        session.persist(hr);
        logger.info("HR after persist {}", hr );

        Department hrFromSession = session.get(Department.class, hr.getDepartmentId());
        logger.info("Hr from session {}", hrFromSession);

        session.persist(new Department("Staffing"));

        Department it = new Department("IT");
        //we do not have to save it separately due to usage of cascadeType.ALL option on OneToOne annotation in Worker class
        //session.persist(it);

        Department department = session.get(Department.class, it.getDepartmentId());
        logger.info("Department in session {}", department);

        Worker bob = new Worker();

        bob.setFirstName("Bob");
        bob.setLastName("Doe");
        bob.setHireDate(LocalDate.of(2018, Month.APRIL, 1));
        bob.setDepartment(it);

        //Persisting worker to session including the department
        session.persist(bob);

        WorkerDao workerDao = new WorkerDao(session);

        Worker ted = new Worker();

        ted.setFirstName("Ted");
        ted.setLastName("Doe");
        ted.setHireDate(LocalDate.of(2018, Month.APRIL, 1));
        ted.setDepartment(it);

        workerDao.create(ted);

        //this will initiate real (non resource-local) DB operations
        transaction.commit();
        //closing session as a cleanup
        session.close();

        //for the demo purposes second session and transactions are created
        Session secondSession = sessionFactory.openSession();
        Transaction secondTransaction = secondSession.beginTransaction();

        //bob object will be fetched form the DB and not form the session object
        // which was previously closed
        bob = secondSession.get(Worker.class, bob.getWorkerId());
        logger.info("Bob form DB {}", bob);

       // secondSession.remove(bob);



        secondTransaction.commit();
        secondSession.close();

    }
}