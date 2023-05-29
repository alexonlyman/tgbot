package com.example.tgtestbot.config;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class TelegramBotConfig {


    @Bean
    public TelegramBot telegramBot(@Value("${Telegram.bot.token}") String token) {
        return new TelegramBot(token);
    }
}
