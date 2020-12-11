package com.sanchoo.elevator.entity.person;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.*;

@Slf4j
@Getter
public class Person {
    private final UUID id;
    private final int weight;
    private final int floor;

    private Person(UUID id, int weight, int floor) {
        this.id = id;
        this.weight = weight;
        this.floor = floor;
        log.debug("Created person {}", this.id);
    }

    public static Person of(int weight, int floor) {
        UUID id = UUID.randomUUID();
        log.debug("Creating person {}", id);

        checkArgument(weight > 0, "weight less than 0");
        checkArgument(floor > 0, "floor less than 0");

        return new Person(id, weight, floor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringBuilder("Person{")
                .append("weight=").append(weight)
                .append(", floor=").append(floor)
                .append("}").toString();
    }
}
