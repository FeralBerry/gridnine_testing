package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final List<Flight> removedFlight = new ArrayList<>();
    private static final List<Flight> flightBuilder = new ArrayList<>(FlightBuilder.createFlights());
    public static void main(String[] args) {
        System.out.println("Вывод тестовых образцов");
        System.out.println(flightBuilder);
        task1();
        setRemovedFlight();
        System.out.println();
        System.out.println("Вылет до текущего момента времени удалены");
        System.out.println(flightBuilder);
        System.out.println();
        task2();
        setRemovedFlight();
        System.out.println();
        System.out.println("Сегменты с датой прилёта раньше даты вылета удалены");
        System.out.println(flightBuilder);
        System.out.println();
        task3();
        setRemovedFlight();
        System.out.println();
        System.out.println("Перелеты, где общее время, проведённое на земле, превышает два часа удалены");
        System.out.println();
        System.out.println("Оставшиеся перелёты:");
        System.out.println(flightBuilder);
        System.out.println();
    }
    private static ZonedDateTime zonedDateTime(LocalDateTime dateTime){
        return ZonedDateTime.of(dateTime, ZoneId.systemDefault());
    }
    private static void setRemovedFlight(){
        for (Flight flight : removedFlight) {
            flightBuilder.remove(flight);
        }
        removedFlight.clear();
    }
    private static void task1(){
        System.out.println("Вылет до текущего момента времени");
        LocalDateTime now = LocalDateTime.now();
        flightBuilder
                .forEach(value -> value.getSegments()
                        .forEach(item -> {
                            if (zonedDateTime(item.getDepartureDate()).toInstant().toEpochMilli() < zonedDateTime(now).toInstant().toEpochMilli()) {
                                System.out.println(value);
                                removedFlight.add(value);
                            }
                        }));
    }
    private static void task2(){
        System.out.println("Сегменты с датой прилёта раньше даты вылета");
        flightBuilder
                .forEach(value -> value.getSegments()
                        .forEach(item -> {
                            if (zonedDateTime(item.getDepartureDate()).toInstant().toEpochMilli() > zonedDateTime(item.getArrivalDate()).toInstant().toEpochMilli()) {
                                System.out.println(value);
                                removedFlight.add(value);
                            }
                        }));
    }
    private static void task3(){
        long twoHourMilli = 2 * 60 *60 * 1000;
        System.out.println("Перелеты, где общее время, проведённое на земле, превышает два часа:");
        for (Flight value : flightBuilder) {
            List<Segment> items = value.getSegments();
            long time = 0;
            if (items.size() > 1) {
                for (int j = 0; j < items.size() - 1; j++) {
                    time = time + zonedDateTime(items.get(j + 1).getDepartureDate()).toInstant().toEpochMilli() - zonedDateTime(items.get(j).getArrivalDate()).toInstant().toEpochMilli();
                }
            }
            if (time >= twoHourMilli) {
                System.out.println(value);
                removedFlight.add(value);
            }
        }
    }
}
