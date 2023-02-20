package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "worker")
public class Worker {

    @Id
    @GeneratedValue
    @Column(name = "worker_id")
    private int workerId;


}
