package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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

    public List<ScheduleDay> getScheduleByWeek(WeekName weekName) {

        return weekName == WeekName.FIRST_WEEK
                ? scheduleFirstWeek
                : scheduleSecondWeek;
    }

    public Optional<ScheduleDay> getScheduleDayByDayOfWeek(WeekName weekName, DayOfWeek dayOfWeek) {

        return getScheduleByWeek(weekName).stream()
                .filter(scheduleDay -> scheduleDay.getDay() == dayOfWeek)
                .findFirst();
    }

    public String scheduleByNameWeekToString(WeekName weekName) {

        var stringBuilder = new StringBuilder();

        var schedule = getScheduleByWeek(weekName);

        for (var i = 0; i < schedule.size(); i++) {

            var scheduleDay = schedule.get(i);

            if (scheduleDay.getPairs() == null || scheduleDay.getPairs().isEmpty()) {

                continue;
            }

            if (i == 0) {

                stringBuilder.append(scheduleDay);
            } else {

                stringBuilder.append("\n\n").append(scheduleDay);
            }
        }

        return stringBuilder.toString();
    }
}
