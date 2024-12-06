package com.dungeonmapper.backend.service;

import com.dungeonmapper.backend.entity.Zone;
import com.dungeonmapper.backend.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    public List<Zone> getAllZones() {
        return zoneRepository.findAll();
    }

    public Zone getZoneById(Long zid) {
        return zoneRepository.findById(zid).orElse(null);
    }

    public Zone createZone(Zone z) {
        return zoneRepository.save(z);
    }

    public List<Zone> getAllFloorZones(Long fid) {
        return zoneRepository.findAllFloorZones(fid);
    }

    public Zone getFloorZoneById(Long fid, Long zid) {
        return zoneRepository.findFloorZoneById(fid, zid);
    }

    public Zone updateZone(Zone updatedZone) {
        return zoneRepository.save(updatedZone);
    }

    public void deleteZone(Long zid) {
        zoneRepository.deleteById(zid);
    }

}
