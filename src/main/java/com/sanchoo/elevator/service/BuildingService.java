package com.sanchoo.elevator.service;

import com.sanchoo.elevator.entity.elevator.Elevator;
import com.sanchoo.elevator.entity.floor.Floor;

import java.util.List;

public interface BuildingService {
    List<Floor> createdListOfFloor(int numberOfFloor);
    List<Elevator> createdListOfElevator(int numberOfElevator, Floor startFloor);
}
