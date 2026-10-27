package com.college.placement.entity;

import jakarta.persistence.*;

/**
 * Branch Entity: represents college academic branches (CSE, ECE, etc.).
 * Mapped to 'branches' table populated via Flyway migration V2.
 */
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false)
    private String name;

    public Branch() {}

    public Branch(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Branch(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

