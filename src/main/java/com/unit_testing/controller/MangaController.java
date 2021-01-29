package com.unit_testing.controller;

import com.unit_testing.model.Manga;
import com.unit_testing.service.MangaService;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@ResponseBody
@RequestMapping("/api/v1/manga")
public class MangaController {

    private static final Logger logger = LoggerFactory.getLogger(MangaController.class);

    private final MangaService mangaService;


    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }

    @Async
    @GetMapping(value = "/async/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CompletableFuture<List<Manga>> asyncSearch(@PathVariable(name = "title") String title,
                                                      HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        List<Manga> mangaList = mangaService.getMangaByTitleFromMangaAPI(title, sessionId);

        mangaList.parallelStream().forEach(manga -> {
            manga = mangaService.add(manga, sessionId);
        });

        return CompletableFuture.completedFuture(mangaService.getMangaByTitleFromMangaAPI(title, sessionId));
    }


    @GetMapping(value = "/sync/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Manga> syncSearch(@PathVariable(name = "title") String title,
                                  HttpServletRequest httpServletRequest) {
        String sessionId = httpServletRequest.getSession().getId();

        logger.info("[ " + sessionId + " ] about to get man by title: " + title);

        List<Manga> mangaList = mangaService.getMangaByTitleFromMangaAPI(title, sessionId);

        List<Manga> cleanedManga = mangaList.parallelStream().filter(manga -> manga != null && manga.getEpisodes() > 100)
                .collect(Collectors.toList());

        mangaList.parallelStream().forEach(manga -> {
            LocalTime localTime = LocalTime.now();

            if (manga.getEpisodes() > 100) {
                logger.info("[ " + sessionId + " ] : " + localTime);
                manga.setTime(localTime);
                mangaService.add(manga, sessionId);
                cleanedManga.add(manga);
            }
        });


        //mangaList = mangaService.findAllFromDb();

        logger.info("[ " + sessionId + " ] found manga in db " + mangaList.toString());


        return mangaList;
    }

    @GetMapping(value = "/error/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Manga> errorSearch(@PathVariable(name = "title") String title,
                                   HttpServletRequest httpServletRequest) {

        String sessionId = httpServletRequest.getSession().getId();

        logger.info("[ " + sessionId + " ] about to get error by title: " + title);

        List<Manga> mangaList = null;

        List<Manga> cleanedManga = mangaList.parallelStream().filter(manga -> manga.getEpisodes() > 100)
                .collect(Collectors.toList());

        mangaList.parallelStream().forEach(manga -> {
            if (manga.getEpisodes() > 100) {
                cleanedManga.add(manga);
            }
        });


        return mangaList;
    }
}
