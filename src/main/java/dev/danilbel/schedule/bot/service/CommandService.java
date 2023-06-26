package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.bot.enums.Command;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CommandService {

    public List<BotCommand> getBotCommandList() {

        var commands = new ArrayList<BotCommand>();

        for (var command : Command.values())
            commands.add(new BotCommand(
                    command.getCommand(),
                    command.getDescription())
            );

        return commands;
    }
}
