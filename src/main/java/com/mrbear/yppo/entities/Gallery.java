package com.mrbear.yppo.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * A collection of references to Photographs and other galleries.
 *
 * @author maartenl
 */
@Entity
@Table(name = "Gallery")
@NamedQuery(name = "Gallery.findAll", query = "SELECT p FROM Gallery p")
public class Gallery
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "description")
    private String description;
    @Basic
    @Column(name = "creation_date")
    private Timestamp creationDate;
    @Basic
    @Column(name = "parent_id")
    private Long parentId;
    @Basic
    @Column(name = "highlight")
    private Long highlight;
    @Basic
    @Column(name = "sortorder")
    private int sortorder;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public Timestamp getCreationDate()
    {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate)
    {
        this.creationDate = creationDate;
    }

    public Long getParentId()
    {
        return parentId;
    }

    public void setParentId(Long parentId)
    {
        this.parentId = parentId;
    }

    public Long getHighlight()
    {
        return highlight;
    }

    public void setHighlight(Long highlight)
    {
        this.highlight = highlight;
    }

    public int getSortorder()
    {
        return sortorder;
    }

    public void setSortorder(int sortorder)
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
        Gallery gallery = (Gallery) o;
        return id == gallery.id && sortorder == gallery.sortorder && Objects.equals(name, gallery.name)
                && Objects.equals(description, gallery.description) && Objects.equals(creationDate,
                gallery.creationDate) && Objects.equals(parentId, gallery.parentId) && Objects.equals(
                highlight, gallery.highlight);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, name, description, creationDate, parentId, highlight, sortorder);
    }
}
