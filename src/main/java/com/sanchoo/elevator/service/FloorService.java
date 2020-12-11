package com.sanchoo.elevator.service;

import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.floor.Floor;

import java.util.List;

public interface FloorService {
    boolean isUseThisList(List<Floor> floorList);
    boolean isContains(Floor floor);
    int getMaxNumberFloor();
    Floor nextFloor(Floor floor);
    Floor prevFloor(Floor floor);
    Floor move(Floor floor, Direction direction);
}
