package dev.danilbel.schedule.parser.service;

import com.google.gson.Gson;
import dev.danilbel.schedule.domain.ScheduleDay;
import dev.danilbel.schedule.domain.SchedulePair;
import dev.danilbel.schedule.domain.NameWeek;
import dev.danilbel.schedule.domain.TimeTable;
import dev.danilbel.schedule.domain.DayOfWeek;
import dev.danilbel.schedule.parser.response.ScheduleApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ScheduleParser {

    ResponseApiService responseApiService;

    public List<ScheduleDay> getScheduleByWeek(NameWeek scheduleWeek) {

        var json = responseApiService.getResponseApiSchedule();

        Gson gson = new Gson();
        var scheduleApiResponse = gson.fromJson(json, ScheduleApiResponse.class);

        var scheduleDataResponse = scheduleApiResponse.getData();

        var schedule = scheduleWeek == NameWeek.FIRST_WEEK
                ? scheduleDataResponse.getScheduleFirstWeek()
                : scheduleDataResponse.getScheduleSecondWeek();

        var scheduleDayList = new ArrayList<ScheduleDay>();

        for (var scheduleDayResponse : schedule) {

            var scheduleDay = ScheduleDay.builder()
                    .day(DayOfWeek.fromValue(scheduleDayResponse.getDay())).build();

            var schedulePairList = new ArrayList<SchedulePair>();

            for (var schedulePairResponse : scheduleDayResponse.getPairs()) {

                var schedulePair = SchedulePair.builder()
                        .teacherName(schedulePairResponse.getTeacherName())
                        .type(schedulePairResponse.getType())
                        .time(TimeTable.fromValue(schedulePairResponse.getTime()))
                        .name(schedulePairResponse.getName())
                        .place(schedulePairResponse.getPlace())
                        .build();

                schedulePairList.add(schedulePair);
            }

            scheduleDay.setPairs(schedulePairList);
            scheduleDayList.add(scheduleDay);
        }

        return scheduleDayList;
    }
}
