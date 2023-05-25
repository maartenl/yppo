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
@Table(name = "Photograph")
public class Photograph {
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
  @jakarta.persistence.Column(name = "location_id")
  private long locationId;

  public long getLocationId() {
    return locationId;
  }

  public void setLocationId(long locationId) {
    this.locationId = locationId;
  }

  @Basic
  @jakarta.persistence.Column(name = "filename")
  private String filename;

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  @Basic
  @jakarta.persistence.Column(name = "relativepath")
  private String relativepath;

  public String getRelativepath() {
    return relativepath;
  }

  public void setRelativepath(String relativepath) {
    this.relativepath = relativepath;
  }

  @Basic
  @jakarta.persistence.Column(name = "taken")
  private Timestamp taken;

  public Timestamp getTaken() {
    return taken;
  }

  public void setTaken(Timestamp taken) {
    this.taken = taken;
  }

  @Basic
  @jakarta.persistence.Column(name = "hashstring")
  private String hashstring;

  public String getHashstring() {
    return hashstring;
  }

  public void setHashstring(String hashstring) {
    this.hashstring = hashstring;
  }

  @Basic
  @jakarta.persistence.Column(name = "filesize")
  private Long filesize;

  public Long getFilesize() {
    return filesize;
  }

  public void setFilesize(Long filesize) {
    this.filesize = filesize;
  }

  @Basic
  @jakarta.persistence.Column(name = "angle")
  private Integer angle;

  public Integer getAngle() {
    return angle;
  }

  public void setAngle(Integer angle) {
    this.angle = angle;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Photograph that = (Photograph) o;
    return id == that.id && locationId == that.locationId && Objects.equals(filename, that.filename)
        && Objects.equals(relativepath, that.relativepath) && Objects.equals(taken, that.taken)
        && Objects.equals(hashstring, that.hashstring) && Objects.equals(filesize, that.filesize)
        && Objects.equals(angle, that.angle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, locationId, filename, relativepath, taken, hashstring, filesize, angle);
  }
}
