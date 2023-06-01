package com.mrbear.yppo.entities;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Location where a number of files, which are pictures, reside. Bascially, the root of all photographs on the
 * file system.
 * It's possible to have more than one root.
 *
 * @author maartenl
 */
@Entity
@Table(name = "Location")
public class Location
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "filepath")
    private String filepath;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public void setFilepath(String filepath)
    {
        this.filepath = filepath;
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
        Location location = (Location) o;
        return id == location.id && Objects.equals(filepath, location.filepath);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, filepath);
    }
}
