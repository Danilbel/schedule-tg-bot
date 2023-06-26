package dev.danilbel.schedule.parser.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScheduleDataResponse {

    List<ScheduleDayResponse> scheduleFirstWeek;

    List<ScheduleDayResponse> scheduleSecondWeek;
}
