package com.sanchoo.elevator;

import com.sanchoo.elevator.entity.building.OfficeBuilding;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.PersonService;
import com.sanchoo.elevator.service.implementation.CallingElevatorServiceImpl;
import com.sanchoo.elevator.service.implementation.FloorServiceImpl;
import com.sanchoo.elevator.service.implementation.PersonServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainApp {
    private static final int NUMBER_OF_FLOOR = 5;
    private static final int NUMBER_OF_ELEVATOR = 3;

    public static void main(String[] args) {
        OfficeBuilding officeBuilding = OfficeBuilding.of(NUMBER_OF_FLOOR, NUMBER_OF_ELEVATOR);

        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(officeBuilding.getFloorList());

        officeBuilding.setCallingElevatorService(callingElevatorService);
        officeBuilding.setFloorService(floorService);
        officeBuilding.setPersonService(personService);

        officeBuilding.startProcess();
    }
}
