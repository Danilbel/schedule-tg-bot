package dev.danilbel.schedule.parser.response;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class WorldTimeApiResponse {

    @SerializedName("datetime")
    String dateTime;

    @SerializedName("day_of_week")
    int weekDay;
}
