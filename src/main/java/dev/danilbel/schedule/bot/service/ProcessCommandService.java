package dev.danilbel.schedule.bot.service;

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

    MessageService messageService;
    SendMessageService sendMessageService;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandStart(Update update) {

        return sendMessageService.makeSendMessageWithText(update, messageService.getStartMessage());
    }

    public SendMessage processCommandCurrent(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeCurrentPairMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNext(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeNextPairMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandLast(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeRemainingTimeMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandTimetable(Update update) {

        return sendMessageService.makeSendMessageWithText(update, messageService.getTimetableMessage());
    }

    public SendMessage processCommandToday(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleDayMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextDay(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleDayMessageByScheduleDateTime(
                schedule,
                dateTime.getNextWorkScheduleDateTime()
        );

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandCurrentWeek(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleWeekMessageByWeekName(
                schedule, dateTime.getWeekName()
        );

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandNextWeek(Update update) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleWeekMessageByWeekName(
                schedule, dateTime.getWeekName().getNextWeek()
        );

        return sendMessageService.makeSendMessageWithText(update, msg);
    }

    public SendMessage processCommandHelp(Update update) {

        return sendMessageService.makeSendMessageWithText(update, messageService.getHelpMessage());
    }
}
