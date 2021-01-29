package com.unit_testing.controller;

import com.unit_testing.model.Manga;
import com.unit_testing.service.MangaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@SpringBootTest
@RunWith(SpringRunner.class)
public class MangaControllerTest {

    @Autowired
    protected WebApplicationContext webApplicationContext;
    MockMvc mockMvc;

    @Autowired
    MangaController mangaController;

    @SpyBean
    private MangaService mangaService;

    private List<Manga> mangas;


    @Before
    public void setUp() throws Exception {
        this.mangas = new ArrayList<>();
        this.mockMvc = standaloneSetup(this.mangaController).build(); // Standalone context
        Manga manga = Manga.builder()
                .title("Naruto")
                .airing(false)
                .synopsis("Moments prior to Naruto Uzumaki's birth, a huge demon known as the Kyuubi, the " +
                        "Nine-Tailed Fox, attacked Konohagakure, the Hidden Leaf Village, and wreaked havoc. " +
                        "In order to put an end to the Kyuubi'...")
                .build();

        this.mangas.add(manga);

        manga = Manga.builder().title("Naruto: Shippuuden")
                .synopsis("It has been two and a half years since Naruto Uzumaki left Konohagakure, " +
                        "the Hidden Leaf Village, for intense training following events which fueled " +
                        "his desire to be stronger. Now Akatsuki, the myster...")
                .airing(false)
                .build();

        this.mangas.add(manga);
    }

    @Test
    public void asyncSearch() throws Exception {
        // Mocking service

        Mockito.when(mangaService.getMangaByTitleFromMangaAPI(any(String.class), any(String.class)))
                .thenReturn(this.mangas);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/manga/async/Naruto")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();


        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void syncSearch() throws Exception {
        // Mocking service
        // Mockito.when(this.mangaService.getMangaByTitleFromMangaAPI(any(String.class), any(String.class)))
        //          .thenReturn(this.mangas);

        MvcResult mvcResult = this.mockMvc.perform(get("/api/v1/manga/sync/Naruto")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


        assert "name" == "name";

        assert !StringUtils.isEmpty(mvcResult.getResponse().getContentAsString());

    }
}