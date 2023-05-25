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
@Table(name = "Log")
public class Log {
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
  @jakarta.persistence.Column(name = "creation_date")
  private Timestamp creationDate;

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  @Basic
  @jakarta.persistence.Column(name = "description")
  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @jakarta.persistence.Column(name = "loglevel")
  private String loglevel;

  public String getLoglevel() {
    return loglevel;
  }

  public void setLoglevel(String loglevel) {
    this.loglevel = loglevel;
  }

  @Basic
  @jakarta.persistence.Column(name = "message")
  private String message;

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Basic
  @jakarta.persistence.Column(name = "source")
  private String source;

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Log log = (Log) o;
    return id == log.id && Objects.equals(creationDate, log.creationDate) && Objects.equals(
        description, log.description) && Objects.equals(loglevel, log.loglevel) && Objects.equals(
        message, log.message) && Objects.equals(source, log.source);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creationDate, description, loglevel, message, source);
  }
}
