package com.mrbear.yppo.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Gallery")
@NamedQuery(name = "Gallery.findAll", query = "SELECT p FROM Gallery p")
public class Gallery {
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
  @jakarta.persistence.Column(name = "name")
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
  @jakarta.persistence.Column(name = "creation_date")
  private Timestamp creationDate;

  public Timestamp getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Timestamp creationDate) {
    this.creationDate = creationDate;
  }

  @Basic
  @jakarta.persistence.Column(name = "parent_id")
  private Long parentId;

  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Basic
  @jakarta.persistence.Column(name = "highlight")
  private Long highlight;

  public Long getHighlight() {
    return highlight;
  }

  public void setHighlight(Long highlight) {
    this.highlight = highlight;
  }

  @Basic
  @jakarta.persistence.Column(name = "sortorder")
  private int sortorder;

  public int getSortorder() {
    return sortorder;
  }

  public void setSortorder(int sortorder) {
    this.sortorder = sortorder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Gallery gallery = (Gallery) o;
    return id == gallery.id && sortorder == gallery.sortorder && Objects.equals(name, gallery.name)
        && Objects.equals(description, gallery.description) && Objects.equals(creationDate,
        gallery.creationDate) && Objects.equals(parentId, gallery.parentId) && Objects.equals(
        highlight, gallery.highlight);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, creationDate, parentId, highlight, sortorder);
  }
}
