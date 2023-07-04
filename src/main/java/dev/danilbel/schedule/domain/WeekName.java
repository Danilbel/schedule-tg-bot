package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum WeekName {

    FIRST_WEEK("first", "Перший"),
    SECOND_WEEK("second", "Другий");

    String week;
    String nameWeek;

    public static WeekName fromValue(String week) {
        for (WeekName scheduleWeek : WeekName.values()) {
            if (scheduleWeek.week.equals(week)) {
                return scheduleWeek;
            }
        }
        throw new IllegalArgumentException("Invalid value for WeekName: " + week);
    }

    public WeekName getNextWeek() {
        return this == FIRST_WEEK ? SECOND_WEEK : FIRST_WEEK;
    }
}
