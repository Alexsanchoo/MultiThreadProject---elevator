package com.sanchoo.elevator.entity.statistics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class StatisticsRecordTest {

    @Test
    public void of_incorrect_number_floor_from() {
        assertThrows(IllegalArgumentException.class, () -> {
           StatisticsRecord.of(0, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           StatisticsRecord.of(-2, 3);
        });
    }

    @Test
    public void of_incorrect_number_floor_to() {
        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsRecord.of(2, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            StatisticsRecord.of(3, -2);
        });
    }

    @Test
    public void of_success() {
        StatisticsRecord statisticsRecord = StatisticsRecord.of(2, 3);

        assertThat(statisticsRecord, hasProperty("numberFloorFrom", is(equalTo(2))));
        assertThat(statisticsRecord, hasProperty("numberFloorTo", is(equalTo(3))));
    }

    @Test
    public void addNumberOfPersons_incorrect_argument() {
        StatisticsRecord statisticsRecord = StatisticsRecord.of(2, 3);

        assertThrows(IllegalArgumentException.class, () -> {
           statisticsRecord.addNumberOfPersons(-23);
        });
    }

    @Test
    public void addNumberOfPersons_success() {
        StatisticsRecord statisticsRecord = StatisticsRecord.of(2, 3);

        statisticsRecord.addNumberOfPersons(3);

        assertThat(statisticsRecord, hasProperty("numberOfPersonsMove", is(equalTo(3))));
    }
}