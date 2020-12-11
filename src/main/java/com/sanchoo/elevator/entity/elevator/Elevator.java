package com.sanchoo.elevator.entity.elevator;

import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.entity.person.Person;
import com.sanchoo.elevator.entity.statistics.Statistics;
import com.sanchoo.elevator.entity.statistics.StatisticsRecord;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class Elevator extends Thread {
    public static final int MOVE_SPEED = 1000;
    public static final int DOOR_SPEED = 300;
    public static final int WEIGHT_CAPACITY = 300;

    @Getter
    private final UUID ID;
    private final Map<Integer, List<Person>> passengers;
    private final List<StatisticsRecord> waitingRecords;
    @Getter
    private final Statistics statistics;
    private CallingElevatorService callingElevatorService;
    @Getter
    private FloorService floorService;
    private CallingElevator call;
    @Getter
    private Floor floor;
    @Getter
    private Direction direction;
    @Getter
    private int weightNow;
    private boolean isStopped;
    @Getter
    private boolean isDoorsOpened;

    private Elevator(int number, Floor startFloor) {
        this.ID = UUID.randomUUID();
        this.passengers = new HashMap<>();
        this.waitingRecords = new ArrayList<>();
        this.statistics = Statistics.create();
        this.floor = startFloor;
        this.direction = Direction.STOP;
        setName("elevator-" + number);
        log.debug("Created elevator");
    }

    public static Elevator of(int number, Floor startFloor) {
        log.debug("Creating elevator");
        checkArgument(number > 0, "number less than or equal to 0");
        checkNotNull(startFloor, "start floor is null");

        return new Elevator(number, startFloor);
    }

    public void setFloor(Floor floor) {
        checkNotNull(floor, "floor is null");

        this.floor = floor;
    }

    public boolean hasPassengers() {
        long count = passengers.values().stream().mapToInt(List::size).filter(value -> value > 0).count();
        return count != 0;
    }

    public void setCallingElevatorService(CallingElevatorService callingElevatorService) {
        checkNotNull(callingElevatorService, "calling elevator service is null");

        this.callingElevatorService = callingElevatorService;
    }

    public void setFloorService(FloorService floorService) {
        checkNotNull(floorService, "floor service is null");

        this.floorService = floorService;
    }

    public void stopElevator() {
        this.isStopped = true;
    }

    @SneakyThrows
    public boolean openDoors() {
        if (!isDoorsOpened) {
            log.info("Opening doors");
            TimeUnit.MILLISECONDS.sleep(DOOR_SPEED);
            log.info("Opened doors");
            this.isDoorsOpened = true;
            return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean closeDoors() {
        if (isDoorsOpened) {
            log.info("Closing doors");
            TimeUnit.MILLISECONDS.sleep(DOOR_SPEED);
            log.info("Closed doors");
            this.isDoorsOpened = false;
            return true;
        }
        return false;
    }

    private void determiningDirection() {
        checkNotNull(call, "call is null");
        checkNotNull(floor, "floor is null");

        if (call.getFloor() > floor.getNumber()) {
            this.direction = Direction.UP;

        } else if (call.getFloor() < floor.getNumber()) {
            this.direction = Direction.DOWN;

        } else {
            this.direction = Direction.STOP;
        }
        log.info("Elevator set direction: {}", this.direction);
    }

    public void landingPassengers() {
        checkNotNull(floor, "floor is null");

        if (passengers.containsKey(floor.getNumber()) && passengers.get(floor.getNumber()).size() != 0) {
            openDoors();
            int weight = passengers.get(floor.getNumber()).stream().mapToInt(Person::getWeight).sum();
            log.info("Weight: {}", weightNow);
            log.info("Landing: {}", passengers.get(floor.getNumber()));
            passengers.get(floor.getNumber()).clear();
            weightNow -= weight;
            addWaitingRecordsToStatistics();
            log.info("Weight after landing: {}", weightNow);
        }
    }

    private void addWaitingRecordsToStatistics() {
        checkNotNull(floor, "floor is null");

        List<StatisticsRecord> recordList = waitingRecords.stream()
                .filter(rec -> rec.getNumberFloorTo() == floor.getNumber())
                .collect(Collectors.toList());

        statistics.addStatisticsRecords(recordList);
        waitingRecords.removeAll(recordList);
    }

    private void checkingCallCompletion() {
        checkNotNull(floor, "floor is null");
        checkNotNull(call, "call is null");

        if (floor.getNumber() == call.getFloor()) {
            openDoors();
            this.direction = call.getDirection();
            call.setCallProcessed(true);
            log.info("Call is processed {}", call);
        }
    }

    public void addPassenger(Person person) {
        checkNotNull(person, "person is null");
        checkState(isDoorsOpened, "doors not opened");

        if(!passengers.containsKey(person.getFloor())) {
            passengers.put(person.getFloor(), new ArrayList<>());
        }
        passengers.get(person.getFloor()).add(person);
        addStatisticsRecordsToWait(person);
    }

    private void addStatisticsRecordsToWait(Person person) {
        checkNotNull(person, "person is null");
        checkNotNull(floor, "floor is null");

        StatisticsRecord record = StatisticsRecord.of(floor.getNumber(), person.getFloor());
        record.addNumberOfPersons(1);

        Optional<StatisticsRecord> recordFind = waitingRecords.stream().filter(rec -> rec.equals(record))
                .findFirst();

        if(recordFind.isPresent()) {
            recordFind.get().addNumberOfPersons(1);
        } else {
            waitingRecords.add(record);
        }
    }

    private void takePersonsToMoveUp() {
        checkNotNull(callingElevatorService, "calling elevator service is null");
        checkNotNull(floor, "floor is null");
        checkNotNull(call, "call is null");

        if (callingElevatorService.removeCalling(floor.getNumber(), direction) || call.getFloor() == floor.getNumber()) {
            openDoors();
            floor.turnOffUpButton();
            boolean isPersonPlaced = true;
            while (isPersonPlaced) {
                Person person = floor.takePersonFromQueueUp(WEIGHT_CAPACITY - weightNow);
                if (person != null) {
                    addPassenger(person);
                    log.info("Added passenger from up queue: {}", person);
                    weightNow += person.getWeight();
                    log.info("Weight now: {}", weightNow);
                } else {
                    isPersonPlaced = false;
                }
            }
            floor.turnOnUpButton();
        }
    }

    private void takePersonsToMoveDown() {
        checkNotNull(callingElevatorService, "calling elevator service is null");
        checkNotNull(floor, "floor is null");
        checkNotNull(call, "call is null");

        if (callingElevatorService.removeCalling(floor.getNumber(), direction) || call.getFloor() == floor.getNumber()) {
            openDoors();
            floor.turnOffDownButton();
            boolean isPersonPlaced = true;
            while (isPersonPlaced) {
                Person person = floor.takePersonFromQueueDown(WEIGHT_CAPACITY - weightNow);
                if (person != null) {
                    addPassenger(person);
                    log.info("Added passenger from down queue: {}", person);
                    weightNow += person.getWeight();
                    log.info("Weight now: {}", weightNow);
                } else {
                    isPersonPlaced = false;
                }
            }
            floor.turnOnDownButton();
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        checkNotNull(callingElevatorService, "calling elevator service is null");
        checkNotNull(floorService, "floor service is null");

        while (!isStopped) {
            call = callingElevatorService.isCallingExists(floor);
            if(call == null) {
                closeDoors();
                log.info("Waiting call...");
                call = callingElevatorService.takeCalling();
            }

            determiningDirection();

            while (!call.isCallProcessed() || weightNow != 0) {
                checkState(!isDoorsOpened, "doors not closed");
                floor = floorService.move(floor, direction);

                landingPassengers();
                checkingCallCompletion();

                if (direction == call.getDirection()) {
                    switch (direction) {
                        case UP:
                           takePersonsToMoveUp();
                            break;

                        case DOWN:
                           takePersonsToMoveDown();
                            break;

                        case STOP:
                            break;
                    }
                    closeDoors();
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Elevator elevator = (Elevator) o;
        return Objects.equals(ID, elevator.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return new StringBuilder("Elevator{")
                .append("ID=").append(ID)
                .append(", passengers=").append(passengers)
                .append(", statistics=").append(statistics)
                .append(", floor=").append(floor)
                .append(", direction=").append(direction)
                .append(", weightNow=").append(weightNow)
                .append(", isDoorOpened=").append(isDoorsOpened)
                .append("}").toString();
    }
}