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

    public List<ScheduleDay> getScheduleByWeek(NameWeek nameWeek) {

        return nameWeek == NameWeek.FIRST_WEEK
                ? scheduleFirstWeek
                : scheduleSecondWeek;
    }

    public ScheduleDay getScheduleDayByDayOfWeek(NameWeek nameWeek, DayOfWeek dayOfWeek) {

        return getScheduleByWeek(nameWeek).stream()
                .filter(scheduleDay -> scheduleDay.getDay() == dayOfWeek)
                .findFirst()
                .orElse(null);
    }

    public String toStringScheduleByWeek(NameWeek nameWeek) {

        var stringBuilder = new StringBuilder();

        var schedule = getScheduleByWeek(nameWeek);

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
