package dev.danilbel.schedule.parser.service;

import dev.danilbel.schedule.parser.config.ParserConfig;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ResponseApiService {

    public static final String URL_API_DATETIME = "https://worldtimeapi.org/api/timezone/Europe/Kyiv";
    public static final String URL_API_SCHEDULE = "https://schedule.kpi.ua/api/schedule/lessons?groupId=";
    public static final String URL_API_GROUP = "https://schedule.kpi.ua/api/schedule/groups";

    ParserConfig parserConfig;

    public String getResponseApiDateTime() {
        return getResponse(URL_API_DATETIME);
    }

    public String getResponseApiSchedule() {
        return getResponse(URL_API_SCHEDULE + parserConfig.getScheduleGroupId());
    }

    public String getResponseApiGroups() {
        return getResponse(URL_API_GROUP);
    }

    private String getResponse(String url) {
        try {
            var obj = new URL(url);
            var connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");
            var in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream())
            );
            String inputLine;
            var response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error get response from api", e);
        }
    }
}
