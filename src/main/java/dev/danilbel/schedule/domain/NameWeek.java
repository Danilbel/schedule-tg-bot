package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum NameWeek {

    FIRST_WEEK("first", "Перший"),
    SECOND_WEEK("second", "Другий");

    String week;
    String nameWeek;

    public static NameWeek fromValue(String week) {
        for (NameWeek scheduleWeek : NameWeek.values()) {
            if (scheduleWeek.week.equals(week)) {
                return scheduleWeek;
            }
        }
        throw new IllegalArgumentException("Invalid value for NameWeek: " + week);
    }

    public NameWeek getNextWeek() {
        return this == FIRST_WEEK ? SECOND_WEEK : FIRST_WEEK;
    }
}
