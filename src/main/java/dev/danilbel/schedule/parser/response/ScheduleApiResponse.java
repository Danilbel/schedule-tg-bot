package dev.danilbel.schedule.parser.response;

import com.google.gson.annotations.SerializedName;
import dev.danilbel.schedule.domain.Schedule;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ScheduleApiResponse {

    @SerializedName("data")
    Schedule schedule;
}
