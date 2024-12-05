package com.dungeonmapper.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Floor floorId;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String dimensions;
    @Column(nullable = false)
    private String description;

}
