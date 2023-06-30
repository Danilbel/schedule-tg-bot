package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Schedule {

    List<ScheduleDay> scheduleFirstWeek;

    List<ScheduleDay> scheduleSecondWeek;
}
