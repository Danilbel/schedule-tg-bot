package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum DayOfWeek {

    SUNDAY(0, "Нд", "Неділя"),
    MONDAY(1, "Пн", "Понеділок"),
    TUESDAY(2, "Вв", "Вівторок"),
    WEDNESDAY(3, "Ср", "Середа"),
    THURSDAY(4, "Чт", "Четвер"),
    FRIDAY(5, "Пт", "П'ятниця"),
    SATURDAY(6, "Сб", "Субота");

    int numberDay;
    String nameDay;
    String fullNameDay;

    public static DayOfWeek fromValue(int numberDay) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.numberDay == numberDay) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid value for WeekDay: " + numberDay);
    }

    public static DayOfWeek fromValue(String nameDay) {
        for (DayOfWeek day : DayOfWeek.values()) {
            if (day.nameDay.equals(nameDay)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid value for WeekDay: " + nameDay);
    }

    public DayOfWeek getNextWorkDay() {
        return this == SATURDAY
                ? MONDAY
                : DayOfWeek.fromValue(numberDay + 1);
    }
}
