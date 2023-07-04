package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.domain.dto.Chat;
import dev.danilbel.schedule.parser.service.ScheduleDateTimeParser;
import dev.danilbel.schedule.parser.service.ScheduleParser;
import dev.danilbel.schedule.store.entity.ChatEntity;
import dev.danilbel.schedule.store.repository.ChatRepository;
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

    ChatRepository chatRepository;

    ScheduleDateTimeParser scheduleDateTimeParser;
    ScheduleParser scheduleParser;

    public SendMessage processCommandStart(Message message) {

        return sendMessageService.makeSendMessageWithText(message, messageService.getStartMessage());
    }

    public SendMessage processCommandBind(Message message) {

        if (message.getText().trim().split(" ").length < 2) {
            return sendMessageService.makeSendMessageWithText(message, "<i>Помилка! Не вказана група</i>");
        }

        var groupName = message.getText().trim().split(" ")[1];

        var groupList = scheduleParser.getGroups();

        var group = groupList.stream()
                .filter(g -> g.getName().equalsIgnoreCase(groupName))
                .findFirst();

        if (group.isPresent()) {

            var chatEntity = ChatEntity.builder()
                    .id(message.getChatId())
                    .groupName(group.get().getName())
                    .groupId(group.get().getId())
                    .build();

            chatRepository.saveAndFlush(chatEntity);

            var msg = String.format("<i>Група %s успішно обрана!</i>", group.get().getName());

            return sendMessageService.makeSendMessageWithText(message, msg);
        }

        return sendMessageService.makeSendMessageWithText(message, "<i>Помилка! Групу не знайдено (</i>");
    }

    public SendMessage processCommandCurrent(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeCurrentPairMessageByScheduleDateTime(schedule, dateTime, chat.getGroupName());

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    private Chat getChatOrThrow(Long chatId) {

        return chatRepository.findById(chatId)
                .map(Chat::makeDto)
                .orElseThrow(() -> new IllegalArgumentException("<b>В чаті не обрана група!</b>\n" +
                        "Використайте команду /bind <i>назва групи</i> для вибору групи"));
    }

    public SendMessage processCommandNext(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeNextPairMessageByScheduleDateTime(schedule, dateTime, chat.getGroupName());

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandLast(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeRemainingTimeMessageByScheduleDateTime(schedule, dateTime, chat.getGroupName());

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandTimetable(Message message) {

        return sendMessageService.makeSendMessageWithText(message, messageService.getTimetableMessage());
    }

    public SendMessage processCommandToday(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeScheduleDayMessageByScheduleDateTime(schedule, dateTime, chat.getGroupName());

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandNextDay(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeScheduleDayMessageByScheduleDateTime(
                schedule,
                dateTime.getNextWorkScheduleDateTime(),
                chat.getGroupName()
        );

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandCurrentWeek(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeScheduleWeekMessageByWeekName(
                schedule, dateTime.getWeekName(),
                chat.getGroupName()
        );

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandNextWeek(Message message) {

        var chat = getChatOrThrow(message.getChatId());

        var dateTime = scheduleDateTimeParser.getScheduleDateTime();
        var schedule = scheduleParser.getSchedule(chat.getGroupId());

        var msg = messageService.makeScheduleWeekMessageByWeekName(
                schedule, dateTime.getWeekName().getNextWeek(),
                chat.getGroupName()
        );

        return sendMessageService.makeSendMessageWithText(message, msg);
    }

    public SendMessage processCommandHelp(Message message) {

        return sendMessageService.makeSendMessageWithText(message, messageService.getHelpMessage());
    }
}
