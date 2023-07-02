package dev.danilbel.schedule.domain.service;

import dev.danilbel.schedule.domain.ScheduleDay;
import dev.danilbel.schedule.domain.SchedulePair;
import dev.danilbel.schedule.domain.TimeTable;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ScheduleDayService {

    SchedulePairService schedulePairService;

    public List<TimeTable> getSortedTimePairs(ScheduleDay day) {

        return day.getPairs().stream()
                .map(SchedulePair::getTime)
                .collect(Collectors.toSet())
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public String pairsByTimeToString(ScheduleDay day, TimeTable time) {

        var stringBuilder = new StringBuilder(time.toString());

        for (var pair : day.getPairs()) {
            if (pair.getTime() == time) {
                stringBuilder.append('\n')
                        .append(schedulePairService.schedulePairToString(pair));
            }
        }

        return stringBuilder.toString();
    }

    public String scheduleDayToString(ScheduleDay day) {

        var stringBuilder = new StringBuilder("<b>• ").append(day.getDay().getFullNameDay()).append(": </b>");

        var pairs = day.getPairs();

        if (pairs == null || pairs.isEmpty()) {

            return stringBuilder.append("<i>Пар немає</i>").toString();
        }

        stringBuilder.append('\n');
        for (var i = 0; i < pairs.size(); i++) {

            var pair = pairs.get(i);

            if (i == 0) {

                stringBuilder.append(pair.getTime()).append('\n')
                        .append(schedulePairService.schedulePairToString(pair));
            } else {

                var prevPair = pairs.get(i - 1);

                if (prevPair.getTime() != pair.getTime()) {
                    stringBuilder.append('\n').append(pair.getTime()).append('\n')
                            .append(schedulePairService.schedulePairToString(pair));
                } else {
                    stringBuilder.append('\n')
                            .append(schedulePairService.schedulePairToString(pair));
                }
            }
        }

        return stringBuilder.toString();
    }
}
