package com.strategy.test.entity;

import jakarta.persistence.*;

@Entity
public class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(
            name = "seq_gen",
            sequenceName = "seq",
            allocationSize = 50
    )
    private Long id;
}
