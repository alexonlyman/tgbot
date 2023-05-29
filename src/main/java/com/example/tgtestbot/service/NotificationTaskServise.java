package com.example.tgtestbot.service;

import com.example.tgtestbot.entity.NotificationTask;
import com.example.tgtestbot.repository.NotificationTaskRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationTaskServise {
    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskServise(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }

    public void save(NotificationTask notificationTask) {
        notificationTaskRepository.save(notificationTask);
    }
}
