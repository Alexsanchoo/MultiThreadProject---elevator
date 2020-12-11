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

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class LastFloorTest {

    @Test
    public void of_incorrect_argument() {
        assertThrows(IllegalArgumentException.class, () -> {
            LastFloor.of(-2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           LastFloor.of(0);
        });
    }

    @Test
    public void of_success() {
        LastFloor lastFloor = LastFloor.of(3);

        assertThat(lastFloor, hasProperty("number", is(equalTo(3))));
    }

    @Test
    public void run_person_service_is_null() {
        Floor floor = LastFloor.of(2);
        FloorService floorService = FloorServiceImpl.of(List.of(GroundFloor.create(), floor));
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setFloorService(floorService);
        floor.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @Test
    public void run_floor_service_is_null() {
        Floor floor = LastFloor.of(2);
        PersonService personService = PersonServiceImpl.create();
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setPersonService(personService);
        floor.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @Test
    public void run_calling_elevator_service_is_null() {
        Floor floor = LastFloor.of(2);
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(List.of(GroundFloor.create(), floor));

        floor.setPersonService(personService);
        floor.setFloorService(floorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @SneakyThrows
    @Test
    public void run_success() {
        Floor floor = LastFloor.of(2);
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(List.of(GroundFloor.create(), floor));
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