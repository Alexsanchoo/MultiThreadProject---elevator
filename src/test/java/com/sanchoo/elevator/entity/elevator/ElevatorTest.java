package com.sanchoo.elevator.entity.elevator;

import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.entity.floor.GroundFloor;
import com.sanchoo.elevator.entity.floor.LastFloor;
import com.sanchoo.elevator.entity.person.Person;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.implementation.CallingElevatorServiceImpl;
import com.sanchoo.elevator.service.implementation.FloorServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class ElevatorTest {

    @Test
    public void of_number_is_incorrect() {
        Floor floor = GroundFloor.create();

        assertThrows(IllegalArgumentException.class, () -> {
            Elevator.of(0, floor);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Elevator.of(-25, floor);
        });
    }

    @Test
    public void of_start_floor_is_null() {
        assertThrows(NullPointerException.class, () -> {
            Elevator.of(2, null);
        });
    }

    @Test
    public void of_success() {
        Floor floor = GroundFloor.create();

        Elevator elevator = Elevator.of(1, floor);

        assertThat(elevator.getName(), is(equalTo("elevator-" + 1)));
        assertThat(elevator, hasProperty("floor", is(equalTo(floor))));
    }

    @Test
    public void setFloor_floor_is_null() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);

        assertThrows(NullPointerException.class, () -> {
           elevator.setFloor(null);
        });
    }

    @Test
    public void setFloor_success() {
        Floor groundFloor = GroundFloor.create();
        Floor floor = new Floor(2);
        Elevator elevator = Elevator.of(1, groundFloor);

        elevator.setFloor(floor);

        assertThat(elevator, hasProperty("floor", is(equalTo(floor))));
    }

    @Test
    public void setCallingElevatorService_argument_is_null() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);

        assertThrows(NullPointerException.class, () -> {
           elevator.setCallingElevatorService(null);
        });
    }

    @Test
    public void setCallingElevatorService_success() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        elevator.setCallingElevatorService(callingElevatorService);
    }

    @Test
    public void setFloorService_argument_is_null() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);

        assertThrows(NullPointerException.class, () -> {
           elevator.setFloorService(null);
        });
    }

    @Test
    public void setFloorService_success() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        FloorService floorService = FloorServiceImpl.of(List.of(groundFloor, new Floor(2), LastFloor.of(3)));

        elevator.setFloorService(floorService);

        assertThat(elevator, hasProperty("floorService", is(equalTo(floorService))));
    }

    @Test
    public void landingPassengers_success() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        Person person1 = Person.of(100, 2);
        Person person2 = Person.of(80, 2);

        elevator.openDoors();
        elevator.addPassenger(person1);
        elevator.addPassenger(person2);
        elevator.setFloor(new Floor(2));
        elevator.landingPassengers();

        assertThat(elevator.hasPassengers(), is(false));
    }

    @Test
    public void addPassenger_person_is_null() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);

        elevator.openDoors();
        assertThrows(NullPointerException.class, () -> {
           elevator.addPassenger(null);
        });
    }

    @Test
    public void addPassenger_doors_not_opened() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        Person person = Person.of(43, 2);

        assertThrows(IllegalStateException.class, () -> {
           elevator.addPassenger(person);
        });
    }

    @Test
    public void addPassenger_success() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        Person person = Person.of(43, 2);

        elevator.openDoors();
        elevator.addPassenger(person);

        assertThat(elevator.hasPassengers(), is(true));
    }

    @Test
    public void run_calling_elevator_service_is_null() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        FloorService floorService = FloorServiceImpl.of(List.of(groundFloor, new Floor(2), LastFloor.of(3)));

        elevator.setFloorService(floorService);

        assertThrows(NullPointerException.class, elevator::run);
    }

    @Test
    public void run_floor_service_is_null() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        elevator.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, elevator::run);
    }

    @SneakyThrows
    @Test
    public void run_success() {
        Floor groundFloor = GroundFloor.create();
        Elevator elevator = Elevator.of(1, groundFloor);
        FloorService floorService = FloorServiceImpl.of(List.of(groundFloor, new Floor(2), LastFloor.of(3)));
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        elevator.setFloorService(floorService);
        elevator.setCallingElevatorService(callingElevatorService);

        elevator.start();
        TimeUnit.SECONDS.sleep(1);
        elevator.stopElevator();

        assertThat(elevator, hasProperty("floorService", is(equalTo(floorService))));
    }
}