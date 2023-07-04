package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.parser.service.ScheduleDateTimeParser;
import dev.danilbel.schedule.parser.service.ScheduleParser;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProcessCommandService {

    MessageService messageService;
    SendMessageService sendMessageService;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandStart(Message message) {

        return sendMessageService.makeSendMessageWithText(message, messageService.getStartMessage());
    }

    public SendMessage processCommandCurrent(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeCurrentPairMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandNext(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeNextPairMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandLast(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeRemainingTimeMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandTimetable(Message message) {

        return sendMessageService.makeSendMessageWithText(message, messageService.getTimetableMessage());
    }

    public SendMessage processCommandToday(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleDayMessageByScheduleDateTime(schedule, dateTime);

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandNextDay(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleDayMessageByScheduleDateTime(
                schedule,
                dateTime.getNextWorkScheduleDateTime()
        );

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandCurrentWeek(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleWeekMessageByWeekName(
                schedule, dateTime.getWeekName()
        );

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandNextWeek(Message message) {

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule();

        var msg = messageService.makeScheduleWeekMessageByWeekName(
                schedule, dateTime.getWeekName().getNextWeek()
        );

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandHelp(Message message) {

        return sendMessageService.makeSendMessageWithText(message, messageService.getHelpMessage());
    }
}
