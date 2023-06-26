package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum ScheduleWeek {

    FIRST_WEEK("first", "Перший"),
    SECOND_WEEK("second", "Другий");

    String week;
    String nameWeek;

    public static ScheduleWeek fromValue(String week) {
        for (ScheduleWeek scheduleWeek : ScheduleWeek.values()) {
            if (scheduleWeek.week.equals(week)) {
                return scheduleWeek;
            }
        }
        throw new IllegalArgumentException("Invalid value for ScheduleWeek: " + week);
    }
}
