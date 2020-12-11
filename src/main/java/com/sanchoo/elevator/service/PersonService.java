package com.sanchoo.elevator.service;

import com.sanchoo.elevator.entity.person.Person;

import java.util.List;

public interface PersonService {
    int MAX_NUMBER_OF_PERSON = 5;
    int MAX_WEIGHT = 120;
    int MIN_WEIGHT = 15;
    List<Person> createdRandomNumberOfPersonMoveUp(int floorNumber, int maxFloorNumber);
    List<Person> createdRandomNumberOfPersonMoveDown(int floorNumber);
}
