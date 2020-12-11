package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.elevator.Elevator;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.service.FloorService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.*;

@Slf4j
public class FloorServiceImpl implements FloorService {
    private final List<Floor> floorList;

    private FloorServiceImpl(List<Floor> floorList) {
        this.floorList = floorList;
        log.debug("Created floor service");
    }

    public static FloorService of(List<Floor> floorList) {
        log.debug("Creating floor service");
        checkNotNull(floorList, "floor list is null");
        checkArgument(floorList.size() > 1, "floor list size less than or equal to 1");

        return new FloorServiceImpl(floorList);
    }

    @Override
    public boolean isUseThisList(List<Floor> floorList) {
        checkNotNull(floorList, "floor list is null");

        return this.floorList == floorList;
    }

    @Override
    public boolean isContains(Floor floor) {
        checkNotNull(floor, "floor is null");

        return floorList.contains(floor);
    }

    @Override
    public int getMaxNumberFloor() {
        return floorList.size();
    }

    @SneakyThrows
    @Override
    public Floor nextFloor(Floor floor) {
        checkNotNull(floor, "floor is null");
        checkArgument(floor.getNumber() != floorList.size(), "floor is last in list");

        log.info("Floor: {}", floor);
        Floor result = floorList.get(floorList.indexOf(floor) + 1);
        TimeUnit.MILLISECONDS.sleep(Elevator.MOVE_SPEED);
        log.info("Move to floor: {}", result);
        return result;
    }

    @SneakyThrows
    @Override
    public Floor prevFloor(Floor floor) {
        checkNotNull(floor, "floor is null");
        checkArgument(floor.getNumber() != Floor.MIN_FLOOR, "floor is first in list");

        log.info("Floor: {}", floor);
        Floor result = floorList.get(floorList.indexOf(floor) - 1);
        TimeUnit.MILLISECONDS.sleep(Elevator.MOVE_SPEED);
        log.info("Move to floor: {}", result);
        return result;
    }

    @Override
    public Floor move(Floor floor, Direction direction) {
        checkNotNull(floor, "floor is null");
        checkNotNull(direction, "direction is null");

        Floor result = null;
        switch (direction) {
            case UP:
                result = nextFloor(floor);
                break;

            case DOWN:
               result = prevFloor(floor);
                break;

            case STOP:
                log.info("Floor: {}", floor);
                result = floor;
                break;
        }
        return result;
    }
}
