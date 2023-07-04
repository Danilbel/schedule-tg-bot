package dev.danilbel.schedule.domain.service;

import dev.danilbel.schedule.domain.DayOfWeek;
import dev.danilbel.schedule.domain.Schedule;
import dev.danilbel.schedule.domain.ScheduleDay;
import dev.danilbel.schedule.domain.WeekName;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ScheduleWeekService {

    ScheduleDayService scheduleDayService;

    public List<ScheduleDay> getSortedScheduleWeekByWeekName(Schedule schedule, WeekName weekName) {

        schedule.sort();

        return weekName == WeekName.FIRST_WEEK
                ? schedule.getScheduleFirstWeek()
                : schedule.getScheduleSecondWeek();
    }

    public ScheduleDay getSortedScheduleDayOrNullByDayOfWeek(Schedule schedule, WeekName weekName, DayOfWeek dayOfWeek) {

        return getSortedScheduleWeekByWeekName(schedule, weekName)
                .stream()
                .filter(scheduleDay -> scheduleDay.getDay() == dayOfWeek)
                .findFirst()
                .orElse(null);
    }

    public String scheduleWeekByWeekNameToString(Schedule schedule, WeekName weekName) {

        var stringBuilder = new StringBuilder();

        var week = getSortedScheduleWeekByWeekName(schedule, weekName);

        for (var i = 0; i < week.size(); i++) {

            var day = week.get(i);

            if (day.getPairs() == null || day.getPairs().isEmpty()) {

                continue;
            }

            if (i == 0) {

                stringBuilder.append(scheduleDayService.scheduleDayToString(day));
            } else {

                stringBuilder.append("\n\n").append(scheduleDayService.scheduleDayToString(day));
            }
        }

        return stringBuilder.toString();
    }
}
