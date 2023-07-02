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
}
