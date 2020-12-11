package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.elevator.Elevator;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.entity.floor.GroundFloor;
import com.sanchoo.elevator.service.BuildingService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class BuildingServiceImplTest {

    @Test
    public void createdListOfFloor_incorrect_argument() {
        BuildingService buildingService = BuildingServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
            buildingService.createdListOfFloor(-2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            buildingService.createdListOfFloor(0);
        });
    }

    @Test
    public void createdListOfFloor_success() {
        BuildingService buildingService = BuildingServiceImpl.create();

        List<Floor> floorList = buildingService.createdListOfFloor(5);

        assertThat(floorList, (is(hasSize(5))));
    }

    @Test
    public void createdListOfElevator_incorrect_number_of_elevators() {
        BuildingService buildingService = BuildingServiceImpl.create();
        Floor floor = new Floor(2);

        assertThrows(IllegalArgumentException.class, () -> {
           buildingService.createdListOfElevator(0, floor);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           buildingService.createdListOfElevator(-1, floor);
        });
    }

    @Test
    public void createdListOfElevator_start_floor_is_null() {
        BuildingService buildingService = BuildingServiceImpl.create();

        assertThrows(NullPointerException.class, () -> {
            buildingService.createdListOfElevator(4, null);
        });
    }

    @Test
    public void createdListOfElevator_success() {
        BuildingService buildingService = BuildingServiceImpl.create();
        Floor floor = GroundFloor.create();

        List<Elevator> elevatorList = buildingService.createdListOfElevator(3, floor);

        assertThat(elevatorList, is(hasSize(3)));
    }
}