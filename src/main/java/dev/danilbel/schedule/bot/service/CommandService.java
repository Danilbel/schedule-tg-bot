package dev.danilbel.schedule.bot.service;

import dev.danilbel.schedule.bot.config.BotConfig;
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

    BotConfig botConfig;

    public List<BotCommand> getBotCommandList() {

        var commands = new ArrayList<BotCommand>();

        for (var command : Command.values())
            commands.add(new BotCommand(
                    command.getCommand(),
                    command.getDescription())
            );

        return commands;
    }

    public List<String> getAllCommandList() {
        var commands = new ArrayList<String>();

        for (Command command : Command.values()) {
            commands.add(command.getCommand());
            commands.add(getFullCommand(command));
        }

        return commands;
    }

    private String getFullCommand(Command command) {
        return command.getCommand() + "@" + botConfig.getBotUsername();
    }

    public Command fromCommandName(String commandName) {
        for (Command command : Command.values()) {
            if (command.getCommand().equals(commandName) || getFullCommand(command).equals(commandName)) {
                return command;
            }
        }
        throw new IllegalArgumentException("Invalid value for Command: " + commandName);
    }

    public String commandListToString() {

        var stringBuilder = new StringBuilder();

        for (var command : Command.values()) {
            stringBuilder.append(command.getCommand())
                    .append(" – ")
                    .append(command.getDescription())
                    .append("\n");
        }

        return stringBuilder.toString();
    }
}
