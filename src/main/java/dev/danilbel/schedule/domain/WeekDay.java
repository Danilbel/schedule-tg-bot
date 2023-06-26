package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public enum WeekDay {

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

    public static WeekDay fromValue(int numberDay) {
        for (WeekDay day : WeekDay.values()) {
            if (day.numberDay == numberDay) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid value for WeekDay: " + numberDay);
    }

    public static WeekDay fromValue(String nameDay) {
        for (WeekDay day : WeekDay.values()) {
            if (day.nameDay.equals(nameDay)) {
                return day;
            }
        }
        throw new IllegalArgumentException("Invalid value for WeekDay: " + nameDay);
    }
}
