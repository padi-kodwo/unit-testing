package com.unit_testing.service;

import com.unit_testing.model.Manga;
import com.unit_testing.model.MangaSearchResult;
import com.unit_testing.repo.MangaRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MangaServiceTest {

    @Autowired
    private MangaService mangaService;

    // MockBean is the annotation provided by Spring that wraps mockito one
    // Annotation that can be used to add mocks to a Spring ApplicationContext.
    // If any existing single bean of the same type defined in the context will be replaced by the mock,
    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private MangaRepo mangaRepo;

    private Manga manga;
    private String sessionId;


    @Before
    public void setUp() {
        Manga manga = new Manga();
        manga.setImage_url("https://cdn.myanimelist.net/images/anime/7/6803.jpg?s=378cbce9c8ed92084b54ba3240a8a290");
        manga.setAiring(false);
        manga.setEpisodes(94);
        manga.setTitle("Rurouni Kenshin: Meiji Kenkaku Romantan");
        manga.setRated("PG-13");
        manga.setScore(8.34);

        this.manga = manga;
        this.sessionId = "79577976343964DD523F6E-TEST-CASE=RUNNING";
    }

    /**
     * unit testing simulating external API call
     */
    @Test
    public void getMangaByTitle() {

        // Parsing mock file
        MangaSearchResult msr = new MangaSearchResult();
        msr.setRequest_hash("request:search:2dab57bcb96e92637b33c6cfe6f0b244b3df4ac9");
        msr.setRequest_cached(false);
        msr.setRequest_cache_expiry(432000);
        msr.setLast_page(4);

        msr.setResults(Collections.singletonList(this.manga));

        // Mocking remote service
        Mockito.when(restTemplate.getForEntity(any(String.class), any(Class.class)))
                .thenReturn(new ResponseEntity<>(msr, HttpStatus.OK));

        // I will now search for ken
        List<Manga> mangas = mangaService.getMangaByTitleFromMangaAPI("ken", sessionId);
        assert mangas != null;
        assert mangas.get(0) != null;
        assert !StringUtils.isEmpty(mangas.get(0).getTitle());
    }

    /**
     * unit test simulating data base
     */
    @Test
    public void add() {
        Mockito.when(mangaRepo.save(any(Manga.class))).thenReturn(this.manga);

        Manga manga = mangaService.add(this.manga, this.sessionId);

        assert manga == this.manga;
    }

    /**
     * database integration test coming up
     */
    @Test
    public void findByTitle() {
        long unixTime = System.currentTimeMillis() / 1000L;
        System.out.println(unixTime);
    }
}