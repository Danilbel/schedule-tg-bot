package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.domain.*;
import dev.danilbel.schedule.parser.service.ScheduleDateTimeParser;
import dev.danilbel.schedule.parser.service.ScheduleParser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProcessCommandService {

    MessageService messageService;
    SendMessageService sendMessageService;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandStart(Update update) {

        return sendMessageService.makeSendMessageWithText(update, messageService.getStartMessage());
    }

    public SendMessage processCommandCurrent(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        ScheduleDay scheduleCurrentDay;
        try {

            scheduleCurrentDay = getScheduleDayByScheduleDateTime(dateTime);
        } catch (IllegalArgumentException e) {

            return sendMessageService.makeSendMessageWithText(update, e.getMessage());
        }

        var timePairs = scheduleCurrentDay.getTimePairs();

        for (TimeTable timePair : timePairs) {

            if (dateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var msg = "";

                if (dateTime.getTime().isBefore(TimeTable.FIRST_PAIR.getLocalTimeOfStartTime()))
                    msg = "<b>Пари ще не почалися!</b>\n\n";
                else
                    msg = "<b>Зараз перерва!</b>\n\n";

                var remainingTime = timePair.getLocalTimeOfStartTime().minusHours(dateTime.getTime().getHour()).
                        minusMinutes(dateTime.getTime().getMinute());

                msg += String.format("Наступна пара через <i>%d год. %d хв.</i>\n",
                        remainingTime.getHour(), remainingTime.getMinute());
                msg += scheduleCurrentDay.toStringPairsByTime(timePair);

                return sendMessageService.makeSendMessageWithText(update, msg);

            } else if (dateTime.getTime().isBefore(timePair.getLocalTimeOfEndTime())) {

                var msg = "<b>Зараз пара:</b>\n\n";
                msg += scheduleCurrentDay.toStringPairsByTime(timePair);

                return sendMessageService.makeSendMessageWithText(update, msg);
            }
        }

        return sendMessageService.makeSendMessageWithText(update, "<i>Пари вже закінчилися!</i>");
    }

    public SendMessage processCommandNext(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        ScheduleDay scheduleCurrentDay;
        try {

            scheduleCurrentDay = getScheduleDayByScheduleDateTime(dateTime);
        } catch (IllegalArgumentException e) {

            return sendMessageService.makeSendMessageWithText(update, e.getMessage());
        }

        var timePairs = scheduleCurrentDay.getTimePairs();

        for (TimeTable timePair : timePairs) {

            if (dateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var remainingTime = timePair.getLocalTimeOfStartTime().minusHours(dateTime.getTime().getHour()).
                        minusMinutes(dateTime.getTime().getMinute());

                var msg = String.format("Наступна пара через <i>%d год. %d хв.</i>\n",
                        remainingTime.getHour(), remainingTime.getMinute());
                msg += scheduleCurrentDay.toStringPairsByTime(timePair);

                return sendMessageService.makeSendMessageWithText(update, msg);
            }
        }

        return sendMessageService.makeSendMessageWithText(update, "<i>Пар більше немає!</i>");
    }

    public SendMessage processCommandLast(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        ScheduleDay scheduleCurrentDay;
        try {

            scheduleCurrentDay = getScheduleDayByScheduleDateTime(dateTime);
        } catch (IllegalArgumentException e) {

            return sendMessageService.makeSendMessageWithText(update, e.getMessage());
        }

        var timePairs = scheduleCurrentDay.getTimePairs();

        for (TimeTable timePair : timePairs) {

            if (dateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var msg = "";

                if (dateTime.getTime().isBefore(TimeTable.FIRST_PAIR.getLocalTimeOfStartTime()))
                    msg = "<b>Пари ще не почалися, до наступної пари залишилося:</b>\n\n";
                else
                    msg = "<b>До кінця перерви залишилося:</b>\n\n";

                var remainingTime = timePair.getLocalTimeOfStartTime().minusHours(dateTime.getTime().getHour()).
                        minusMinutes(dateTime.getTime().getMinute()).minusSeconds(dateTime.getTime().getSecond());

                msg += String.format("<i>%d год. %d хв. %d сек.</i>",
                        remainingTime.getHour(), remainingTime.getMinute(), remainingTime.getSecond());

                return sendMessageService.makeSendMessageWithText(update, msg);

            } else if (dateTime.getTime().isBefore(timePair.getLocalTimeOfEndTime())) {

                var remainingTime = timePair.getLocalTimeOfEndTime().minusHours(dateTime.getTime().getHour()).
                        minusMinutes(dateTime.getTime().getMinute()).minusSeconds(dateTime.getTime().getSecond());

                var msg = String.format("<b>До кінця пари залишилося:</b>\n\n<i>%d год. %d хв. %d сек.</i>",
                        remainingTime.getHour(), remainingTime.getMinute(), remainingTime.getSecond());

                return sendMessageService.makeSendMessageWithText(update, msg);
            }
        }

        return sendMessageService.makeSendMessageWithText(update, "<i>Пари вже закінчилися!</i>");
    }

    private ScheduleDay getScheduleDayByScheduleDateTime(ScheduleDateTime dateTime) {

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            throw new IllegalArgumentException("<i>У неділю пар немає!</i>");
        }

        var scheduleCurrentDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(dateTime.getWeekName(), dateTime.getDayOfWeek())
                .orElse(null);

        if (scheduleCurrentDay == null || scheduleCurrentDay.getPairs().isEmpty()) {

            throw new IllegalArgumentException("<i>Сьогодні пар немає!</i>");
        }

        return scheduleCurrentDay;
    }

    public SendMessage processCommandTimetable(Update update) {

        return sendMessageService.makeSendMessageWithText(update, TimeTable.timeTableToString());
    }

    public SendMessage processCommandToday(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return sendMessageService.makeSendMessageWithText(update, "<i>У неділю пар немає!</i>");
        }

        var msg = scheduleByNameWeekAndDayOfWeekToString(dateTime.getDate(), dateTime.getWeekName(), dateTime.getDayOfWeek());

        if (msg == null) return null;

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextDay(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        var nextWorkDay = dateTime.getDayOfWeek().getNextWorkDay();

        var nextWorkDate = dateTime.getDate().plusDays(
                nextWorkDay == DayOfWeek.MONDAY
                && dateTime.getDayOfWeek() != DayOfWeek.SUNDAY
                        ? 2 : 1
        );

        var nameWeek = nextWorkDay == DayOfWeek.MONDAY
                ? dateTime.getWeekName().getNextWeek()
                : dateTime.getWeekName();

        var msg = scheduleByNameWeekAndDayOfWeekToString(nextWorkDate, nameWeek, nextWorkDay);

        if (msg == null) return null;

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    private String scheduleByNameWeekAndDayOfWeekToString(LocalDate date, WeekName weekName, DayOfWeek dayOfWeek) {

        var scheduleDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(weekName, dayOfWeek);

        return scheduleDay.map(day -> {

            var msg = String.format("<b>Пари на %s (%02d.%02d):</b>\n\n",
                    dayOfWeek.getFullNameDay().toLowerCase(),
                    date.getDayOfMonth(),
                    date.getMonthValue());

            day.sort();
            msg += day;

            return msg;
        }).orElse(null);
    }

    public SendMessage processCommandCurrentWeek(Update update) {

        var msg = scheduleByNameWeekToString(
                scheduleDateTimeParser.getScheduleDateTime()
                        .getWeekName()
        );

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextWeek(Update update) {

        var msg = scheduleByNameWeekToString(
                scheduleDateTimeParser.getScheduleDateTime()
                        .getWeekName()
                        .getNextWeek()
        );

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    private String scheduleByNameWeekToString(WeekName weekName) {

        var msg = String.format("<b>Пари на %s тиждень:</b>\n\n",
                weekName.getNameWeek().toLowerCase());

        var schedule = scheduleParser.getSchedule();
        schedule.sort();

        msg += schedule.scheduleByNameWeekToString(weekName);

        return msg;
    }

    public SendMessage processCommandHelp(Update update) {

        return sendMessageService.makeSendMessageWithText(update, messageService.getHelpMessage());
    }
}
