package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.person.Person;
import com.sanchoo.elevator.service.PersonService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

class PersonServiceImplTest {

    @Test
    public void createdRandomNumberOfPersonMoveUp_incorrect_floor_number() {
        PersonService personService = PersonServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
           personService.createdRandomNumberOfPersonMoveUp(0, 3);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           personService.createdRandomNumberOfPersonMoveUp(-2, 3);
        });

    }

    @Test
    public void createdRandomNumberOfPersonMoveUp_incorrect_max_floor_number() {
        PersonService personService = PersonServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
           personService.createdRandomNumberOfPersonMoveUp(2, 1);
        });
    }

    @Test
    public void createdRandomNumberOfPersonMoveUp_success() {
        PersonService personService = PersonServiceImpl.create();

        List<Person> personList = personService.createdRandomNumberOfPersonMoveUp(2, 5);

        personList.forEach(person -> {
            assertThat(person, hasProperty("floor", is(greaterThan(2))));
            assertThat(person, hasProperty("floor", is(lessThan(6))));
        });
    }

    @Test
    public void createdRandomNumberOfPersonMoveDown_incorrect_floor_number() {
        PersonService personService = PersonServiceImpl.create();

        assertThrows(IllegalArgumentException.class, () -> {
            personService.createdRandomNumberOfPersonMoveDown(0);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            personService.createdRandomNumberOfPersonMoveDown(-2);
        });
    }

    @Test
    public void createdRandomNumberOfPersonMoveDown_success() {
        PersonService personService = PersonServiceImpl.create();

        List<Person> personList = personService.createdRandomNumberOfPersonMoveDown(4);

        personList.forEach(person -> {
            assertThat(person, hasProperty("floor", is(lessThan(4))));
        });
    }
}