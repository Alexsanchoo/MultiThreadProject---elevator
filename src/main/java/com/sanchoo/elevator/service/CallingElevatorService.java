package com.sanchoo.elevator.service;

import com.sanchoo.elevator.entity.elevator.CallingElevator;
import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.floor.Floor;

public interface CallingElevatorService {
    void putCalling(int floor, Direction direction);
    CallingElevator takeCalling();
    boolean removeCalling(int floor, Direction direction);
    CallingElevator isCallingExists(Floor floor);
}
