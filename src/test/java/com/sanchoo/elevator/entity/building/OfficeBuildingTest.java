package com.sanchoo.elevator.entity.building;

import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.PersonService;
import com.sanchoo.elevator.service.implementation.CallingElevatorServiceImpl;
import com.sanchoo.elevator.service.implementation.FloorServiceImpl;
import com.sanchoo.elevator.service.implementation.PersonServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class OfficeBuildingTest {

    @Test
    public void of_incorrect_number_of_floors() {
        assertThrows(IllegalArgumentException.class, () -> {
            OfficeBuilding.of(1, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           OfficeBuilding.of(-3, 1);
        });
    }

    @Test
    public void of_incorrect_number_of_elevators() {
        assertThrows(IllegalArgumentException.class, () -> {
           OfficeBuilding.of(2, -2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            OfficeBuilding.of(2, 0);
        });


        assertThrows(IllegalArgumentException.class, () -> {
           OfficeBuilding.of(2, 3);
        });
    }

    @Test
    public void of_success() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);

        assertThat(officeBuilding, hasProperty("floorList", is(hasSize(5))));
        assertThat(officeBuilding, hasProperty("elevatorList", is(hasSize(3))));
    }

    @Test
    public void setFloorService_argument_is_null() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);

        assertThrows(NullPointerException.class, () -> {
            officeBuilding.setFloorService(null);
        });
    }

    @Test
    public void setFloorService_uses_wrong_floor_list() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(3, 1);
        List<Floor> floorList = List.of(new Floor(1), new Floor(2), new Floor(3));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(IllegalStateException.class, () -> {
            officeBuilding.setFloorService(floorService);
        });
    }

    @Test
    public void setFloorService_success() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);
        FloorService floorService = FloorServiceImpl.of(officeBuilding.getFloorList());

        officeBuilding.setFloorService(floorService);

        assertThat(officeBuilding, hasProperty("floorService", is(equalTo(floorService))));
    }

    @Test
    public void setPersonService_argument_is_null() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);

        assertThrows(NullPointerException.class, () -> {
            officeBuilding.setPersonService(null);
        });
    }

    @Test
    public void setPersonService_success() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);
        PersonService personService = PersonServiceImpl.create();

        officeBuilding.setPersonService(personService);

        assertThat(officeBuilding, hasProperty("personService", is(equalTo(personService))));
    }

    @Test
    public void setCallingElevatorService_argument_is_null() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);

        assertThrows(NullPointerException.class, () -> {
           officeBuilding.setCallingElevatorService(null);
        });
    }

    @Test
    public void setCallingElevatorService_success() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(5, 3);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        officeBuilding.setCallingElevatorService(callingElevatorService);

        assertThat(officeBuilding, hasProperty("callingElevatorService", is(equalTo(callingElevatorService))));
    }

    @Test
    public void startProcess_floorService_is_null() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(3, 2);
        PersonService personService = PersonServiceImpl.create();
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        officeBuilding.setPersonService(personService);
        officeBuilding.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, officeBuilding::startProcess);
    }

    @Test
    public void startProcess_personService_is_null() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(3, 2);
        FloorService floorService = FloorServiceImpl.of(officeBuilding.getFloorList());
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        officeBuilding.setFloorService(floorService);
        officeBuilding.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, officeBuilding::startProcess);
    }

    @Test
    public void startProcess_callingElevatorService_is_null() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(3, 2);
        FloorService floorService = FloorServiceImpl.of(officeBuilding.getFloorList());
        PersonService personService = PersonServiceImpl.create();

        officeBuilding.setFloorService(floorService);
        officeBuilding.setPersonService(personService);

        assertThrows(NullPointerException.class, officeBuilding::startProcess);
    }

    @Test
    public void startProcess_success() {
        OfficeBuilding officeBuilding = OfficeBuilding.of(3, 2);
        FloorService floorService = FloorServiceImpl.of(officeBuilding.getFloorList());
        PersonService personService = PersonServiceImpl.create();
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        officeBuilding.setFloorService(floorService);
        officeBuilding.setPersonService(personService);
        officeBuilding.setCallingElevatorService(callingElevatorService);

        assertThat(officeBuilding, hasProperty("floorService", is(equalTo(floorService))));
        assertThat(officeBuilding, hasProperty("personService", is(equalTo(personService))));
        assertThat(officeBuilding, hasProperty("callingElevatorService", is(equalTo(callingElevatorService))));
    }
}