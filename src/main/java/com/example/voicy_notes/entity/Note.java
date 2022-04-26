package com.example.voicy_notes.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "t_note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String text;
    private Date lastModified;
    private boolean isPublic;
    private String str;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Note(String name, String text, Date lastModified, User user, boolean isPublic, String str) {
        this.name = name;
        this.text = text;
        this.lastModified = lastModified;
        this.user = user;
        this.isPublic = isPublic;
        this.str = str;
    }

    public Note() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
