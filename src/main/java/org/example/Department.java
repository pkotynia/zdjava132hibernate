package org.example;

import jakarta.persistence.*;

//Base on this annotation Hibernate will use Department object as DB entity
//this will allow for operations such as persist to be performed
@Entity
//this annotation allows Hibernate to create/map our class to DB table
@Table(name = "department")
public class Department {

    // this field will be a Primary Key
    @Id
    // Hibernate will handle key generation for us
    @GeneratedValue
    // explicitly setting column name, if not used column name = field name
    @Column(name = "department_id")
    private int departmentId;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    public Department() {}

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", departmentName='" + departmentName + '\'' +
                '}';
    }
}
