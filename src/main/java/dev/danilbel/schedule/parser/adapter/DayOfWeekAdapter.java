package dev.danilbel.schedule.parser.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import dev.danilbel.schedule.domain.DayOfWeek;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class DayOfWeekAdapter implements JsonDeserializer<DayOfWeek> {

    @Override
    public DayOfWeek deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        return DayOfWeek.fromValue(
                json.getAsString()
        );
    }
}
