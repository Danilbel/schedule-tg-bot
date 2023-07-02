package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum TimeTable {
    FIRST_PAIR(1, "8.30", "10.05"),
    SECOND_PAIR(2, "10.25", "12.00"),
    THIRD_PAIR(3, "12.20", "13.55"),
    FOURTH_PAIR(4, "14.15", "15.50"),
    FIFTH_PAIR(5, "16.10", "17.45"),
    SIXTH_PAIR(6, "18.30", "20.05");

    int number;
    String startTime;
    String endTime;

    public static TimeTable fromValue(String startTime) {
        for (TimeTable timeTable : TimeTable.values()) {
            if (timeTable.startTime.equals(startTime)) {
                return timeTable;
            }
        }
        throw new IllegalArgumentException("Invalid value for TimeTable: " + startTime);
    }

    public static String timeTableToString() {

        StringBuilder msg = new StringBuilder("<b>Розклад пар:</b>\n\n");

        for (var time : TimeTable.values()) {
            msg.append(time).append("\n");
        }

        return msg.toString();
    }

    public LocalTime getLocalTimeOfStartTime() {

        if (startTime.equals("8.30")) {
            return LocalTime.of(8, 30);
        }

        var formatter = DateTimeFormatter.ofPattern("HH.mm");

        return LocalTime.parse(startTime, formatter);
    }

    public LocalTime getLocalTimeOfEndTime() {
        var formatter = DateTimeFormatter.ofPattern("HH.mm");

        return LocalTime.parse(endTime, formatter);
    }

    @Override
    public String toString() {
        return "<b>" + number + ". [" + startTime + " – " + endTime + "]</b>";
    }
}
