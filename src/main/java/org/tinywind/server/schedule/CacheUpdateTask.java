package org.tinywind.server.schedule;

import org.tinywind.server.config.CachedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@EnableScheduling
@Component
@PropertySource("classpath:application.properties")
public class CacheUpdateTask {
    private static final Logger logger = LoggerFactory.getLogger(CacheUpdateTask.class);

    @Autowired
    private SimpMessagingTemplate messenger;
    @Autowired
    private CachedEntity cachedEntity;

    @PostConstruct
    @Scheduled(fixedRateString = "${application.cache.title.update:3600}000")
    public void load() {
        logger.trace("update cache");
        messenger.convertAndSend("/heartbeat", System.currentTimeMillis());
    }
}
