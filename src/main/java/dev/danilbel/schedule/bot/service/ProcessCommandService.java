package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.domain.TimeTable;
import dev.danilbel.schedule.domain.DayOfWeek;
import dev.danilbel.schedule.parser.service.ScheduleDateTimeParser;
import dev.danilbel.schedule.parser.service.ScheduleParser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProcessCommandService {

    CommandService commandService;
    MessageService messageService;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandNext(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return messageService.makeSendMessageWithText(update, "<i>У неділю пар немає!</i>");
        }

        var scheduleCurrentDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(dateTime.getNameWeek(), dateTime.getDayOfWeek());

        if (scheduleCurrentDay == null || scheduleCurrentDay.getPairs().isEmpty()) {

            return messageService.makeSendMessageWithText(update, "<i>Сьогодні пар немає!</i>");
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

    public SendMessage processCommandCurrent(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return messageService.makeSendMessageWithText(update, "<i>У неділю пар немає!</i>");
        }

        var scheduleCurrentDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(dateTime.getNameWeek(), dateTime.getDayOfWeek());

        if (scheduleCurrentDay == null || scheduleCurrentDay.getPairs().isEmpty()) {

            return messageService.makeSendMessageWithText(update, "<i>Сьогодні пар немає!</i>");
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

    public SendMessage processCommandLast(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return messageService.makeSendMessageWithText(update, "<i>У неділю пар немає!</i>");
        }

        var scheduleCurrentDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(dateTime.getNameWeek(), dateTime.getDayOfWeek());

        if (scheduleCurrentDay == null || scheduleCurrentDay.getPairs().isEmpty()) {

            return messageService.makeSendMessageWithText(update, "<i>Сьогодні пар немає!</i>");
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
    public SendMessage processCommandTimetable(Update update) {

        StringBuilder msg = new StringBuilder("<b>Розклад пар:</b>\n\n");

        for (var time : TimeTable.values()) {
            msg.append(time).append("\n");
        }

        return messageService.makeSendMessageWithText(update, msg.toString());
    }

    public SendMessage processCommandToday(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        if (dateTime.getDayOfWeek() == DayOfWeek.SUNDAY) {

            return messageService.makeSendMessageWithText(update, "<i>У неділю пар немає!</i>");
        }

        var scheduleCurrentDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(dateTime.getNameWeek(), dateTime.getDayOfWeek());

        if (scheduleCurrentDay != null) {

            var msg = String.format("<b>Пари на сьогодні (%02d.%02d):</b>\n\n",
                    dateTime.getDate().getDayOfMonth(),
                    dateTime.getDate().getMonthValue());

            scheduleCurrentDay.sort();
            msg += scheduleCurrentDay;

            return messageService.makeSendMessageWithText(update, msg);
        } else {
            return null;
        }
    }

    public SendMessage processCommandNextDay(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        var nextWorkDay = dateTime.getDayOfWeek().getNextWorkDay();

        // Если следующий рабочий день - понедельник, то перепрыгиваем воскресенье
        var nextWorkDate = dateTime.getDate().plusDays(
                nextWorkDay == DayOfWeek.MONDAY ? 2 : 1
        );

        var nameWeek = nextWorkDay == DayOfWeek.MONDAY
                ? dateTime.getNameWeek().getNextWeek()
                : dateTime.getNameWeek();

        var scheduleNextDay = scheduleParser.getSchedule().
                getScheduleDayByDayOfWeek(nameWeek, nextWorkDay);

        if (scheduleNextDay != null) {

            var msg = String.format("<b>Пари на завтра (%02d.%02d):</b>\n\n",
                    nextWorkDate.getDayOfMonth(),
                    nextWorkDate.getMonthValue());

            scheduleNextDay.sort();
            msg += scheduleNextDay;

            return messageService.makeSendMessageWithText(update, msg);
        } else {
            return null;
        }
    }

    public SendMessage processCommandCurrentWeek(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        var msg = String.format("<b>Пари на поточний (%s) тиждень:</b>\n\n",
                dateTime.getNameWeek().getNameWeek().toLowerCase());

        var schedule = scheduleParser.getSchedule();
        schedule.sort();

        msg += schedule.toStringScheduleByWeek(dateTime.getNameWeek());

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextWeek(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();

        var nextNameWeek = dateTime.getNameWeek().getNextWeek();

        var msg = String.format("<b>Пари на наступний (%s) тиждень:</b>\n\n",
                nextNameWeek.getNameWeek().toLowerCase());

        var schedule = scheduleParser.getSchedule();
        schedule.sort();

        msg += schedule.toStringScheduleByWeek(nextNameWeek);

        return messageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandHelp(Update update) {

        var msg = "<b>Команди бота:</b>\n\n";

        msg += commandService.commandListToString();

        return messageService.makeSendMessageWithText(update, msg);
    }
}
