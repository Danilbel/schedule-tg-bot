package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Schedule {

    List<ScheduleDay> scheduleFirstWeek;

    List<ScheduleDay> scheduleSecondWeek;

    public void sort() {
        scheduleFirstWeek.sort(Comparator.comparing(ScheduleDay::getDay));
        scheduleSecondWeek.sort(Comparator.comparing(ScheduleDay::getDay));

        for (var scheduleDay : scheduleFirstWeek) {
            scheduleDay.sort();
        }

        for (var scheduleDay : scheduleSecondWeek) {
            scheduleDay.sort();
        }
    }
}
