package dev.danilbel.schedule.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleDay {

    WeekDay day;

    List<SchedulePair> pairs;

    @Override
    public String toString() {
        var stringBuilderDay = new StringBuilder();

        var count = 0;
        for (var timeTable : TimeTable.values()) {

            var countPair = 0;
            var stringBuilderPairs = new StringBuilder();
            for (var pair : pairs) {
                if (pair.getTime() == timeTable) {
                    stringBuilderPairs.append('\n').append(pair);

                    countPair++;
                }
            }

            if (countPair > 0) {

                if (count > 0) stringBuilderDay.append('\n');

                stringBuilderDay.append(timeTable).append(stringBuilderPairs);

                count++;
            }
        }

        if (count == 0) {
            stringBuilderDay.append("<i>Пар немає</i>");
        }

        return stringBuilderDay.toString();
    }
}
