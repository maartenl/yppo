package com.mrbear.yppo.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * A photograph contained in a gallery, with references to both the gallery and
 * the original photograph itself.
 *
 * @author maartenl
 */
@Entity
@Table(name = "GalleryPhotograph")
@NamedQuery(name = "GalleryPhotograph.findAll", query = "SELECT p FROM GalleryPhotograph p where p.galleryId = :galleryId order by p.sortorder")
public class GalleryPhotograph
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "gallery_id")
    private long galleryId;
    @Basic
    @Column(name = "photograph_id")
    private long photographId;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "sortorder")
    private Long sortorder;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getGalleryId()
    {
        return galleryId;
    }

    public void setGalleryId(long galleryId)
    {
        this.galleryId = galleryId;
    }

    public long getPhotographId()
    {
        return photographId;
    }

    public void setPhotographId(long photographId)
    {
        this.photographId = photographId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Long getSortorder()
    {
        return sortorder;
    }

    public void setSortorder(Long sortorder)
    {
        this.sortorder = sortorder;
    }

    @Override
    public boolean equals(Object o)
    {
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
    public int hashCode()
    {
        return Objects.hash(id, galleryId, photographId, name, description, sortorder);
    }
}
