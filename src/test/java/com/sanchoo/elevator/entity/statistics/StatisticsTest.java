package com.sanchoo.elevator.entity.statistics;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class StatisticsTest {

    @Test
    void addStatisticsRecords_list_is_null() {
        Statistics statistics = Statistics.create();

        assertThrows(NullPointerException.class, () -> {
           statistics.addStatisticsRecords(null);
        });
    }

    @Test
    void addStatisticsRecords_success() {
        Statistics statistics = Statistics.create();
        List<StatisticsRecord> statisticsRecords = List.of(
                StatisticsRecord.of(1,2),
                StatisticsRecord.of(2,3)
        );

        statistics.addStatisticsRecords(statisticsRecords);
        List<StatisticsRecord> statisticsList = statistics.getStatisticsRecords();

        assertThat(statisticsList.containsAll(statisticsRecords), is(equalTo(true)));
    }

    @Test
    public void getRecord_incorrect_number_floor_from() {
        Statistics statistics = Statistics.create();

        assertThrows(IllegalArgumentException.class, () -> {
            statistics.getRecord(0, 2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            statistics.getRecord(-2, 3);
        });
    }

    @Test
    public void getRecord_incorrect_number_floor_to() {
        Statistics statistics = Statistics.create();

        assertThrows(IllegalArgumentException.class, () -> {
            statistics.getRecord(2, 0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            statistics.getRecord(3, -2);
        });
    }

    @Test
    public void getRecord_success() {
        Statistics statistics = Statistics.create();
        StatisticsRecord record1 =  StatisticsRecord.of(1,2);
        StatisticsRecord record2 = StatisticsRecord.of(2,3);
        List<StatisticsRecord> statisticsRecords = List.of(record1, record2);

        statistics.addStatisticsRecords(statisticsRecords);
        StatisticsRecord statisticsRecord1 = statistics.getRecord(1,2).get();
        StatisticsRecord statisticsRecord2 = statistics.getRecord(2,3).get();

        assertThat(statisticsRecord1, is(equalTo(record1)));
        assertThat(statisticsRecord2, is(equalTo(record2)));
    }
}