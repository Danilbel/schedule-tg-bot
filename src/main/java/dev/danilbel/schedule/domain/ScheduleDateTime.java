package dev.danilbel.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScheduleDateTime {

    LocalDate date;

    LocalTime time;

    DayOfWeek dayOfWeek;

    WeekName weekName;

    public ScheduleDateTime getNextWorkScheduleDateTime() {

        var nextWorkDayOfWeek = dayOfWeek.getNextWorkDay();

        var nextWorkDate = date.plusDays(
                nextWorkDayOfWeek == DayOfWeek.MONDAY
                && dayOfWeek != DayOfWeek.SUNDAY
                        ? 2 : 1
        );

        var nextWeekName = nextWorkDayOfWeek == DayOfWeek.MONDAY
                ? weekName.getNextWeek()
                : weekName;

        return ScheduleDateTime.builder()
                .date(nextWorkDate)
                .time(time)
                .dayOfWeek(nextWorkDayOfWeek)
                .weekName(nextWeekName)
                .build();
    }
}
