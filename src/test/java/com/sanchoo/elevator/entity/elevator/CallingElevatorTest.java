package com.sanchoo.elevator.entity.elevator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class CallingElevatorTest {

    @Test
    public void of_incorrect_floor_number() {
        assertThrows(IllegalArgumentException.class, () -> {
            CallingElevator.of(0, Direction.UP);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           CallingElevator.of(-2, Direction.UP);
        });
    }

    @Test
    public void of_direction_is_null() {
        assertThrows(NullPointerException.class, () -> {
            CallingElevator.of(1, null);
        });
    }

    @Test
    public void of_direction_is_equal_to_STOP() {
        assertThrows(IllegalArgumentException.class, () -> {
           CallingElevator.of(1, Direction.STOP);
        });
    }

    @Test
    public void of_success() {
        CallingElevator callingElevator = CallingElevator.of(3, Direction.DOWN);

        assertThat(callingElevator, hasProperty("floor", is(equalTo(3))));
        assertThat(callingElevator, hasProperty("direction", is(equalTo(Direction.DOWN))));
    }
}