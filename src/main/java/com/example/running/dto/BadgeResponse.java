package com.example.running.dto;

public class BadgeResponse {

    private Long id;
    private String name;
    private int requiredLikes;

    public BadgeResponse() {}

    public BadgeResponse(Long id, String name, int requiredLikes) {
        this.id = id;
        this.name = name;
        this.requiredLikes = requiredLikes;
    }

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequiredLikes() {
        return requiredLikes;
    }

    public void setRequiredLikes(int requiredLikes) {
        this.requiredLikes = requiredLikes;
    }
}
