package dev.danilbel.schedule.parser.config;

import dev.danilbel.schedule.domain.ScheduleWeek;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Configuration
@PropertySource("classpath:parser.properties")
public class ParserConfig {

    @Value("${parser.start.semester.date}")
    String startSemesterDate;

    @Value("${parser.start.semester.week}")
    String startSemesterWeek;

    public LocalDate getStartSemesterDate() {
        return LocalDate.parse(startSemesterDate);
    }

    public ScheduleWeek getStartSemesterWeek() {
        return ScheduleWeek.fromValue(startSemesterWeek);
    }
}
