package com.example.tgtestbot.listner;

import com.example.tgtestbot.entity.NotificationTask;
import com.example.tgtestbot.service.NotificationTaskServise;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBotsUpdateListner implements UpdatesListener {
    private final TelegramBot telegramBot;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotsUpdateListner.class);
    private final Pattern pattern = Pattern.compile("(\\d{1,2}\\.\\d{1,2}\\.\\d{4} \\d{1,2}\\:\\d{2})\\s+([А-я\\d\\s,.!?;]+)");
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final NotificationTaskServise notificationTaskServise;



public TelegramBotsUpdateListner(TelegramBot telegramBot,NotificationTaskServise notificationTaskServise) {
    this.telegramBot = telegramBot;
    this.notificationTaskServise = notificationTaskServise;
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
                    sendMessage(chatId,"""
                    Привет, помогу запланировать задачу, введи дату,
                    время и задачу в формате 11.12.2023 20:00 сдать бота на проверку""");

                    } else if (text != null) {
                        Matcher matcher = pattern.matcher(text);
                    if (matcher.find()) {
                        LocalDateTime dateTime = parse(matcher.group(1));
                        if (Objects.isNull(dateTime)) {
                            sendMessage(chatId, "некорретный формат даты или времени");
                        } else {
                            String txt = matcher.group(2);
                            NotificationTask notificationTask = new NotificationTask();
                            notificationTask.setChatId(chatId);
                            notificationTask.setMessage(txt);
                            notificationTask.setNotificationDateTime(dateTime);
                            notificationTaskServise.save(notificationTask);
                            sendMessage(chatId,"успешно сохранено");
                        }

                    } else {
                        sendMessage(chatId,"некорректный формат");
                    }
                    }
            });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
@Nullable
    private LocalDateTime parse(String dateTime) {
        try {
            return LocalDateTime.parse(dateTime, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse sendResponse = telegramBot.execute(sendMessage);
        if (!sendResponse.isOk()) {
            logger.error("Error", sendResponse.description());
        }
    }
}
