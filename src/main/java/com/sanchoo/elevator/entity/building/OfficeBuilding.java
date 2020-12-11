package com.sanchoo.elevator.entity.building;

import com.sanchoo.elevator.entity.elevator.Elevator;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.service.BuildingService;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.PersonService;
import com.sanchoo.elevator.service.implementation.BuildingServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.*;

@Slf4j
@Getter
public class OfficeBuilding {
    private final UUID id;
    private final List<Floor> floorList;
    private final List<Elevator> elevatorList;
    private FloorService floorService;
    private PersonService personService;
    private CallingElevatorService callingElevatorService;

    private OfficeBuilding(UUID id, int numberOfFloor, int numberOfElevator) {
        BuildingService buildingService = BuildingServiceImpl.create();
        this.id = id;
        this.floorList = List.copyOf(buildingService.createdListOfFloor(numberOfFloor));
        this.elevatorList = List.copyOf(buildingService.createdListOfElevator(numberOfElevator, floorList.get(0)));
        log.debug("Created office building '{}'", this.id);
    }

    public static OfficeBuilding of(int numberOfFloor, int numberOfElevator) {
        checkArgument(numberOfFloor > 1, "number of floors less than 2");
        checkArgument(numberOfElevator > 0, "number of elevators less than 1");
        checkArgument(numberOfElevator <= numberOfFloor, "number of elevators more than number of floors");

        UUID id = UUID.randomUUID();
        log.debug("Creating office building '{}'", id);
        return new OfficeBuilding(id, numberOfFloor, numberOfElevator);
    }

    public int getNumberOfFloor() {
        return floorList.size();
    }

    public int getNumberOfElevator() {
        return elevatorList.size();
    }

    public void setFloorService(FloorService floorService) {
        log.debug("Setting floor service for office building '{}'", this.id);
        checkNotNull(floorService, "floor service is null");
        checkState(floorService.isUseThisList(floorList), "floor service uses wrong floor list");

        this.floorService = floorService;
        log.debug("Set floor service for office '{}'", this.id);
    }

    public void setPersonService(PersonService personService) {
        log.debug("Setting person service for office building '{}'", this.id);
        checkNotNull(personService, "person service is null");

        this.personService = personService;
        log.debug("Set person service for office building '{}'", this.id);
    }

    public void setCallingElevatorService(CallingElevatorService callingElevatorService) {
        log.debug("Setting calling elevator service for office building '{}'", this.id);
        checkNotNull(callingElevatorService,"calling elevator service is null");

        this.callingElevatorService = callingElevatorService;
        log.debug("Set calling elevator service for office building '{}'", this.id);
    }

    public void startProcess() {
        log.info("Starting elevators and generation of people on the floors in office building '{}'", this.id);
        checkNotNull(this.floorService, "floor service is null");
        checkNotNull(this.personService, "person service is null");
        checkNotNull(this.callingElevatorService, "calling elevator service is null");

        log.debug("Setting services for each floor");
        floorList.forEach(floor -> {
            floor.setCallingElevatorService(callingElevatorService);
            floor.setPersonService(personService);
            floor.setFloorService(floorService);
        });
        log.debug("Set services for each floor");

        log.debug("Setting services for each elevator");
        elevatorList.forEach(elevator -> {
            elevator.setCallingElevatorService(callingElevatorService);
            elevator.setFloorService(floorService);
        });
        log.debug("Set services for each elevator");

        floorList.forEach(Thread::start);
        elevatorList.forEach(Thread::start);
        log.info("Elevators and generation of people on the floors in office building '{}' are started", this.id);
    }

    public void stopProcess() {
        log.info("Stopping elevators and generation of people on the floors in office building '{}'", this.id);
        floorList.forEach(Floor::stopGeneratePersons);
        elevatorList.forEach(Elevator::stopElevator);
        log.info("Elevators and generation of people on the floors in office building '{}' are stopped", this.id);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OfficeBuilding that = (OfficeBuilding) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringBuilder("OfficeBuilding{")
                .append("id=").append(id)
                .append(", floorList=").append(floorList)
                .append(", elevatorList=").append(elevatorList)
                .append("}").toString();
    }
}