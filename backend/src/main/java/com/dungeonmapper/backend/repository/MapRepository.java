package com.dungeonmapper.backend.repository;

import com.dungeonmapper.backend.entity.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapRepository extends JpaRepository<Map, Long> {

    @Query(value = "SELECT m FROM Map m, User u WHERE m.userId.id = u.id AND u.id = :uid")
    List<Map> findAllUserMaps(@Param("uid") Long uid);

    @Query(value = "SELECT m FROM Map m, User u WHERE m.userId.id = :uid AND m.id = :id")
    Map findUserMapById(@Param("uid") Long uid, @Param("id") Long id);

    @Query(value = "SELECT m FROM Map m, User u WHERE m.name LIKE :name AND m.userId.id = u.id AND u.id = :uid")
    List<Map> findUserMapsByName(@Param("name") String name, @Param("uid") Long uid);

    @Query(value = "SELECT m FROM Map m, User u WHERE :tag MEMBER OF m.tags AND m.userId.id = u.id AND u.id = :uid")
    List<Map> findUserMapsByTag(@Param("tag") String tag, @Param("uid") Long uid);

}
