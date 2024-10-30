package com.gamesstore.gamestoreweb.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Genre is required")
    private String genre;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be greater than or equal to 0")
    private double price;

    @NotBlank(message = "Developer is required")
    private String developer;

    @NotBlank(message = "Release date is required")
    private String releaseDate;

    private String imageUrl; // New field for image URL

    // Constructor that accepts all fields except the ID
    public Game(String title, String genre, double price, String developer, String releaseDate, String imageUrl) {
        this.title = title;
        this.genre = genre;
        this.price = price;
        this.developer = developer;
        this.releaseDate = releaseDate;
        this.imageUrl = imageUrl;
    }

    // Default constructor (required by JPA)
    public Game() {
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDeveloper() { return developer; }
    public void setDeveloper(String developer) { this.developer = developer; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getImageUrl() { return imageUrl; } // New getter
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; } // New setter
}
