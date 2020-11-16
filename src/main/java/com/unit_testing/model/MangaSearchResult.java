package com.unit_testing.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MangaSearchResult implements Serializable {

    private static final long serialVersionUID = 1234L;

    private List<Manga> results;
    private String request_hash;
    private Boolean request_cached;
    private Integer request_cache_expiry;
    private Integer last_page;

    @Override
    public String toString() {
        return "MangaSearchResult{" +
                "results=" + results +
                ", request_hash='" + request_hash + '\'' +
                ", request_cached=" + request_cached +
                ", request_cache_expiry=" + request_cache_expiry +
                ", last_page=" + last_page +
                '}';
    }
}
