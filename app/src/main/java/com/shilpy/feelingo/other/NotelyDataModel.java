package com.shilpy.feelingo.other;

/**
 * This is the model class of Notes.
 * This Model class is used to hold data of each row of the table.
 */

public class NotelyDataModel {
    public static final String TYPE_STORY="story";
    public static final String TYPE_POEM="poem";
    private int id;
    private String title;
    private String content;
    private String createdAt;
    private boolean favourite;
    private boolean hearted;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isHearted() {
        return hearted;
    }

    public void setHearted(boolean hearted) {
        this.hearted = hearted;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
