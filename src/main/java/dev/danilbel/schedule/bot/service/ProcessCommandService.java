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

    CommandService commandService;
    MessageService messageService;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandStart(Update update) {

        var msg = "<b>Хеййоу!\n" +
                  "Я бот, який допоможе з розкладом пар КПІ!</b>\n\n" +
                  "Які пари на поточний та наступний тиждень, на сьогодні та наступний робочий день. " +
                  "Яка пара зараз та яка наступна. Скільки часу залишилось до кінця пари або перерви. " +
                  "Це все я дізнаюсь з сайту " +
                  "<a href=\"https://schedule.kpi.ua/\">розкладу КПІ</a> " +
                  "та надам тобі відповідь.\n\n" +
                  "– Користуйся мною у особистих повідомленнях або додай до чату групи.\n" +
                  "– Дізнайся про всі команди бота: /help\n\n" +
                  "<i>v1.0.0 (beta)\n" +
                  "Бот працює за умови роботи API сайту https://schedule.kpi.ua/api\n" +
                  "Автор боту: @danillbel</i>";

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandCurrent(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        ScheduleDay scheduleCurrentDay;
        try {

            scheduleCurrentDay = getScheduleDayByScheduleDateTime(dateTime);
        } catch (IllegalArgumentException e) {

            return messageService.makeSendMessageWithText(update, e.getMessage());
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

                return messageService.makeSendMessageWithText(update, msg);

            } else if (dateTime.getTime().isBefore(timePair.getLocalTimeOfEndTime())) {

                var msg = "<b>Зараз пара:</b>\n\n";
                msg += scheduleCurrentDay.toStringPairsByTime(timePair);

                return messageService.makeSendMessageWithText(update, msg);
            }
        }

        return messageService.makeSendMessageWithText(update, "<i>Пари вже закінчилися!</i>");
    }

    public SendMessage processCommandNext(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        ScheduleDay scheduleCurrentDay;
        try {

            scheduleCurrentDay = getScheduleDayByScheduleDateTime(dateTime);
        } catch (IllegalArgumentException e) {

            return messageService.makeSendMessageWithText(update, e.getMessage());
        }

        var timePairs = scheduleCurrentDay.getTimePairs();

        for (TimeTable timePair : timePairs) {

            if (dateTime.getTime().isBefore(timePair.getLocalTimeOfStartTime())) {

                var remainingTime = timePair.getLocalTimeOfStartTime().minusHours(dateTime.getTime().getHour()).
                        minusMinutes(dateTime.getTime().getMinute());

                var msg = String.format("Наступна пара через <i>%d год. %d хв.</i>\n",
                        remainingTime.getHour(), remainingTime.getMinute());
                msg += scheduleCurrentDay.toStringPairsByTime(timePair);

                return messageService.makeSendMessageWithText(update, msg);
            }
        }

        return messageService.makeSendMessageWithText(update, "<i>Пар більше немає!</i>");
    }

    public SendMessage processCommandLast(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        ScheduleDay scheduleCurrentDay;
        try {

            scheduleCurrentDay = getScheduleDayByScheduleDateTime(dateTime);
        } catch (IllegalArgumentException e) {

            return messageService.makeSendMessageWithText(update, e.getMessage());
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

                return messageService.makeSendMessageWithText(update, msg);

            } else if (dateTime.getTime().isBefore(timePair.getLocalTimeOfEndTime())) {

                var remainingTime = timePair.getLocalTimeOfEndTime().minusHours(dateTime.getTime().getHour()).
                        minusMinutes(dateTime.getTime().getMinute()).minusSeconds(dateTime.getTime().getSecond());

                var msg = String.format("<b>До кінця пари залишилося:</b>\n\n<i>%d год. %d хв. %d сек.</i>",
                        remainingTime.getHour(), remainingTime.getMinute(), remainingTime.getSecond());

                return messageService.makeSendMessageWithText(update, msg);
            }
        }

        return messageService.makeSendMessageWithText(update, "<i>Пари вже закінчилися!</i>");
    }

    private ScheduleDay getScheduleDayByScheduleDateTime(ScheduleDateTime dateTime) {

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            throw new IllegalArgumentException("<i>У неділю пар немає!</i>");
        }

        var scheduleCurrentDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(dateTime.getNameWeek(), dateTime.getDayOfWeek())
                .orElse(null);

        if (scheduleCurrentDay == null || scheduleCurrentDay.getPairs().isEmpty()) {

            throw new IllegalArgumentException("<i>Сьогодні пар немає!</i>");
        }

        return scheduleCurrentDay;
    }

    public SendMessage processCommandTimetable(Update update) {

        return messageService.makeSendMessageWithText(update, TimeTable.timeTableToString());
    }

    public SendMessage processCommandToday(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return messageService.makeSendMessageWithText(update, "<i>У неділю пар немає!</i>");
        }

        var msg = scheduleByNameWeekAndDayOfWeekToString(dateTime.getDate(), dateTime.getNameWeek(), dateTime.getDayOfWeek());

        if (msg == null) return null;

        return messageService.makeSendMessageWithText(update, msg);
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
                ? dateTime.getNameWeek().getNextWeek()
                : dateTime.getNameWeek();

        var msg = scheduleByNameWeekAndDayOfWeekToString(nextWorkDate, nameWeek, nextWorkDay);

        if (msg == null) return null;

        return messageService.makeSendMessageWithText(update, msg);
    }

    private String scheduleByNameWeekAndDayOfWeekToString(LocalDate date, NameWeek nameWeek, DayOfWeek dayOfWeek) {

        var scheduleDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(nameWeek, dayOfWeek);

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
                        .getNameWeek()
        );

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextWeek(Update update) {

        var msg = scheduleByNameWeekToString(
                scheduleDateTimeParser.getScheduleDateTime()
                        .getNameWeek()
                        .getNextWeek()
        );

        return messageService.makeSendMessageWithText(update, msg);
    }

    private String scheduleByNameWeekToString(NameWeek nameWeek) {

        var msg = String.format("<b>Пари на %s тиждень:</b>\n\n",
                nameWeek.getNameWeek().toLowerCase());

        var schedule = scheduleParser.getSchedule();
        schedule.sort();

        msg += schedule.scheduleByNameWeekToString(nameWeek);

        return msg;
    }

    public SendMessage processCommandHelp(Update update) {

        return messageService.makeSendMessageWithText(update, commandService.commandListToString());
    }
}
