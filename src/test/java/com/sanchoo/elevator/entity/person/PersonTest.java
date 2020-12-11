package com.sanchoo.elevator.entity.person;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

class PersonTest {

    @Test
    public void of_incorrect_weight() {
        assertThrows(IllegalArgumentException.class, () -> {
           Person.of(-1, 1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
           Person.of(0, 1);
        });
    }

    @Test
    public void of_incorrect_floor() {
        assertThrows(IllegalArgumentException.class, () -> {
            Person.of(25, -1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Person.of(24, 0);
        });
    }

    @Test
    public void of_success() {
        Person person = Person.of(25, 3);

        assertThat(person, hasProperty("weight", is(equalTo(25))));
        assertThat(person, hasProperty("floor", is(equalTo(3))));
    }
}