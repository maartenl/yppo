package com.mrbear.yppo.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "GalleryPhotograph")
public class GalleryPhotograph {
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
  @jakarta.persistence.Column(name = "gallery_id")
  private long galleryId;

  public long getGalleryId() {
    return galleryId;
  }

  public void setGalleryId(long galleryId) {
    this.galleryId = galleryId;
  }

  @Basic
  @jakarta.persistence.Column(name = "photograph_id")
  private long photographId;

  public long getPhotographId() {
    return photographId;
  }

  public void setPhotographId(long photographId) {
    this.photographId = photographId;
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
  @jakarta.persistence.Column(name = "sortorder")
  private Long sortorder;

  public Long getSortorder() {
    return sortorder;
  }

  public void setSortorder(Long sortorder) {
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
    GalleryPhotograph that = (GalleryPhotograph) o;
    return id == that.id && galleryId == that.galleryId && photographId == that.photographId && Objects.equals(
        name, that.name) && Objects.equals(description, that.description) && Objects.equals(sortorder,
        that.sortorder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, galleryId, photographId, name, description, sortorder);
  }
}
