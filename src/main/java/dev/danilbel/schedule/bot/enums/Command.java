package dev.danilbel.schedule.bot.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum Command {

    START("/start", "Почати роботу"),
    CURRENT("/current", "Поточна пара"),
    NEXT("/next", "Наступна пара"),
    LAST("/last", "Залишок часу до кінця пари/перерви"),
    TIMETABLE("/timetable", "Розклад пар"),
    TODAY("/today", "Пари на сьогодні"),
    NEXT_DAY("/next_day", "Пари на наступний робочий день"),
    CURRENT_WEEK("/current_week", "Пари на поточний тиждень"),
    NEXT_WEEK("/next_week", "Пари на наступний тиждень"),
    HELP("/help", "Повний список команд");

    String command;
    String description;
}
