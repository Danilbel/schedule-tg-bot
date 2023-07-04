package dev.danilbel.schedule.bot.controller;

import dev.danilbel.schedule.bot.TelegramBot;
import dev.danilbel.schedule.bot.enums.Command;
import dev.danilbel.schedule.bot.service.CommandService;
import dev.danilbel.schedule.bot.service.ProcessCommandService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Controller
public class UpdateController {

    @NonFinal
    TelegramBot telegramBot;

    CommandService commandService;
    ProcessCommandService processCommandService;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {

        if (update != null && update.hasMessage()) {

            processMessage(update.getMessage());
        }
    }

    private void processMessage(Message message) {

        if (message.hasText()) {

            var textMessage = message.getText();
            var command = textMessage.trim().split(" ")[0];

            if (commandService.getAllCommandList().contains(command)) {
                processCommandMessage(
                        message,
                        commandService.fromCommandName(command)
                );
            }
        }
    }

    private void processCommandMessage(Message message, Command command) {
        var sendMessage = switch (command) {
            case START -> processCommandService.processCommandStart(message);
            case CURRENT -> processCommandService.processCommandCurrent(message);
            case NEXT -> processCommandService.processCommandNext(message);
            case LAST -> processCommandService.processCommandLast(message);
            case TIMETABLE -> processCommandService.processCommandTimetable(message);
            case TODAY -> processCommandService.processCommandToday(message);
            case NEXT_DAY -> processCommandService.processCommandNextDay(message);
            case CURRENT_WEEK -> processCommandService.processCommandCurrentWeek(message);
            case NEXT_WEEK -> processCommandService.processCommandNextWeek(message);
            case HELP -> processCommandService.processCommandHelp(message);
        };

        setView(sendMessage);
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
