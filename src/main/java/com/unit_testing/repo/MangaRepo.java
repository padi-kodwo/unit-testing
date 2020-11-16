package com.unit_testing.repo;

import com.unit_testing.model.Manga;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MangaRepo extends MongoRepository<Manga, String> {

    List<Manga> findByTitle(String title);

    List<Manga> findAllByTitle(String title);

    List<Manga> findByScoreGreaterThan(Double score);

}
