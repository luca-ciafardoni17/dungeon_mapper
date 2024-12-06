package com.dungeonmapper.backend.controller;

import com.dungeonmapper.backend.entity.Floor;
import com.dungeonmapper.backend.entity.Zone;
import com.dungeonmapper.backend.exceptions.DuplicateException;
import com.dungeonmapper.backend.exceptions.NotFoundException;
import com.dungeonmapper.backend.service.FloorService;
import com.dungeonmapper.backend.service.ZoneService;
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
public class ZoneController {

    @Autowired
    private FloorService floorService;

    @Autowired
    private ZoneService zoneService;

    @GetMapping(value = "/zone", produces = "application/json")
    public ResponseEntity<List<Zone>> apiGetAllZones() {
        List<Zone> foundZones = zoneService.getAllZones();
        return new ResponseEntity<List<Zone>>(foundZones, HttpStatus.FOUND);
    }

    @GetMapping(value = "/zone/{zid}", produces = "application/json")
    public ResponseEntity<Zone> apiGetZoneById(@PathVariable("zid") Long zid) throws NotFoundException {
        Zone foundZone = zoneService.getZoneById(zid);
        if (foundZone == null) {
            String errMsg = String.format("The zone with id %s was not found", zid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<Zone>(foundZone, HttpStatus.FOUND);
    }

    @PostMapping(value = "/floor/{fid}/zone", produces = "application/json")
    public ResponseEntity<Zone> apiCreateZone(@PathVariable("fid") Long fid, @RequestBody Zone zone) throws DuplicateException, NotFoundException{
        Floor floor = floorService.getFloorById(fid);
        if (floor == null) {
            String errMsg = String.format("The floor with id %s does not exist!", fid);
            throw new NotFoundException(errMsg);
        }
        String zoneName = zone.getName();
        List<Zone> foundZones = zoneService.getAllFloorZones(fid);
        if (foundZones.stream().anyMatch(mapMatch -> zoneName.equals(mapMatch.getName()))){
            String errMsg = String.format("A zone with name %s already exists!", zoneName);
            throw new DuplicateException(errMsg);
        }
        zone.setFloorId(floorService.getFloorById(fid));
        Zone newZone = zoneService.createZone(zone);
        return new ResponseEntity<Zone>(newZone, HttpStatus.CREATED);
    }

    @GetMapping(value = "/floor/{fid}/zone", produces = "application/json")
    public ResponseEntity<List<Zone>> apiGetAllFloorZones(@PathVariable("fid") Long fid) throws NotFoundException {
        Floor floor = floorService.getFloorById(fid);
        if (floor == null) {
            String errMsg = String.format("The floor with id %s does not exist!", fid);
            throw new NotFoundException(errMsg);
        }
        List<Zone> foundZones = zoneService.getAllFloorZones(fid);
        return new ResponseEntity<List<Zone>>(foundZones, HttpStatus.FOUND);
    }

    @GetMapping(value = "/floor/{fid}/zone/{zid}", produces = "application/json")
    public ResponseEntity<Zone> apiGetFloorZoneById(@PathVariable("fid") Long fid, @PathVariable("zid") Long zid) throws NotFoundException {
        Floor floor = floorService.getFloorById(fid);
        if (floor == null) {
            String errMsg = String.format("The floor with id %s does not exist!", fid);
            throw new NotFoundException(errMsg);
        }
        Zone foundZone = zoneService.getFloorZoneById(fid, zid);
        if (foundZone == null) {
            String errMsg = String.format("The zone with id %s of floor with id %s does not exist", zid, fid);
            throw new NotFoundException(errMsg);
        }
        return new ResponseEntity<Zone>(foundZone, HttpStatus.FOUND);
    }

    @PutMapping(value = "/floor/{fid}/zone/{zid}", produces = "application/json")
    public ResponseEntity<Zone> apiUpdateFloorZone(@PathVariable("fid") Long fid, @PathVariable("zid") Long zid, @RequestBody Zone zone) throws NotFoundException, DuplicateException {
        Floor floor = floorService.getFloorById(fid);
        if (floor == null) {
            String errMsg = String.format("The floor with id %s does not exist!", fid);
            throw new NotFoundException(errMsg);
        }
        Zone zoneToUpdate = zoneService.getFloorZoneById(fid, zid);
        if (zoneToUpdate == null) {
            String errMsg = String.format("The zone with id %s of floor with id %s does not exist", zid, fid);
            throw new NotFoundException(errMsg);
        }
        String zoneToUpdateName = zone.getName();
        List<Zone> foundZones = zoneService.getAllFloorZones(fid);
        if (foundZones.stream()
                .filter(zoneMatch -> !zoneMatch.getId().equals(zoneToUpdate.getId()))
                .anyMatch(zoneMatch -> zoneToUpdateName.equals(zoneMatch.getName()))){
            String errMsg = String.format("A zone with name %s already exists!", zoneToUpdateName);
            throw new DuplicateException(errMsg);
        }
        Hibernate.initialize(zoneToUpdate.getTreasures());        //Forcing Hibernate Lazy fetch
        zoneToUpdate.setName(zone.getName());
        zoneToUpdate.setDescription(zone.getDescription());
        zoneToUpdate.setDimensions(zone.getDimensions());
        zoneToUpdate.setGameInfo(zone.getGameInfo());
        zoneToUpdate.setTreasures(zone.getTreasures());
        zoneService.updateZone(zoneToUpdate);
        return new ResponseEntity<Zone>(zoneToUpdate, HttpStatus.OK);
    }

    @DeleteMapping(value = "/floor/{fid}/zone/{zid}", produces = "application/json")
    public ResponseEntity<Zone> apiDeleteUserMap(@PathVariable("fid") Long fid, @PathVariable("zid") Long zid) throws NotFoundException {
        Floor floor = floorService.getFloorById(fid);
        if (floor == null) {
            String errMsg = String.format("The floor with id %s does not exist!", fid);
            throw new NotFoundException(errMsg);
        }
        Zone zoneToDelete = zoneService.getFloorZoneById(fid, zid);
        if (zoneToDelete == null) {
            String errMsg = String.format("The zone with id %s of floor with id %s does not exist", zid, fid);
            throw new NotFoundException(errMsg);
        }
        Hibernate.initialize(zoneToDelete.getTreasures());      //Forcing Hibernate Lazy fetch
        zoneService.deleteZone(zoneToDelete.getId());
        return new ResponseEntity<Zone>(zoneToDelete, HttpStatus.OK);
    }

}
