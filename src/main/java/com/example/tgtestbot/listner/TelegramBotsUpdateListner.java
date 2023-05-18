package com.example.tgtestbot.listner;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TelegramBotsUpdateListner implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotsUpdateListner.class);

    public TelegramBotsUpdateListner(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);

    }

    @Override
    public int process(List<Update> updates) {
        try {
            updates.forEach(update -> {
                logger.info("Handless update{}", update);
                Message message = update.message();
                Long chatId = message.chat().id();
                String text = message.text();

                if ("/Start".equals(text)) {
                    SendMessage sendMessage = new SendMessage(chatId, "H1");
                    SendResponse sendResponse = telegramBot.execute(sendMessage);
                    if (!sendResponse.isOk()) {
                        logger.error("Error", sendResponse.description());

                    }
                }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
