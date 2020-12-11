package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.entity.person.Person;
import com.sanchoo.elevator.service.PersonService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class PersonServiceImpl implements PersonService {

    private PersonServiceImpl() {
        log.debug("Created person service");
    }

    public static PersonServiceImpl create() {
        log.debug("Creating person service");
        return new PersonServiceImpl();
    }

    @Override
    public List<Person> createdRandomNumberOfPersonMoveUp(int floorNumber, int maxFloorNumber) {
        checkArgument(floorNumber > 0, "floor number less than or equal to 0");
        checkArgument(maxFloorNumber >= floorNumber, "max floor number less than 'floorNumber'");

        Random random = new Random();
        int maxPersons = random.nextInt(MAX_NUMBER_OF_PERSON + 1);
        List<Person> personList = IntStream.range(0, maxPersons)
                .mapToObj(v -> {
                    int weight = random.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
                    int floor = random.nextInt(maxFloorNumber - floorNumber) + floorNumber + 1;
                    return Person.of(weight, floor);
                })
                .collect(Collectors.toList());

        return personList;
    }

    @Override
    public List<Person> createdRandomNumberOfPersonMoveDown(int floorNumber) {
        checkArgument(floorNumber > 0, "floor number less than or equal to 0");

        Random random = new Random();
        int maxPersons = random.nextInt(MAX_NUMBER_OF_PERSON + 1);
        List<Person> personList = IntStream.range(0, maxPersons)
                .mapToObj(v -> {
                    int weight = random.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
                    int floor = random.nextInt(floorNumber - Floor.MIN_FLOOR) + Floor.MIN_FLOOR;
                    return Person.of(weight, floor);
                })
                .collect(Collectors.toList());

        return personList;
    }
}
