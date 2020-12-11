package com.sanchoo.elevator.entity.statistics;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class Statistics {
    private final List<StatisticsRecord> statisticsRecords;

    private Statistics() {
        this.statisticsRecords = new ArrayList<>();
        log.debug("Created statistics");
    }

    public static Statistics create() {
        log.debug("Creating statistics");
        return new Statistics();
    }

    public void addStatisticsRecords(List<StatisticsRecord> records) {
        checkNotNull(records, "records is null");

        records.forEach(record -> {
            Optional<StatisticsRecord> recordFind = findRecordInList(record);

            if (recordFind.isPresent()) {
                recordFind.get().addNumberOfPersons(record.getNumberOfPersonsMove());
                log.info("Statistics: {}", recordFind);
            } else {
                statisticsRecords.add(record);
                log.info("Statistics: {}", record);
            }
        });
    }

    public Optional<StatisticsRecord> findRecordInList(StatisticsRecord record) {
        Optional<StatisticsRecord> recordFind = statisticsRecords.stream().filter(rec -> rec.equals(record))
                .findFirst();

        return recordFind;
    }

    public List<StatisticsRecord> getStatisticsRecords() {
        return List.copyOf(statisticsRecords);
    }

    public Optional<StatisticsRecord> getRecord(int numberFloorFrom, int numberFloorTo) {
        checkArgument(numberFloorFrom > 0, "number floor from less than or equal to 0");
        checkArgument(numberFloorTo > 0, "number floor to less than or equal to 0");

        Optional<StatisticsRecord> record = statisticsRecords.stream()
                .filter(rec -> rec.getNumberFloorTo() == numberFloorTo && rec.getNumberFloorFrom() == numberFloorFrom)
                .findFirst();

        return record;
    }
}
