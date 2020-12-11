package com.sanchoo.elevator.entity.floor;

import com.sanchoo.elevator.entity.person.Person;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.PersonService;
import com.sanchoo.elevator.service.implementation.CallingElevatorServiceImpl;
import com.sanchoo.elevator.service.implementation.FloorServiceImpl;
import com.sanchoo.elevator.service.implementation.PersonServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FloorTest {

    @Test
    public void constructor_incorrect_number() {
        assertThrows(IllegalArgumentException.class, () -> {
           new Floor(-1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Floor(0);
        });
    }

    @Test
    public void constructor_success() {
        Floor floor = new Floor(3);

        assertThat(floor, hasProperty("number", is(equalTo(3))));
    }

    @Test
    public void setPersonService_argument_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, () -> {
           floor.setPersonService(null);
        });
    }

    @Test
    public void setPersonService_success() {
        Floor floor = new Floor(2);
        PersonService personService = PersonServiceImpl.create();

        floor.setPersonService(personService);

        assertThat(floor, hasProperty("personService", is(equalTo(personService))));
    }

    @Test
    public void setCallingElevatorService_argument_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, () -> {
            floor.setCallingElevatorService(null);
        });
    }

    @Test
    public void setCallingElevatorService_success() {
        Floor floor = new Floor(2);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setCallingElevatorService(callingElevatorService);
    }

    @Test
    public void setFloorService_argument_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, () -> {
            floor.setFloorService(null);
        });
    }

    @Test
    public void setFloorService_floor_list_does_not_contain_floor() {
        Floor floor = new Floor(2);
        List<Floor> floorList = List.of(new Floor(1), new Floor(2), new Floor(3));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(IllegalArgumentException.class, () -> {
           floor.setFloorService(floorService);
        });
    }

    @Test
    public void setFloorService_success() {
        Floor floor = new Floor(2);
        List<Floor> floorList = List.of(new Floor(1), floor, new Floor(3));
        FloorService floorService = FloorServiceImpl.of(floorList);

        floor.setFloorService(floorService);

        assertThat(floor, hasProperty("floorService", is(equalTo(floorService))));
    }

    @Test
    public void turnOnUpButton_calling_elevator_service_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, floor::turnOnUpButton);
    }

    @Test
    public void turnOnUpButton_success() {
        Floor floor = new Floor(2);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setCallingElevatorService(callingElevatorService);
        boolean isTurnOnUpButton = floor.turnOnUpButton();

        assertThat(isTurnOnUpButton, is(equalTo(false)));
    }

    @Test
    public void turnOnDownButton_calling_elevator_service_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, floor::turnOnDownButton);
    }

    @Test
    public void turnOnDownButton_success() {
        Floor floor = new Floor(2);
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setCallingElevatorService(callingElevatorService);
        boolean isTurnOnDownButton = floor.turnOnDownButton();

        assertThat(isTurnOnDownButton, is(equalTo(false)));
    }

    @Test
    public void putPersonToQueueUp_argument_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, () -> {
           floor.putPersonToQueueUp(null);
        });
    }

    @Test
    public void putPersonToQueueUp_incorrect_argument() {
        Floor floor = new Floor(2);
        Person person = Person.of(100, 1);
        Person person2 = Person.of(100, 2);

        assertThrows(IllegalArgumentException.class, () -> {
           floor.putPersonToQueueUp(person);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           floor.putPersonToQueueUp(person2);
        });
    }

    @Test
    public void putPersonToQueueUp_success() {
        Floor floor = new Floor(2);
        Person person = Person.of(100, 3);

        floor.putPersonToQueueUp(person);
        List<Person> queueUp = floor.getQueueUp();

        assertThat(queueUp.contains(person), is(equalTo(true)));
    }

    @Test
    public void putPersonToQueueDown_argument_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, () -> {
            floor.putPersonToQueueDown(null);
        });
    }

    @Test
    public void putPersonToQueueDown_incorrect_argument() {
        Floor floor = new Floor(2);
        Person person = Person.of(100, 3);
        Person person2 = Person.of(100, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            floor.putPersonToQueueDown(person);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            floor.putPersonToQueueDown(person2);
        });
    }

    @Test
    public void putPersonToQueueDown_success() {
        Floor floor = new Floor(2);
        Person person = Person.of(100, 1);

        floor.putPersonToQueueDown(person);
        List<Person> queueDown = floor.getQueueDown();

        assertThat(queueDown.contains(person), is(equalTo(true)));
    }

    @Test
    public void putPersonsToQueue_argument_is_null() {
        Floor floor = new Floor(2);

        assertThrows(NullPointerException.class, () -> {
            floor.putPersonsToQueueUp(null);
        });
    }

    @Test
    public void putPersonsToQueueUp_success() {
        Floor floor = new Floor(2);

        List<Person> personList = List.of(Person.of(23, 3), Person.of(100, 4));
        floor.putPersonsToQueueUp(personList);
        List<Person> queueUp = floor.getQueueUp();

        assertThat(queueUp.containsAll(personList), is(equalTo(true)));
    }

    @Test
    public void putPersonsToQueueDown_argument_is_null() {
        Floor floor = new Floor(3);

        assertThrows(NullPointerException.class, () -> {
            floor.putPersonsToQueueDown(null);
        });
    }

    @Test
    public void putPersonsToQueueDown_success() {
        Floor floor = new Floor(3);

        List<Person> personList = List.of(Person.of(23, 2), Person.of(100, 1));
        floor.putPersonsToQueueDown(personList);
        List<Person> queueDown = floor.getQueueDown();

        assertThat(queueDown.containsAll(personList), is(equalTo(true)));
    }

    @Test
    public void takePersonFromQueueUp_incorrect_argument() {
        Floor floor = new Floor(2);

        assertThrows(IllegalArgumentException.class, () -> {
            floor.takePersonFromQueueUp(-20);
        });
    }

    @Test
    public void takePersonFromQueueUp_success() {
        Floor floor = new Floor(2);
        Person person1 = Person.of(25, 3);
        Person person2 = Person.of(30, 4);

        floor.putPersonsToQueueUp(List.of(person1, person2));
        Person person1Temp = floor.takePersonFromQueueUp(25);
        Person person2Temp = floor.takePersonFromQueueUp(40);

        assertThat(person1, is(equalTo(person1Temp)));
        assertThat(person2, is(equalTo(person2Temp)));
    }

    @Test
    public void takePersonFromQueueDown_incorrect_argument() {
        Floor floor = new Floor(3);

        assertThrows(IllegalArgumentException.class, () -> {
            floor.takePersonFromQueueDown(-20);
        });
    }

    @Test
    public void takePersonFromQueueDown_success() {
        Floor floor = new Floor(3);
        Person person1 = Person.of(25, 2);
        Person person2 = Person.of(30, 1);

        floor.putPersonsToQueueDown(List.of(person1, person2));
        Person person1Temp = floor.takePersonFromQueueDown(25);
        Person person2Temp = floor.takePersonFromQueueDown(40);

        assertThat(person1, is(equalTo(person1Temp)));
        assertThat(person2, is(equalTo(person2Temp)));
    }

    @Test
    public void run_person_service_is_null() {
        Floor floor = new Floor(2);
        FloorService floorService = FloorServiceImpl.of(List.of(floor, new Floor(3)));
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setFloorService(floorService);
        floor.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @Test
    public void run_floor_service_is_null() {
        Floor floor = new Floor(2);
        PersonService personService = PersonServiceImpl.create();
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setPersonService(personService);
        floor.setCallingElevatorService(callingElevatorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @Test
    public void run_calling_elevator_service_is_null() {
        Floor floor = new Floor(2);
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(List.of(floor, new Floor(3)));

        floor.setPersonService(personService);
        floor.setFloorService(floorService);

        assertThrows(NullPointerException.class, floor::run);
    }

    @SneakyThrows
    @Test
    public void run_success() {
        Floor floor = new Floor(2);
        PersonService personService = PersonServiceImpl.create();
        FloorService floorService = FloorServiceImpl.of(List.of(GroundFloor.create(), floor, new Floor(3)));
        CallingElevatorService callingElevatorService = CallingElevatorServiceImpl.create();

        floor.setPersonService(personService);
        floor.setFloorService(floorService);
        floor.setCallingElevatorService(callingElevatorService);

        floor.start();
        TimeUnit.SECONDS.sleep(1);
        floor.stopGeneratePersons();

        assertThat(floor, hasProperty("floorService", is(equalTo(floorService))));
        assertThat(floor, hasProperty("personService", is(equalTo(personService))));
    }
}