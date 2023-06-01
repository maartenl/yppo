package com.mrbear.yppo.entities;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mrbear.yppo.enums.ImageAngle;
import com.mrbear.yppo.images.ImageOperations;
import jakarta.persistence.*;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An entity representing the file containing the image.
 *
 * @author maartenl
 */
@Entity
@Table(name = "Photograph")
@NamedQuery(name = "Photograph.findByFilename", query = "SELECT p FROM Photograph p WHERE p.filename = :filename and p.relativepath = :relativepath")
@NamedQuery(name = "Photograph.findByStats", query = "SELECT p FROM Photograph p WHERE p.hashstring = :hashstring and p.filesize = :filesize")
public class Photograph
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Location location;
    @Basic
    @Column(name = "filename")
    private String filename;
    @Basic
    @Column(name = "relativepath")
    private String relativepath;
    @Basic
    @Column(name = "taken")
    private Timestamp taken;
    @Basic
    @Column(name = "hashstring")
    private String hashstring;
    @Basic
    @Column(name = "filesize")
    private Long filesize;
    @Basic
    @Column(name = "angle")
    private Integer angle;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Location getLocation()
    {
        return location;
    }

    public void setLocation(Location location)
    {
        this.location = location;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public String getRelativepath()
    {
        return relativepath;
    }

    public void setRelativepath(String relativepath)
    {
        this.relativepath = relativepath;
    }

    public Instant getTaken()
    {
        return taken == null ? null : taken.toInstant();
    }

    public void setTaken(Instant taken)
    {
        this.taken = taken == null ? null : Timestamp.from(taken);
    }

    public String getHashstring()
    {
        return hashstring;
    }

    public void setHashstring(String hashstring)
    {
        this.hashstring = hashstring;
    }

    public Long getFilesize()
    {
        return filesize;
    }

    public void setFilesize(Long filesize)
    {
        this.filesize = filesize;
    }

    /**
     * Indicates the angle at which the picture was taken. This is, if possible,
     * stored
     * in the information contained in the picture and is usually stored
     * by the device that took the picture. If not set for this picture, will
     * attempt
     * to retrieve it from the metadata.
     *
     * @return an ImageAngle or null if unable to determine.
     */
    public Optional<ImageAngle> getAngle() throws ImageProcessingException, IOException, MetadataException
    {
        Logger.getLogger(Photograph.class.getName()).log(Level.FINE, "getAngle {0}", angle);
        if (angle == null && ImageOperations.isImage(getFilename())) {
            Optional<ImageAngle> result = ImageOperations.getAngle(new File(getFullPath()));
            if (result.isPresent()) {
                Logger.getLogger(Photograph.class.getName()).log(Level.FINE, "getAngle 1 returns {0}", result);
                return result;
            }
        }
        Optional<ImageAngle> result = ImageAngle.getAngle(angle);
        Logger.getLogger(Photograph.class.getName()).log(Level.FINE, "getAngle 2 returns {0}", result);
        return result;
    }

    public void setAngle(Integer angle)
    {
        this.angle = angle;
    }

    /**
     * Sets the angle of the photograph.
     *
     * @param angle the new angle, may be null.
     */
    public void setAngle(ImageAngle angle)
    {
        if (angle == null) {
            this.angle = null;
            return;
        }
        this.angle = angle.getAngle();
    }

    /**
     * Provides a full absolute file path to this file.
     *
     * @return an absolute path to the file, for example
     * "/home/mrbear/gallery/vacation/2012/DSCN00244.JPG".
     */
    public String getFullPath()
    {
        return getLocation().getFilepath() + File.separator + getRelativepath() + File.separator + getFilename();
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
        Photograph that = (Photograph) o;
        return id == that.id && location == that.location && Objects.equals(filename, that.filename)
                && Objects.equals(relativepath, that.relativepath) && Objects.equals(taken, that.taken)
                && Objects.equals(hashstring, that.hashstring) && Objects.equals(filesize, that.filesize)
                && Objects.equals(angle, that.angle);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, location, filename, relativepath, taken, hashstring, filesize, angle);
    }
}
