package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private LocalDateTime createAt;
}
