package com.sanchoo.elevator.entity.floor;

import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.person.Person;
import com.sanchoo.elevator.service.CallingElevatorService;
import com.sanchoo.elevator.service.FloorService;
import com.sanchoo.elevator.service.PersonService;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Floor extends Thread{
    public static final int MAX_WAITING_TIME = 30;
    public static final int MIN_WAITING_TIME = 10;
    public static final int MIN_FLOOR = 1;
    private final Lock lock;
    @Getter
    protected final UUID ID;
    @Getter
    protected final int number;
    protected final Queue<Person> queueDown;
    protected final Queue<Person> queueUp;
    @Getter
    protected PersonService personService;
    protected CallingElevatorService callingElevatorService;
    @Getter
    protected FloorService floorService;
    protected boolean isUpButtonPressed;
    protected boolean isDownButtonPressed;
    protected boolean isStopped;

    public Floor(int number) {
        UUID id = UUID.randomUUID();
        log.debug("Creating floor {}", id);

        checkArgument(number > 0, "number less than or equal to 0");

        this.ID = id;
        this.lock = new ReentrantLock(true);
        this.queueDown = new ConcurrentLinkedQueue<>();
        this.queueUp = new ConcurrentLinkedQueue<>();
        this.number = number;
        setName("floor-" + number);
        log.debug("Created floor {}", this.ID);
    }

    public void setPersonService(PersonService personService) {
        log.debug("Setting person service for floor {}", this.ID);
        checkNotNull(personService, "person service is null");

        this.personService = personService;
        log.debug("Set person service for floor {}", this.ID);
    }

    public void setCallingElevatorService(CallingElevatorService callingElevatorService) {
        log.debug("Setting calling elevator service for floor {}", this.ID);
        checkNotNull(callingElevatorService, "calling elevator service is null");

        this.callingElevatorService = callingElevatorService;
        log.debug("Set calling elevator service for floor {}", this.ID);
    }

    public void setFloorService(FloorService floorService) {
        log.debug("Setting floor service for floor {}", this.ID);
        checkNotNull(floorService, "floor service is null");
        checkArgument(floorService.isContains(this), "floor list doesn't contain this floor");

        this.floorService = floorService;
        log.debug("Set floor service for floor {}", this.ID);
    }

    public List<Person> getQueueDown() {
        return List.copyOf(queueDown);
    }

    public List<Person> getQueueUp() {
        return List.copyOf(queueUp);
    }

    public void stopGeneratePersons() {
        log.debug("Stopping generate people");
        isStopped = true;
        log.debug("Stopped generate people");
    }

    public boolean turnOnUpButton() {
        log.debug("Enabling up button");
        checkNotNull(callingElevatorService, "calling elevator service is null");

        if(!isUpButtonPressed && !queueUp.isEmpty()) {
            isUpButtonPressed = true;
            callingElevatorService.putCalling(number, Direction.UP);
            log.debug("Up button is enabled");
            return true;
        }
        log.debug("Up button is not enabled");
        return false;
    }

    public void turnOffUpButton() {
        this.isUpButtonPressed = false;
        log.debug("Up button is disabled");
    }

    public boolean turnOnDownButton() {
        log.debug("Enabling down button");
        checkNotNull(callingElevatorService, "calling elevator service is null");

        if(!this.isDownButtonPressed && !queueDown.isEmpty()) {
            this.isDownButtonPressed = true;
            callingElevatorService.putCalling(number, Direction.DOWN);
            log.debug("Down button is enabled");
            return true;
        }
        log.debug("Down button is not enabled");
        return false;
    }

    public void turnOffDownButton() {
        this.isDownButtonPressed = false;
        log.debug("Down button is disabled");
    }

    public void putPersonToQueueUp(Person person) {
        log.debug("Adding person {} to queue up", person);
        checkNotNull(person, "person is null");
        checkArgument(person.getFloor() > number, "number of floor is less than or equal to floor number of person");

        lock.lock();
        queueUp.add(person);
        log.debug("Person {} added to queue up", person);
        lock.unlock();
    }

    public void putPersonToQueueDown(Person person) {
        log.debug("Adding person {} to queue down", person);
        checkNotNull(person, "person is null");
        checkArgument(person.getFloor() < number, "number of floor is more than or equal to floor number of person");

        lock.lock();
        queueDown.add(person);
        log.debug("Person {} added to queue down", person);
        lock.unlock();
    }

    public void putPersonsToQueueUp(List<Person> personList) {
        checkNotNull(personList, "person list is null");

        personList.forEach(this::putPersonToQueueUp);
    }

    public void putPersonsToQueueDown(List<Person> personList) {
        checkNotNull(personList, "person list is null");

        personList.forEach(this::putPersonToQueueDown);
    }

    public Person takePersonFromQueueUp(int freeWeight) {
        checkArgument(freeWeight >= 0, "free weight less than 0");

        lock.lock();
        Person result = null;
        Person personTemp = queueUp.peek();
        if(personTemp != null && personTemp.getWeight() <= freeWeight) {
            result = queueUp.poll();
            log.info("Take person {} from up queue", result);
        }
        lock.unlock();
        return result;
    }

    public Person takePersonFromQueueDown(int freeWeight) {
        checkArgument(freeWeight >= 0, "free weight less than 0");

        lock.lock();
        Person result = null;
        Person personTemp = queueDown.peek();
        if(personTemp != null && personTemp.getWeight() <= freeWeight) {
            result = queueDown.poll();
            log.info("Take person {} from down queue", result);
        }
        lock.unlock();
        return result;
    }

    @SneakyThrows
    @Override
    public void run() {
        checkNotNull(personService, "person service is null");
        checkNotNull(floorService, "floor service is null");
        checkNotNull(callingElevatorService, "calling elevator service is null");

        isStopped = false;
        Random random = new Random();

        while(!isStopped) {
            List<Person> personsMoveUp = personService.createdRandomNumberOfPersonMoveUp(number, floorService.getMaxNumberFloor());
            List<Person> personsMoveDown = personService.createdRandomNumberOfPersonMoveDown(number);

            putPersonsToQueueUp(personsMoveUp);
            putPersonsToQueueDown(personsMoveDown);
            log.info("Up: {}, Down: {}", queueUp, queueDown);

            turnOnUpButton();
            turnOnDownButton();

            TimeUnit.SECONDS.sleep(random.nextInt(MAX_WAITING_TIME - MIN_WAITING_TIME) + MIN_WAITING_TIME + 1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Floor floor = (Floor) o;
        return Objects.equals(ID, floor.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return new StringBuilder("Floor{")
                .append("number=").append(number)
                .append(", queueDown=").append(queueDown)
                .append(", queueUp=").append(queueUp)
                .append(", isUpButtonPressed").append(isUpButtonPressed)
                .append(", isDownButtonPressed").append(isDownButtonPressed)
                .append("}").toString();
    }
}
