package dev.danilbel.schedule.bot.config;

import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Configuration
@PropertySource("classpath:bot.properties")
public class BotConfig {

    @Value("${bot.username}")
    String botUsername;

    @Value("${bot.token}")
    String botToken;
}
