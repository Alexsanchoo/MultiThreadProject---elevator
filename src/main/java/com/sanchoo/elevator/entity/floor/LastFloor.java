package com.sanchoo.elevator.entity.floor;

import com.sanchoo.elevator.entity.person.Person;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;
@Slf4j
public class LastFloor extends Floor{

    private LastFloor(int number) {
        super(number);
        log.debug("Created last floor");
    }

    public static LastFloor of(int number) {
        log.debug("Creating last floor");
        checkArgument(number > 0, "number less than or equal to 0");

        return new LastFloor(number);
    }

    @SneakyThrows
    @Override
    public void run() {
        checkNotNull(personService, "person service is null");
        checkNotNull(floorService, "floor service is null");
        checkNotNull(callingElevatorService, "calling elevator service is null");

        this.isStopped = false;
        Random random = new Random();

        while(!isStopped) {
            List<Person> personsMoveDown = personService.createdRandomNumberOfPersonMoveDown(number);

            putPersonsToQueueDown(personsMoveDown);
            log.info("Down: {}", queueDown);

            turnOnDownButton();

            TimeUnit.SECONDS.sleep(random.nextInt(MAX_WAITING_TIME - MIN_WAITING_TIME) + MIN_WAITING_TIME + 1);
        }
    }
}
