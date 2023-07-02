package dev.danilbel.schedule.parser.service;

import com.google.gson.Gson;
import dev.danilbel.schedule.domain.ScheduleDateTime;
import dev.danilbel.schedule.domain.WeekName;
import dev.danilbel.schedule.domain.DayOfWeek;
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

        var weekDay = DayOfWeek
                .fromValue(
                        worldTimeApiResponse.getWeekDay()
                );

        var scheduleWeek = getScheduleWeek(currentDateTime.toLocalDate());

        return ScheduleDateTime.builder()
                .date(currentDateTime.toLocalDate())
                .time(currentDateTime.toLocalTime())
                .dayOfWeek(weekDay)
                .weekName(scheduleWeek)
                .build();

//        // временно для тестов
//        return ScheduleDateTime.builder()
//                .date(LocalDate.of(2023, 6, 26))
//                .time(LocalTime.of(15, 50, 0))
//                .dayOfWeek(DayOfWeek.MONDAY)
//                .weekName(WeekName.FIRST_WEEK)
//                .build();
    }

    private WeekName getScheduleWeek(LocalDate currentDate) {

        var startSemesterDate = parserConfig.getStartSemesterDate();

        var weekFields = WeekFields.of(Locale.forLanguageTag("uk-UA"));
        var startWeekNumber = startSemesterDate.get(weekFields.weekOfWeekBasedYear());
        var currentWeekNumber = currentDate.get(weekFields.weekOfWeekBasedYear());

        var weekNumber = currentWeekNumber - startWeekNumber + 1;

        var startSemesterWeek = parserConfig.getStartSemesterWeek();

        if (startSemesterWeek == WeekName.FIRST_WEEK) {

            return weekNumber % 2 != 0
                    ? WeekName.FIRST_WEEK
                    : WeekName.SECOND_WEEK;
        } else {

            return weekNumber % 2 != 0
                    ? WeekName.SECOND_WEEK
                    : WeekName.FIRST_WEEK;
        }
    }
}
