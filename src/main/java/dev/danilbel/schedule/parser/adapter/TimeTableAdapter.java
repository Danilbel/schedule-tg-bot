package dev.danilbel.schedule.parser.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.danilbel.schedule.domain.TimeTable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class TimeTableAdapter implements JsonDeserializer<TimeTable> {

    @Override
    public TimeTable deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return TimeTable.fromValue(
                json.getAsString()
        );
    }
}
