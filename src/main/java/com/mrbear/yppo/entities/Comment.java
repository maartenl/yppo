package com.mrbear.yppo.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Comment")
public class Comment {
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @jakarta.persistence.Column(name = "id")
  private long id;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @jakarta.persistence.Column(name = "galleryphotograph_id")
  private long galleryphotographId;

  public long getGalleryphotographId() {
    return galleryphotographId;
  }

  public void setGalleryphotographId(long galleryphotographId) {
    this.galleryphotographId = galleryphotographId;
  }

  @Basic
  @jakarta.persistence.Column(name = "author")
  private String author;

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  @Basic
  @jakarta.persistence.Column(name = "submitted")
  private Timestamp submitted;

  public Timestamp getSubmitted() {
    return submitted;
  }

  public void setSubmitted(Timestamp submitted) {
    this.submitted = submitted;
  }

  @Basic
  @jakarta.persistence.Column(name = "comment")
  private String comment;

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment1 = (Comment) o;
    return id == comment1.id && galleryphotographId == comment1.galleryphotographId && Objects.equals(author,
        comment1.author) && Objects.equals(submitted, comment1.submitted) && Objects.equals(comment,
        comment1.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, galleryphotographId, author, submitted, comment);
  }
}
