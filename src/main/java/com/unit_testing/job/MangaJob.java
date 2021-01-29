package com.unit_testing.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MangaJob {

    private static final Logger logger = LoggerFactory.getLogger(MangaJob.class);


    @Scheduled(fixedRate = 1000 * 60)
    public void mangaSearchResult() {

        logger.info("started from the bottom now were're here");
    }

    @Scheduled(cron = "0 23 13 * * *")
    public void mangaSearchResultCron() {

        logger.info("started from the bottom now were're here");
    }
}
