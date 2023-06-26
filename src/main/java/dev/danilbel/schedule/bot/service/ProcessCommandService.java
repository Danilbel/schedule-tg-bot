package dev.danilbel.schedule.bot.service;

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

    public SendMessage processCommandHelp(Update update) {

        var msg = "<b>Команди бота:</b>\n\n";

        msg += commandService.commandListToString();

        return messageService.makeSendMessageWithText(update, msg);
    }
}
