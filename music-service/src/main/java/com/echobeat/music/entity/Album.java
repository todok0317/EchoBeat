package com.echobeat.music.entity;

import com.echobeat.music.enums.AlbumType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;

// Album.java - 앨범 정보 (향후 확장용)
@Entity
@Table(name = "albums")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "album_type")
    @Enumerated(EnumType.STRING)
    private AlbumType albumType;       // SINGLE, EP, ALBUM

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;
}
