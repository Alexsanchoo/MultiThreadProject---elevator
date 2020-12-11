package com.sanchoo.elevator.entity.floor;

import com.sanchoo.elevator.entity.person.Person;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class GroundFloor extends Floor {

    private GroundFloor() {
        super(1);
        log.debug("Created ground floor");
    }

    public static GroundFloor create() {
        log.debug("Creating ground floor");
        return new GroundFloor();
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
            List<Person> personsMoveUp = personService.createdRandomNumberOfPersonMoveUp(number, floorService.getMaxNumberFloor());

            putPersonsToQueueUp(personsMoveUp);
            log.info("Up: {}", queueUp);

            turnOnUpButton();

            TimeUnit.SECONDS.sleep(random.nextInt(MAX_WAITING_TIME - MIN_WAITING_TIME) + MIN_WAITING_TIME + 1);
        }
    }
}
