package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.elevator.Elevator;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.entity.floor.GroundFloor;
import com.sanchoo.elevator.entity.floor.LastFloor;
import com.sanchoo.elevator.service.BuildingService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class BuildingServiceImpl implements BuildingService {

    private BuildingServiceImpl() {
        log.debug("Created building service");
    }

    public static BuildingServiceImpl create() {
        log.debug("Creating building service");
        return new BuildingServiceImpl();
    }

    @Override
    public List<Floor> createdListOfFloor(int numberOfFloor) {
        checkArgument(numberOfFloor > 0, "number of floor less than or equal to 0");

        List<Floor> floorList = new ArrayList<>(numberOfFloor);

        Floor groundFloor = GroundFloor.create();
        floorList.add(groundFloor);

        IntStream.range(2, numberOfFloor)
                .mapToObj(Floor::new)
                .forEach(floorList::add);

        Floor lastFloor = LastFloor.of(numberOfFloor);
        floorList.add(lastFloor);

        return floorList;
    }

    @Override
    public List<Elevator> createdListOfElevator(int numberOfElevator, Floor startFloor) {
        checkArgument(numberOfElevator > 0, "number of elevator less than or equal to 0");
        checkNotNull(startFloor, "start floor is null");

        List<Elevator> elevatorList = IntStream.rangeClosed(1, numberOfElevator)
                .mapToObj(number -> Elevator.of(number, startFloor))
                .collect(Collectors.toList());

        return elevatorList;
    }
}
