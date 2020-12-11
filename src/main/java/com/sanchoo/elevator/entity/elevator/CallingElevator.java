package com.sanchoo.elevator.entity.elevator;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.google.common.base.Preconditions.*;

@Slf4j
@Getter
public class CallingElevator {
    private final int floor;
    private final Direction direction;
    private boolean isCallProcessed;

    private CallingElevator(int floor, Direction direction) {
        this.floor = floor;
        this.direction = direction;
        log.debug("Created calling elevator");
    }

    public static CallingElevator of(int floor, Direction direction) {
        log.debug("Creating calling elevator");
        checkArgument(floor > 0, "floor less than or equal to 0");
        checkNotNull(direction, "direction is null");
        checkArgument(direction != Direction.STOP, "direction equal to STOP");

        return new CallingElevator(floor, direction);
    }

    public void setCallProcessed(boolean callProcessed) {
        log.debug("Setting status of call processed {}", callProcessed);
        isCallProcessed = callProcessed;
        log.debug("Set status of call processed {}", callProcessed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallingElevator that = (CallingElevator) o;
        return floor == that.floor &&
                direction == that.direction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(floor, direction);
    }

    @Override
    public String toString() {
        return new StringBuilder("CallingElevator{")
                .append("floor=").append(floor)
                .append(", mode=").append(direction)
                .append(", isCallProcessed=").append(isCallProcessed)
                .append("}").toString();
    }
}
