package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.elevator.CallingElevator;
import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.service.CallingElevatorService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class CallingElevatorServiceImpl implements CallingElevatorService {
    private final BlockingQueue<CallingElevator> callingElevatorQueue;
    private final Lock lock;

    private CallingElevatorServiceImpl() {
        this.callingElevatorQueue = new LinkedBlockingQueue<>();
        this.lock = new ReentrantLock(true);
        log.debug("Created calling elevator service");
    }

    public static CallingElevatorService create() {
        log.debug("Creating calling elevator service");
        return new CallingElevatorServiceImpl();
    }

    @SneakyThrows
    @Override
    public void putCalling(int floor, Direction direction) {
        checkArgument(floor > 0, "floor less than or equal to 0");
        checkNotNull(direction, "direction is null");
        checkArgument(direction != Direction.STOP);

        lock.lock();
        CallingElevator callingElevator = CallingElevator.of(floor, direction);
        log.info("Put: {}", callingElevator);
        callingElevatorQueue.put(callingElevator);
        lock.unlock();
    }

    @SneakyThrows
    @Override
    public CallingElevator takeCalling() {
        CallingElevator callingElevator = callingElevatorQueue.take();
        log.info("Take from queue: {}", callingElevator);
        return callingElevator;
    }

    @Override
    public boolean removeCalling(int floor, Direction direction) {
        checkArgument(floor > 0, "floor less than or equal to 0");
        checkNotNull(direction, "direction is null");
        checkArgument(direction != Direction.STOP);

        lock.lock();
        CallingElevator callingElevator = CallingElevator.of(floor, direction);
        boolean isDeleted = callingElevatorQueue.remove(callingElevator);
        if(isDeleted) {
            callingElevator.setCallProcessed(true);
            log.info("call is processed {}", callingElevator);
        }
        lock.unlock();
        return isDeleted;
    }

    @Override
    public CallingElevator isCallingExists(Floor floor) {
        checkNotNull(floor, "floor is null");

        lock.lock();
        CallingElevator callUp = CallingElevator.of(floor.getNumber(), Direction.UP);
        CallingElevator callDown = CallingElevator.of(floor.getNumber(), Direction.DOWN);

        if(callingElevatorQueue.remove(callUp)) {
            log.info("Take: {}", callUp);
            lock.unlock();
            return callUp;

        } else if(callingElevatorQueue.remove(callDown)) {
            log.info("Take: {}", callDown);
            lock.unlock();
            return callDown;
        }

        lock.unlock();
        return null;
    }
}
