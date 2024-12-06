package com.dungeonmapper.backend.service;

import com.dungeonmapper.backend.entity.Map;
import com.dungeonmapper.backend.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {

    @Autowired
    private MapRepository mapRepository;

    public List<Map> getAllMaps() { return mapRepository.findAll(); }

    public Map createMap(Map map) {
        return mapRepository.save(map);
    }

    public List<Map> getAllUserMaps(Long uid) {
        return mapRepository.findAllUserMaps(uid);
    }

    public Map getMapById(Long id) { return mapRepository.findById(id).orElse(null); }

    public Map getUserMapById(Long id, Long uid) {
        return mapRepository.findUserMapById(uid, id);
    }

    public List<Map> getUserMapsByName(String name, Long uid) {
        return mapRepository.findUserMapsByName(name, uid);
    }

    public List<Map> getUserMapsByTag(String tag, Long uid) {
        return mapRepository.findUserMapsByTag(tag, uid);
    }

    public Map updateMap(Map updatedMap) {
        return mapRepository.save(updatedMap);
    }

    public void deleteMap(Long id) {
        mapRepository.deleteById(id);
    }

}
