package dev.danilbel.schedule.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleDay {

    DayOfWeek day;

    List<SchedulePair> pairs;

    public void sort() {
        pairs.sort(Comparator.comparing(SchedulePair::getTime));
    }

    @Override
    public String toString() {

        var stringBuilder = new StringBuilder("<b>• ").append(day.getFullNameDay()).append(": </b>");

        if (pairs == null || pairs.isEmpty()) {

            return stringBuilder.append("<i>Пар немає</i>").toString();
        }

        stringBuilder.append('\n');
        for (var i = 0; i < pairs.size(); i++) {

            var pair = pairs.get(i);

            if (i == 0) {

                stringBuilder.append(pair.getTime()).append('\n').append(pair);
            } else {

                var prevPair = pairs.get(i - 1);

                if (prevPair.getTime() != pair.getTime()) {
                    stringBuilder.append('\n').append(pair.getTime()).append('\n').append(pair);
                } else {
                    stringBuilder.append('\n').append(pair);
                }
            }
        }

        return stringBuilder.toString();
    }
}
