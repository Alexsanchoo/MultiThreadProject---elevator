package com.sanchoo.elevator.entity.statistics;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

import static com.google.common.base.Preconditions.*;

@Slf4j
@Getter
public class StatisticsRecord {
    private final int numberFloorFrom;
    private final int numberFloorTo;
    private int numberOfPersonsMove;

    private StatisticsRecord(int numberFloorFrom, int numberFloorTo) {
        this.numberFloorFrom = numberFloorFrom;
        this.numberFloorTo = numberFloorTo;
        log.debug("Created statistics record");
    }

    public static StatisticsRecord of(int numberFloorFrom, int numberFloorTo) {
        log.debug("Creating statistics record");
        checkArgument(numberFloorFrom > 0, "number floor from less than or equal to 0");
        checkArgument(numberFloorTo > 0, "number floor to less than or equal to 0");

        return new StatisticsRecord(numberFloorFrom, numberFloorTo);
    }

    public void addNumberOfPersons(int count) {
        checkArgument(count >= 0, "count less than 0");

        this.numberOfPersonsMove += count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsRecord that = (StatisticsRecord) o;
        return numberFloorFrom == that.numberFloorFrom &&
                numberFloorTo == that.numberFloorTo;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberFloorFrom, numberFloorTo);
    }

    @Override
    public String toString() {
        return new StringBuilder("Statistics{")
                .append("numberFloorFor=").append(numberFloorFrom)
                .append(", numberFloorTo=").append(numberFloorTo)
                .append(", numberOfPersonsMove=").append(numberOfPersonsMove)
                .append("}").toString();
    }
}
