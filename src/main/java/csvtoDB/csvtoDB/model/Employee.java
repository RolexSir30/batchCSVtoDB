package csvtoDB.csvtoDB.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "employee")
@Data
public class Employee implements Serializable {
    @Id
    private Integer id;
    private String name;
    private Integer age;
    private String city;
    private Double salary;
}
