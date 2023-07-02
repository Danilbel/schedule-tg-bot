package dev.danilbel.schedule.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class SendMessageService {

    public SendMessage makeSendMessageWithText(Update update, String text) {
        return SendMessage.builder()
                .chatId(update.getMessage().getChatId().toString())
                .text(text)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .build();
    }
}
