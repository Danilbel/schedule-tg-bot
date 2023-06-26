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

        if (update != null && update.hasMessage() && update.getMessage().hasText()) {

            var textMessage = update.getMessage().getText();
            var command = textMessage.trim().split(" ")[0];

            if (commandService.getAllCommandList().contains(command)) {
                processCommandMessage(
                        update,
                        commandService.fromCommandName(command)
                );
            }
        }
    }

    private void processCommandMessage(Update update, Command command) {
        var sendMessage = switch (command) {
            case START -> null;
            case CURRENT -> null;
            case NEXT -> null;
            case LAST -> null;
            case TIMETABLE -> null;
            case TODAY -> null;
            case NEXT_DAY -> null;
            case CURRENT_WEEK -> null;
            case NEXT_WEEK -> null;
            case HELP -> processCommandService.processCommandHelp(update);
        };

        // temporary
        if (sendMessage != null) {
            setView(sendMessage);
        }
    }

    private void setView(SendMessage sendMessage) {
        telegramBot.sendAnswerMessage(sendMessage);
    }
}
