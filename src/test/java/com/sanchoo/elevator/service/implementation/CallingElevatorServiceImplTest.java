package com.sanchoo.elevator.service.implementation;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.sanchoo.elevator.entity.elevator.CallingElevator;
import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.service.CallingElevatorService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class CallingElevatorServiceImplTest {

    @Test
    public void putCalling_incorrect_floor() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
           callingElevatorService.putCalling(0, Direction.UP);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           callingElevatorService.putCalling(-2, Direction.UP);
        });
    }

    @Test
    public void putCalling_direction_is_null() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(NullPointerException.class, () -> {
            callingElevatorService.putCalling(3, null);
        });
    }

    @Test
    public void putCalling_incorrect_direction() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
            callingElevatorService.putCalling(3, Direction.STOP);
        });
    }

    @Test
    public void putCalling_success() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        callingElevatorService.putCalling(2, Direction.DOWN);
        CallingElevator callingElevator = callingElevatorService.takeCalling();

        assertThat(callingElevator, hasProperty("floor", is(equalTo(2))));
        assertThat(callingElevator, hasProperty("direction", is(equalTo(Direction.DOWN))));
    }

    @Test
    public void removeCalling_incorrect_floor() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
            callingElevatorService.removeCalling(0, Direction.UP);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            callingElevatorService.removeCalling(-2, Direction.UP);
        });
    }

    @Test
    public void removeCalling_direction_is_null() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(NullPointerException.class, () -> {
            callingElevatorService.removeCalling(3, null);
        });
    }

    @Test
    public void removeCalling_incorrect_direction() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
            callingElevatorService.removeCalling(3, Direction.STOP);
        });
    }

    @Test
    public void removeCalling_success() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        callingElevatorService.putCalling(2, Direction.DOWN);
        boolean isDeleted = callingElevatorService.removeCalling(2, Direction.DOWN);

        assertThat(isDeleted, is(equalTo(true)));
    }

    @Test
    public void isCallingExists_argument_is_null() {
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        assertThrows(NullPointerException.class, () -> {
           callingElevatorService.isCallingExists(null);
        });
    }

    @Test
    public void isCallingExists_success() {
        Floor floor = new Floor(2);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        callingElevatorService.putCalling(floor.getNumber(), Direction.DOWN);
        CallingElevator callingElevator = callingElevatorService.isCallingExists(floor);

        assertThat(callingElevator, hasProperty("floor", is(equalTo(floor.getNumber()))));
        assertThat(callingElevator, hasProperty("direction", is(equalTo(Direction.DOWN))));
    }
}