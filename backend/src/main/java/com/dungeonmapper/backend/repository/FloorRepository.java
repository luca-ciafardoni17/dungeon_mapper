package com.dungeonmapper.backend.repository;

import com.dungeonmapper.backend.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FloorRepository extends JpaRepository<Floor, Long> {

    @Query(value = "SELECT f FROM Floor f, Map m WHERE f.mapId.id = m.id AND m.id = :mid")
    List<Floor> findAllMapFloors(@Param("mid") Long mid);

    @Query(value = "SELECT f FROM Floor f, Map m WHERE f.mapId.id = :mid AND f.id = :fid")
    Floor findMapFloorById(@Param("mid") Long mid, @Param("fid") Long fid);
}
