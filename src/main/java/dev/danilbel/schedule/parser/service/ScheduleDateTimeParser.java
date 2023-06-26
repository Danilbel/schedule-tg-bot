package dev.danilbel.schedule.parser.service;

import com.google.gson.Gson;
import dev.danilbel.schedule.domain.ScheduleDateTime;
import dev.danilbel.schedule.domain.ScheduleWeek;
import dev.danilbel.schedule.domain.WeekDay;
import dev.danilbel.schedule.parser.config.ParserConfig;
import dev.danilbel.schedule.parser.response.WorldTimeApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ScheduleDateTimeParser {

    ParserConfig parserConfig;

    ResponseApiService responseApiService;

    public ScheduleDateTime getScheduleDateTime() {

        var json = responseApiService.getResponseApiDateTime();

        Gson gson = new Gson();
        var worldTimeApiResponse = gson.fromJson(json, WorldTimeApiResponse.class);

        var currentDateTime = OffsetDateTime
                .parse(
                        worldTimeApiResponse.getDateTime()
                ).toLocalDateTime();

        var weekDay = WeekDay
                .fromValue(
                        worldTimeApiResponse.getWeekDay()
                );

        var scheduleWeek = getScheduleWeek(currentDateTime.toLocalDate());

        return ScheduleDateTime.builder()
                .date(currentDateTime.toLocalDate())
                .time(currentDateTime.toLocalTime())
                .weekDay(weekDay)
                .scheduleWeek(scheduleWeek)
                .build();
    }

    private ScheduleWeek getScheduleWeek(LocalDate currentDate) {

        var startSemesterDate = parserConfig.getStartSemesterDate();

        var weekFields = WeekFields.of(Locale.forLanguageTag("uk-UA"));
        var startWeekNumber = startSemesterDate.get(weekFields.weekOfWeekBasedYear());
        var currentWeekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());

        var weekNumber = currentWeekNumber - startWeekNumber + 1;

        var startSemesterWeek = parserConfig.getStartSemesterWeek();

        if (startSemesterWeek == ScheduleWeek.FIRST_WEEK) {

            return weekNumber % 2 != 0
                    ? ScheduleWeek.FIRST_WEEK
                    : ScheduleWeek.SECOND_WEEK;
        } else {

            return weekNumber % 2 != 0
                    ? ScheduleWeek.SECOND_WEEK
                    : ScheduleWeek.FIRST_WEEK;
        }
    }
}
