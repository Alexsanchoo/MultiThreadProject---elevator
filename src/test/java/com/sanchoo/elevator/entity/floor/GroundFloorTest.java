package com.sanchoo.elevator.entity.floor;

import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.PersonService;
import com.sanchoo.elevator.service.implementation.CallingElevatorServiceImpl;
import com.sanchoo.elevator.service.implementation.FloorServiceImpl;
import com.sanchoo.elevator.service.implementation.PersonServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class GroundFloorTest {

    @Test
    public void run_person_service_is_null() {
        Floor floor = GroundFloor.create();
        FloorService floorService = FloorServiceImpl.of(List.of(floor, new Floor(2)));
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setFloorService(floorService);
        floor.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @Test
    public void run_floor_service_is_null() {
        Floor floor = GroundFloor.create();
        PersonService personService = PersonServiceImpl.create();
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setPersonService(personService);
        floor.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @Test
    public void run_calling_elevator_service_is_null() {
        Floor floor = GroundFloor.create();
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(List.of(floor, new Floor(2)));

        floor.setPersonService(personService);
        floor.setFloorService(floorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @SneakyThrows
    @Test
    public void run_success() {
        Floor floor = GroundFloor.create();
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(List.of(floor, new Floor(2)));
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setPersonService(personService);
        floor.setFloorService(floorService);
        floor.setCallingElevatorService(callingElevatorService);

        floor.start();
        TimeUnit.SECONDS.sleep(1);
        floor.stopGeneratePersons();

        assertThat(floor, hasProperty("floorService", is(equalTo(floorService))));
        assertThat(floor, hasProperty("personService", is(equalTo(personService))));
    }
}