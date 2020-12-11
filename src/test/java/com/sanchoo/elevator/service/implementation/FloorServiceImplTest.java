package com.sanchoo.elevator.service.implementation;

import com.sanchoo.elevator.entity.elevator.Direction;
import com.sanchoo.elevator.entity.floor.Floor;
import com.sanchoo.elevator.entity.floor.GroundFloor;
import com.sanchoo.elevator.entity.floor.LastFloor;
import com.sanchoo.elevator.service.FloorService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FloorServiceImplTest {

    @Test
    public void of_list_is_null() {
        assertThrows(NullPointerException.class, () -> {
           FloorServiceImpl.of(null);
        });
    }

    @Test
    public void of_size_of_list_incorrect() {
        List<Floor> floorList = List.of(GroundFloor.create());

        assertThrows(IllegalArgumentException.class, () -> {
           FloorServiceImpl.of(floorList);
        });
    }

    @Test
    public void of_success() {
        List<Floor> floorList = List.of(GroundFloor.create(), LastFloor.of(2));

        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThat(floorService.isUseThisList(floorList), is(equalTo(true)));
    }

    @Test
    public void isUseThisList_argument_is_null() {
        List<Floor> floorList = List.of(GroundFloor.create(), LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(NullPointerException.class, () -> {
            floorService.isUseThisList(null);
        });
    }

    @Test
    public void isUseThisList_success() {
        List<Floor> floorList = List.of(GroundFloor.create(), LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        boolean isUsed = floorService.isUseThisList(floorList);

        assertThat(isUsed, is(true));
    }

    @Test
    public void isContains_floor_is_null() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(NullPointerException.class, () -> {
            floorService.isContains(null);
        });
    }

    @Test
    public void isContains_success() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        boolean isContains = floorService.isContains(floor);

        assertThat(isContains, is(true));
    }

    @Test
    public void nextFloor_floor_is_null() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(NullPointerException.class, () -> {
           floorService.nextFloor(null);
        });
    }

    @Test
    public void nextFloor_incorrect_floor() {
        Floor floor = LastFloor.of(2);
        List<Floor> floorList = List.of(GroundFloor.create(), floor);
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(IllegalArgumentException.class, () -> {
           floorService.nextFloor(floor);
        });
    }

    @Test
    public void nextFloor_success() {
        Floor groundFloor = GroundFloor.create();
        Floor lastFloor = LastFloor.of(2);
        List<Floor> floorList = List.of(groundFloor, lastFloor);
        FloorService floorService = FloorServiceImpl.of(floorList);

        Floor nextFloor = floorService.nextFloor(groundFloor);

        assertThat(nextFloor, is(equalTo(lastFloor)));
    }

    @Test
    public void prevFloor_floor_is_null() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(NullPointerException.class, () -> {
            floorService.prevFloor(null);
        });
    }

    @Test
    public void prevFloor_incorrect_floor() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(IllegalArgumentException.class, () -> {
            floorService.prevFloor(floor);
        });
    }

    @Test
    public void prevFloor_success() {
        Floor groundFloor = GroundFloor.create();
        Floor lastFloor = LastFloor.of(2);
        List<Floor> floorList = List.of(groundFloor, lastFloor);
        FloorService floorService = FloorServiceImpl.of(floorList);

        Floor prevFloor = floorService.prevFloor(lastFloor);

        assertThat(prevFloor, is(equalTo(groundFloor)));
    }

    @Test
    public void move_floor_is_null() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(NullPointerException.class, () -> {
           floorService.move(null, Direction.UP);
        });
    }

    @Test
    public void move_direction_is_null() {
        Floor floor = GroundFloor.create();
        List<Floor> floorList = List.of(floor, LastFloor.of(2));
        FloorService floorService = FloorServiceImpl.of(floorList);

        assertThrows(NullPointerException.class, () -> {
            floorService.move(floor, null);
        });
    }

    @Test
    public void move_success() {
        Floor floor = GroundFloor.create();
        Floor lastFloor = LastFloor.of(2);
        List<Floor> floorList = List.of(floor, lastFloor);
        FloorService floorService = FloorServiceImpl.of(floorList);

        Floor moveFloor = floorService.move(floor, Direction.UP);
        Floor moveFloor2 = floorService.move(moveFloor, Direction.DOWN);

        assertThat(moveFloor, is(equalTo(lastFloor)));
        assertThat(moveFloor2, is(equalTo(floor)));
    }
}