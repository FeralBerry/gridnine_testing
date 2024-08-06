package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlightFilterImpl implements FlightFilter {
    private final LocalDateTime timeNow;

    public FlightFilterImpl() {
        this.timeNow = LocalDateTime.now();
    }

    /**
     * Метод возвращает список полетов без сегментов до текущего момента времени.
     *
     * @param flights список полетов
     * @return возвращает список {@link List<Flight>}, содержащий отфильтрованный список полетов
     */
    @Override
    public List<Flight> filterFlightTimeUntilNow(List<Flight> flights) {
        if (flights != null) {
            return flights.stream()
                    .filter(flight -> flight.getSegments().stream()
                            .anyMatch(segment -> timeNow.isBefore(segment.getDepartureDate())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Метод возвращает список полетов без сегментов прилёта раньше даты вылета
     *
     * @param flights полный список полетов
     * @return возвращает список {@link List<Flight>}, содержащий отфильтрованный список полетов
     */
    @Override
    public List<Flight> filterFlightsBeforeDepartureDate(List<Flight> flights) {
        List<Flight> result = new ArrayList<>();
        if (flights != null) {
            flights.forEach(flight -> flight.getSegments()
                    .stream()
                    .filter(segment -> segment.getArrivalDate().isAfter(segment.getDepartureDate())).limit(1)
                    .forEach(segment -> result.add(flight)));
            return result;
        }
        return result;
    }

    /**
     * Метод осуществляет фильтрацию списка рейсов в соответствии с условием, что общее время, проведённое на земле,
     * не превышает два часа.
     *
     * @param flights полный список полетов
     * @return возвращает список {@link List<Flight>}, содержащий отфильтрованный список полетов
     */
    @Override
    public List<Flight> filterTimeOnEarthMoreTwoHours(List<Flight> flights) {
        List<Flight> result = new ArrayList<>();
        if (flights != null) {
            result = flights.stream()
                    .filter(flight -> flight.getSegments().size() > 1)
                    .filter(flight -> {
                        long countHours = IntStream.range(1, flight.getSegments().size())
                                .map(i -> Math.toIntExact(checkTimeDifference(flight.getSegments().get(i - 1).getArrivalDate(),
                                        flight.getSegments().get(i).getDepartureDate())))
                                .sum();
                        long checkTime = 2 * 60 * 60 * 1000;
                        return - countHours <= checkTime;
                    })
                    .collect(Collectors.toList());
            result.addAll(flights.stream()
                    .filter(flight -> flight.getSegments().size() <= 1)
                    .toList());
            return result;
        }
        return result;
    }

    /**
     * Служебный метод для вычисления разницы между двумя промежутками времени(прибытия и отбытия)
     *
     * @param arrival   время прибытия
     * @param departure время отбытия
     * @return возвращает целочисленное значение типа long
     */
    private long checkTimeDifference(LocalDateTime arrival, LocalDateTime departure) {
        return (zonedDateTime(arrival).toInstant().toEpochMilli() - zonedDateTime(departure).toInstant().toEpochMilli());
    }
    /**
     * Служебный метод для преобразования даты и времени полета по временной зоне сервера в тип данных ZonedDateTime
     * для дальнейшего вычисления времени в миллисекундах
     *
     * @param dateTime дата в формате LocalDateTime
     * @return возвращает данные в формате ZonedDateTime в соответствии с зоной сервера.
     */
    private static ZonedDateTime zonedDateTime(LocalDateTime dateTime){
        return ZonedDateTime.of(dateTime, ZoneId.systemDefault());
    }
}
