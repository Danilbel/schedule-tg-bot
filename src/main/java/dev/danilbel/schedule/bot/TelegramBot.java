package dev.danilbel.schedule.bot;

import dev.danilbel.schedule.bot.config.BotConfig;
import dev.danilbel.schedule.bot.controller.UpdateController;
import dev.danilbel.schedule.bot.service.CommandService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Component
public class TelegramBot extends TelegramLongPollingBot {

    BotConfig config;

    UpdateController updateController;

    CommandService commandService;

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
        setCommands();
    }

    @Override
    public String getBotUsername() {
        return config.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateController.processUpdate(update);
    }

    private void setCommands() {
        var commands = commandService.getBotCommandList();

        try {
            execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
