package com.dungeonmapper.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Floor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonBackReference
    private Map mapId;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "floorId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Zone> zones;

}