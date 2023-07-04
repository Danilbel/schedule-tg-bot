package dev.danilbel.schedule.parser.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.danilbel.schedule.domain.Group;
import dev.danilbel.schedule.domain.Schedule;
import dev.danilbel.schedule.domain.TimeTable;
import dev.danilbel.schedule.domain.DayOfWeek;
import dev.danilbel.schedule.parser.adapter.DayOfWeekAdapter;
import dev.danilbel.schedule.parser.adapter.TimeTableAdapter;
import dev.danilbel.schedule.parser.response.GroupApiResponse;
import dev.danilbel.schedule.parser.response.ScheduleApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ScheduleParser {

    ResponseApiService responseApiService;

    DayOfWeekAdapter dayOfWeekAdapter;
    TimeTableAdapter timeTableAdapter;

    public Schedule getSchedule(String groupId) {

        var json = responseApiService.getResponseApiSchedule(groupId);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(DayOfWeek.class, dayOfWeekAdapter)
                .registerTypeAdapter(TimeTable.class, timeTableAdapter)
                .create();

        var scheduleApiResponse = gson.fromJson(json, ScheduleApiResponse.class);

        return scheduleApiResponse.getSchedule();
    }

    public List<Group> getGroups() {

        var json = responseApiService.getResponseApiGroups();

        Gson gson = new GsonBuilder()
                .create();

        var groupApiResponse = gson.fromJson(json, GroupApiResponse.class);

        return groupApiResponse.getData();
    }
}
