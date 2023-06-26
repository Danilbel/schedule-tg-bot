package dev.danilbel.schedule.parser.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScheduleApiResponse {

    ScheduleDataResponse data;
}
