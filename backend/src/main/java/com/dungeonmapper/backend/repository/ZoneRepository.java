package com.dungeonmapper.backend.repository;

import com.dungeonmapper.backend.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

    @Query(value = "SELECT z FROM Zone z, Floor f WHERE z.floorId.id = f.id AND f.id = :fid")
    List<Zone> findAllFloorZones(@Param("fid") Long fid);

    @Query(value = "SELECT z FROM Zone z, Floor f WHERE z.floorId.id = :fid AND z.id = :zid")
    Zone findFloorZoneById(@Param("fid") Long fid, @Param("zid") Long zid);

}
