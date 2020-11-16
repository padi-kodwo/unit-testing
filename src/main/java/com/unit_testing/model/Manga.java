package com.unit_testing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class Manga {

    private String title;
    private String image_url;
    private Boolean airing;
    private String synopsis;
    private Integer episodes;
    private String rated;
    private Double score;
    private Integer mal_id;
    private String url;
    private String type;
    private Date start_date;
    private Date end_date;
    private Long members;

    @Id
    @Field("_id")
    @Indexed
    @JsonIgnore
    private ObjectId id;

    @Indexed
    @Field("_created")
    @CreatedDate
    @JsonIgnore
    private Date created;

    @Indexed
    @Field("_modified")
    @LastModifiedDate
    @JsonIgnore
    private Date modified;

    @JsonProperty("id")
    public String getId() {
        return (id != null) ? id.toHexString() : null;
    }


    @Override
    public String toString() {
        return "Manga{" +
                "title='" + title + '\'' +
                ", image_url='" + image_url + '\'' +
                ", airing=" + airing +
                ", synopsis='" + synopsis + '\'' +
                ", episodes=" + episodes +
                ", rated='" + rated + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Manga manga = (Manga) o;
        return Objects.equals(title, manga.title) &&
                Objects.equals(image_url, manga.image_url) &&
                Objects.equals(airing, manga.airing) &&
                Objects.equals(synopsis, manga.synopsis) &&
                Objects.equals(episodes, manga.episodes) &&
                Objects.equals(rated, manga.rated) &&
                Objects.equals(score, manga.score) &&
                Objects.equals(mal_id, manga.mal_id) &&
                Objects.equals(url, manga.url) &&
                Objects.equals(type, manga.type) &&
                Objects.equals(start_date, manga.start_date) &&
                Objects.equals(end_date, manga.end_date) &&
                Objects.equals(members, manga.members) &&
                Objects.equals(id, manga.id) &&
                Objects.equals(created, manga.created) &&
                Objects.equals(modified, manga.modified);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, image_url, airing, synopsis, episodes, rated, score, mal_id, url, type, start_date, end_date, members, id, created, modified);
    }
}

