package com.unit_testing.service;

import com.unit_testing.model.Manga;
import com.unit_testing.model.MangaSearchResult;
import com.unit_testing.repo.MangaRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.List;

@Service
public class MangaService {

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);
    private final RestTemplate restTemplate;
    private final MangaRepo mangaRepo;
    @Value("${manga.search.url}")
    private String mangaSearchUrl;
    @Value("${manga.search.limit}")
    private Integer mangaSearchLimit;

    @Autowired
    public MangaService(RestTemplateBuilder builder, MangaRepo mangaRepo) {
        this.restTemplate = builder.build();
        this.mangaRepo = mangaRepo;
    }

    public List<Manga> getMangaByTitleFromMangaAPI(String title, String sessionId) {

        long start = new Date().getTime();

        logger.info("[ " + sessionId + " ] about to get man by title: " + title);

        if (!StringUtils.isEmpty(title)) {

            UriBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(mangaSearchUrl);

            uriBuilder.queryParam("q", title);
            uriBuilder.queryParam("limit", mangaSearchLimit);

            String url = uriBuilder.build().toString();

            logger.info("[ " + sessionId + " ] calling url: " + url);
            ResponseEntity<MangaSearchResult> response = this.restTemplate.getForEntity(url, MangaSearchResult.class);

            if (response.getStatusCodeValue() == HttpStatus.OK.value()) {
                logger.info("[ " + sessionId + " ] search returned a response for : " + title
                        + " response: " + response.getBody());

                if (response.getBody() != null) {
                    logger.info("[ " + sessionId + " ] manga found: " + response.getBody().getResults()
                            + " duration " + (new Date().getTime() - start));
                    return response.getBody().getResults();
                }
            }
        }

        logger.warn("[ " + sessionId + " ] no manga found duration " + (new Date().getTime() - start));
        return null;
    }

    public Manga add(Manga manga, String sessionId) {

        logger.info("[ " + sessionId + " ] about to save manga to mongo: " + manga);

        if (manga != null) {
            return mangaRepo.save(manga);
        }

        logger.warn("[ " + sessionId + " ] failed to save manga, null case");

        return null;
    }

    public List<Manga> findByTitle(String title, String sessionId) {
        logger.info("[ " + sessionId + " ] about to get manga by title: " + title);

        if (StringUtils.isEmpty(title)) {
            return mangaRepo.findByTitle(title);
        }

        logger.warn("[ " + sessionId + " ] manga title was emppty ");
        return null;
    }


    public List<Manga> findAllFromDb() {
        return mangaRepo.findAll();
    }


}
