package com.dungeonmapper.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @JsonBackReference
    private Floor floorId;

    @Column(nullable = false)
    private Long x;
    @Column(nullable = false)
    private Long y;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String dimensions;
    @Column(nullable = false)
    private String description;
    @Column(nullable = true)
    private String gameInfo;
    @Column(nullable = true)
    private List<String> treasures;

}
