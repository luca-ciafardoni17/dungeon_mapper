package com.dungeonmapper.backend.controller;

import com.dungeonmapper.backend.entity.Map;
import com.dungeonmapper.backend.entity.User;
import com.dungeonmapper.backend.exceptions.DuplicateException;
import com.dungeonmapper.backend.exceptions.NotFoundException;
import com.dungeonmapper.backend.service.MapService;
import com.dungeonmapper.backend.service.UserService;
import lombok.extern.java.Log;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log
@RestController
@RequestMapping("api")
public class MapController {

    @Autowired
    private MapService mapService;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/user/{uid}/map", produces = "application/json")
    public ResponseEntity<Map> apiCreateMap(@PathVariable("uid") Long uid, @RequestBody Map map) throws DuplicateException{
        String mapName = map.getName();
        User user = userService.getUserById(uid);
        List<Map> foundMaps = mapService.getUserMapsByName(mapName, uid);
        if (foundMaps.stream().anyMatch(mapMatch -> mapName.equals(mapMatch.getName()))){
            String errMsg = String.format("A map with name %s already exists!", mapName);
            throw new DuplicateException(errMsg);
        }
        map.setUserId(userService.getUserById(uid));
        Map newMap = mapService.createMap(map);
        return new ResponseEntity<Map>(newMap, HttpStatus.CREATED);
    }

    @GetMapping(value = "/user/{uid}/map", produces = "application/json")
    public ResponseEntity<List<Map>> apiGetAllUserMaps(@PathVariable("uid") Long uid) {
        List<Map> foundMaps = mapService.getAllUserMaps(uid);
        return new ResponseEntity<List<Map>>(foundMaps, HttpStatus.FOUND);
    }

    @GetMapping(value = "/user/{uid}/map/{mid}", produces = "application/json")
    public ResponseEntity<Map> apiGetUserMapById(@PathVariable("uid") Long uid, @PathVariable("mid") Long mid) throws NotFoundException {
        Map foundMap = mapService.getUserMapById(mid, uid);
        if (foundMap == null) {
            String errMsg = String.format("The map with id %s of user with id %s does not exist", mid, uid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<Map>(foundMap, HttpStatus.FOUND);
    }

    @GetMapping(value = "/user/{uid}/map/name/{name}", produces = "application/json")
    public ResponseEntity<List<Map>> apiGetUserMapByName(@PathVariable("uid") Long uid, @RequestBody String name) throws NotFoundException {
        List<Map> foundMaps = mapService.getUserMapsByName(name, uid);
        if (foundMaps == null) {
            String errMsg = String.format("The map with name %s of user with %s does not exist", name, uid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<List<Map>>(foundMaps, HttpStatus.FOUND);
    }

    @GetMapping(value = "/user/{uid}/map/tag/{tag}", produces = "application/json")
    public ResponseEntity<List<Map>> apiGetUserMapByTag(@PathVariable("uid") Long uid, @RequestBody String tag) throws NotFoundException {
        List<Map> foundMaps = mapService.getUserMapsByTag(tag, uid);
        if (foundMaps == null) {
            String errMsg = String.format("The map with name %s of user with %s does not exist", tag, uid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<List<Map>>(foundMaps, HttpStatus.FOUND);
    }

    @PutMapping(value = "/user/{uid}/map/{mid}", produces = "application/json")
    public ResponseEntity<Map> apiUpdateMap(@PathVariable("uid") Long uid, @PathVariable("mid") Long mid, @RequestBody Map map) throws NotFoundException, DuplicateException {
        Map mapToUpdate = mapService.getUserMapById(mid, uid);
        if (mapToUpdate == null) {
            String errMsg = String.format("The map with id %s of user with id %s does not exist", mid, uid);
            throw new NotFoundException(errMsg);
        }
        Hibernate.initialize(mapToUpdate.getTags());        //Forcing Hibernate Lazy fetch
        Hibernate.initialize(mapToUpdate.getFloors());      //Forcing Hibernate Lazy fetch
        mapToUpdate.setName(map.getName());
        mapToUpdate.setTags(map.getTags());
        mapToUpdate.setDescription(map.getDescription());
        String mapToUpdateName = mapToUpdate.getName();
        List<Map> foundMaps = mapService.getUserMapsByName(mapToUpdateName, uid);
        if (foundMaps.stream().anyMatch(mapMatch -> mapToUpdateName.equals(mapMatch.getName()))){
            String errMsg = String.format("A map with name %s already exists!", mapToUpdateName);
            throw new DuplicateException(errMsg);
        }
        mapService.updateMap(mapToUpdate);
        return new ResponseEntity<Map>(mapToUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/user/{uid}/map/{mid}", produces = "application/json")
    public ResponseEntity<Map> apiDeleteMap(@PathVariable("uid") Long uid, @PathVariable("mid") Long mid) throws NotFoundException {
        Map mapToDelete = mapService.getUserMapById(mid, uid);
        if (mapToDelete == null) {
            String errMsg = String.format("The map with id %s of user with id %s does not exist", mid, uid);
            throw new NotFoundException(errMsg);
        }
        Hibernate.initialize(mapToDelete.getTags());        //Forcing Hibernate Lazy fetch
        Hibernate.initialize(mapToDelete.getFloors());      //Forcing Hibernate Lazy fetch
        this.mapService.deleteMap(mapToDelete.getId());
        return new ResponseEntity<Map>(mapToDelete, HttpStatus.OK);
    }

}