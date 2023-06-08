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
@NamedQuery(name = "GalleryPhotograph.findAll", query = "SELECT p FROM GalleryPhotograph p where p.galleryId = :galleryId order by p.sortorder, p.photograph.taken")
@NamedQuery(name = "GalleryPhotograph.findByPhotograph", query = " SELECT p from GalleryPhotograph p where p.photograph = :photograph")
public class GalleryPhotograph
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "gallery_id")
    private long galleryId;
    @JoinColumn(name = "photograph_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Photograph photograph;
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

    public Photograph getPhotograph()
    {
        return photograph;
    }

    public void setPhotograph(Photograph photograph)
    {
        this.photograph = photograph;
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
        this.description = Utils.getValue(description);
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
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        GalleryPhotograph that = (GalleryPhotograph) o;
        return id == that.id && galleryId == that.galleryId && photograph == that.photograph && Objects.equals(
                name, that.name) && Objects.equals(description, that.description) && Objects.equals(sortorder,
                that.sortorder);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, galleryId, photograph, name, description, sortorder);
    }
}
