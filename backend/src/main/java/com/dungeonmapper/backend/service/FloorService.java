package com.dungeonmapper.backend.service;

import com.dungeonmapper.backend.entity.Floor;
import com.dungeonmapper.backend.repository.FloorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorService {

    @Autowired
    private FloorRepository floorRepository;

    public List<Floor> getAllFloors() {
        return floorRepository.findAll();
    }

    public Floor getFloorById(Long fid) {
        return floorRepository.findById(fid).orElse(null);
    }

    public Floor createFloor(Floor floor) {
        return floorRepository.save(floor);
    }

    public List<Floor> getAllMapFloors(Long mid) {
        return floorRepository.findAllMapFloors(mid);
    }

    public Floor getMapFloorById(Long mid, Long fid){
        return floorRepository.findMapFloorById(mid, fid);
    }

    public Floor updateFloor(Floor updatedFloor) {
        return floorRepository.save(updatedFloor);
    }

    public void deleteFloor(Long id) {
        floorRepository.deleteById(id);
    }

}
