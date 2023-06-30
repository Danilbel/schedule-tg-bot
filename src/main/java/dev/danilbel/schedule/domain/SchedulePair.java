package dev.danilbel.schedule.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SchedulePair {

    String teacherName;

    String type;

    TimeTable time;

    String name;

    String place;

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder("– ");

        stringBuilder.append(name)
                .append("<i>");

        if (type != null && !type.trim().isEmpty()) {
            stringBuilder.append(", ")
                    .append(type);
        }

        if (place != null && !place.trim().isEmpty()) {
            stringBuilder.append(", place: ")
                    .append(place);
        }

        if (teacherName != null && !teacherName.trim().isEmpty()) {
            stringBuilder.append(", ")
                    .append(teacherName);
        }

        return stringBuilder.append("</i>").toString();
    }
}
