package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.domain.ScheduleDay;
import dev.danilbel.schedule.domain.ScheduleWeek;
import dev.danilbel.schedule.domain.TimeTable;
import dev.danilbel.schedule.domain.WeekDay;
import dev.danilbel.schedule.parser.service.ScheduleDateTimeParser;
import dev.danilbel.schedule.parser.service.ScheduleParser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProcessCommandService {

    CommandService commandService;
    MessageService messageService;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandTimetable(Update update) {

        StringBuilder msg = new StringBuilder("<b>Розклад пар:</b>\n\n");

        for (var time : TimeTable.values()) {
            msg.append(time).append("\n");
        }

        return messageService.makeSendMessageWithText(update, msg.toString());
    }

    public SendMessage processCommandToday(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        var msg = String.format("<b>Пари на сьогодні (%s %02d.%02d):</b>\n\n",
                dateTime.getWeekDay().getFullNameDay().toLowerCase(),
                dateTime.getDate().getDayOfMonth(),
                dateTime.getDate().getMonthValue());

        var scheduleWeek = scheduleParser.getScheduleByWeek(dateTime.getScheduleWeek());

        for (var scheduleDay : scheduleWeek) {

            if (scheduleDay.getDay() == dateTime.getWeekDay()) {

                msg += scheduleDay.toString();
                break;
            }
        }

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextDay(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        var weekDay = dateTime.getWeekDay().getNextWorkDay();

        var date = dateTime.getDate().plusDays(1);

        var msg = String.format("<b>Пари на завтра (%s %02d.%02d):</b>\n\n",
                weekDay.getFullNameDay().toLowerCase(),
                date.getDayOfMonth(),
                date.getMonthValue());

        var scheduleWeek = weekDay == WeekDay.MONDAY
                ? dateTime.getScheduleWeek().getNextWeek()
                : dateTime.getScheduleWeek();

        var schedule = scheduleParser.getScheduleByWeek(scheduleWeek);

        for (var scheduleDay : schedule) {

            if (scheduleDay.getDay() == weekDay) {

                msg += scheduleDay.toString();
                break;
            }
        }

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandCurrentWeek(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getScheduleByWeek(dateTime.getScheduleWeek());

        var msg = scheduleToString(dateTime.getScheduleWeek(), schedule);

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextWeek(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var scheduleWeek = dateTime.getScheduleWeek() == ScheduleWeek.FIRST_WEEK
                ? ScheduleWeek.SECOND_WEEK
                : ScheduleWeek.FIRST_WEEK;
        var schedule = scheduleParser.getScheduleByWeek(scheduleWeek);

        var msg = scheduleToString(scheduleWeek, schedule);

        return messageService.makeSendMessageWithText(update, msg);
    }

    private String scheduleToString(ScheduleWeek scheduleWeek, List<ScheduleDay> schedule) {

        var msg = new StringBuilder(String.format("<b>Пари на поточний (%s) тиждень:</b>\n\n",
                scheduleWeek.getNameWeek().toLowerCase()));

        for (var weekDay : WeekDay.values()) {

            if (weekDay == WeekDay.SUNDAY) {
                continue;
            }

            for (var scheduleDay : schedule) {

                if (scheduleDay.getDay() == weekDay) {
                    msg.append("<b>• ").append(weekDay.getFullNameDay()).append(":</b>\n");
                    msg.append(scheduleDay).append("\n\n");
                    break;
                }
            }
        }

        return msg.toString();
    }

    public SendMessage processCommandHelp(Update update) {

        var msg = "<b>Команди бота:</b>\n\n";

        msg += commandService.commandListToString();

        return messageService.makeSendMessageWithText(update, msg);
    }
}
