package com.gridnine.testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestingApplicationTests {
    FlightFilterImpl flightFilterTest = new FlightFilterImpl();
    LocalDateTime now = LocalDateTime.now();
    Segment testOne = new Segment(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 1, 10),
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 9, 10));
    Segment testTwo = new Segment(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 1, 30),
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour() + 2, 0));
    Segment testThree = new Segment(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 5, 30),
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 10, 0));
    Segment testFour = new Segment(LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth() + 1, 5, 30),
            LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 1, 0));
    List<Segment> segmentList = List.of(testOne);
    List<Segment> segmentListTwo = new ArrayList<>();
    List<Segment> segmentListThree = List.of(testTwo);
    List<Segment> segmentListFour = new ArrayList<>();
    List<Segment> segmentListFive = new ArrayList<>();


    Flight testFlight = new Flight(segmentList);
    Flight testFlightTwo = new Flight(segmentListTwo);
    Flight testFlightThree = new Flight(segmentListThree);
    Flight testFlightFour = new Flight(segmentListFour);
    Flight testFlightFive = new Flight(segmentListFive);

    List<Flight> expected = new ArrayList<>();
    List<Flight> actual = new ArrayList<>();

    @BeforeEach
    void addSegment() {
        segmentListTwo.add(testThree);
        segmentListFour.add(testFour);
        segmentListFive.add(testThree);
        segmentListFive.add(testTwo);
    }

    @AfterEach
    void clearFlight() {
        expected.clear();
        actual.clear();
    }

    @Test
    void filterFlightTimeUntilNowTest() {
        expected.add(testFlight);
        actual.add(testFlight);
        List<Flight> result = flightFilterTest.filterFlightTimeUntilNow(actual);
        assertEquals(result, expected);
    }

    @Test
    void filterFlightsBeforeDepartureDateTest() {
        expected.add(testFlightTwo);
        actual.add(testFlightFour);
        actual.add(testFlightTwo);
        List<Flight> result = flightFilterTest.filterFlightsBeforeDepartureDate(actual);
        assertEquals(result, expected);
    }

    @Test
    void filterTimeOnEarthMoreTwoHoursTest() {
        expected.add(testFlightThree);
        actual.add(testFlightFive);
        actual.add(testFlightThree);
        List<Flight> result = flightFilterTest.filterTimeOnEarthMoreTwoHours(actual);
        assertEquals(result, expected);
    }
}