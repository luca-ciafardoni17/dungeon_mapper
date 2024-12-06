package com.dungeonmapper.backend.controller;

import com.dungeonmapper.backend.entity.Floor;
import com.dungeonmapper.backend.entity.Map;
import com.dungeonmapper.backend.exceptions.DuplicateException;
import com.dungeonmapper.backend.exceptions.NotFoundException;
import com.dungeonmapper.backend.service.FloorService;
import com.dungeonmapper.backend.service.MapService;
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
public class FloorController {

    @Autowired
    private FloorService floorService;

    @Autowired
    private MapService mapService;

    @GetMapping(value = "/floor", produces = "application/json")
    public ResponseEntity<List<Floor>> apiGetAllFloors() {
        List<Floor> foundFloors = floorService.getAllFloors();
        return new ResponseEntity<List<Floor>>(foundFloors, HttpStatus.FOUND);
    }

    @GetMapping(value = "/floor/{fid}", produces = "application/json")
    public ResponseEntity<Floor> apiGetFloorById(@PathVariable("fid") Long fid) throws NotFoundException {
        Floor foundFloor = floorService.getFloorById(fid);
        if (foundFloor == null) {
            String errMsg = String.format("The floor with id %s does not exist", fid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<Floor>(foundFloor, HttpStatus.FOUND);
    }

    @PostMapping(value = "/map/{mid}/floor", produces = "application/json")
    public ResponseEntity<Floor> apiCreateFloor(@PathVariable("mid") Long mid, @RequestBody Floor floor) throws DuplicateException, NotFoundException {
        Map map = mapService.getMapById(mid);
        if (map == null) {
            String errMsg = String.format("The map with id %s does not exist!", mid);
            throw new NotFoundException(errMsg);
        }
        String floorName = floor.getName();
        List<Floor> foundFloors = floorService.getAllMapFloors(mid);
        if (foundFloors.stream().anyMatch(floorMatch -> floorName.equals(floorMatch.getName()))) {
            String errMsg = String.format("A floor with name %s in this map already exists!", floorName);
            throw new DuplicateException(errMsg);
        }
        floor.setMapId(mapService.getMapById(mid));
        Floor newFloor = floorService.createFloor(floor);
        return new ResponseEntity<Floor>(newFloor, HttpStatus.CREATED);
    }

    @GetMapping(value = "/map/{mid}/floor", produces = "application/json")
    public ResponseEntity<List<Floor>> apiGetAllMapFloors(@PathVariable("mid") Long mid) throws NotFoundException{
        Map map = mapService.getMapById(mid);
        if (map == null) {
            String errMsg = String.format("The map with id %s does not exist!", mid);
            throw new NotFoundException(errMsg);
        }
        List<Floor> foundFloor = floorService.getAllMapFloors(mid);
        return new ResponseEntity<List<Floor>>(foundFloor, HttpStatus.FOUND);
    }

    @GetMapping(value = "/map/{mid}/floor/{fid}", produces = "application/json")
    public ResponseEntity<Floor> apiGetMapFloorById(@PathVariable("mid") Long mid, @PathVariable("fid") Long fid) throws NotFoundException {
        Map map = mapService.getMapById(mid);
        if (map == null) {
            String errMsg = String.format("The map with id %s does not exist!", mid);
            throw new NotFoundException(errMsg);
        }
        Floor foundFloor = floorService.getMapFloorById(mid, fid);
        if (foundFloor == null) {
            String errMsg = String.format("The floor with id %s of map with id %s does not exist", fid, mid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<Floor>(foundFloor, HttpStatus.FOUND);
    }

    @PutMapping(value = "/map/{mid}/floor/{fid}", produces = "application/json")
    public ResponseEntity<Floor> apiUpdateMapFloor(@PathVariable("mid") Long mid, @PathVariable("fid") Long fid, @RequestBody Floor floor) throws NotFoundException, DuplicateException {
        Map map = mapService.getMapById(mid);
        if (map == null) {
            String errMsg = String.format("The map with id %s does not exist!", mid);
            throw new NotFoundException(errMsg);
        }
        Floor floorToUpdate = floorService.getFloorById(fid);
        if (floorToUpdate == null) {
            String errMsg = String.format("The floor with id %s of map with id %s does not exist!", fid, mid);
            throw new NotFoundException(errMsg);
        }
        String floorToUpdateName = floor.getName();
        List<Floor> foundFloors = floorService.getAllMapFloors(mid);
        if (foundFloors.stream()
                .filter(floorMatch -> !floorMatch.getId().equals(floorToUpdate.getId()))
                .anyMatch(floorMatch -> floorToUpdateName.equals(floorMatch.getName()))) {
            String errMsg = String.format("A floor with name %s already exists!", floorToUpdateName);
            throw new DuplicateException(errMsg);
        }
        Hibernate.initialize(floorToUpdate.getZones());
        Hibernate.initialize(floorToUpdate.getImageUrl());
        floorToUpdate.setName(floor.getName());
        floorToUpdate.setImageUrl(floor.getImageUrl());
        floorService.updateFloor(floorToUpdate);
        return new ResponseEntity<Floor>(floorToUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/map/{mid}/floor/{fid}", produces = "application/json")
    public ResponseEntity<Floor> apiDeleteMapFloor(@PathVariable("mid") Long mid, @PathVariable("fid") Long fid) throws NotFoundException {
        Map map = mapService.getMapById(mid);
        if (map == null) {
            String errMsg = String.format("The map with id %s does not exist!", mid);
            throw new NotFoundException(errMsg);
        }
        Floor floorToDelete = floorService.getMapFloorById(mid, fid);
        if (floorToDelete == null) {
            String errMsg = String.format("The floor with id %s does not exist!", fid);
            throw new NotFoundException(errMsg);
        }
        floorService.deleteFloor(floorToDelete.getId());
        return new ResponseEntity<Floor>(floorToDelete, HttpStatus.OK);
    }

}
