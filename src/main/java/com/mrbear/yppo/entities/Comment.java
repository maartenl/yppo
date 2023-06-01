package com.mrbear.yppo.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Comments provided by people on photographs in a gallery.
 *
 * @author maartenl
 */
@Entity
@Table(name = "Comment")
public class Comment
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "galleryphotograph_id")
    private long galleryphotographId;
    @Basic
    @Column(name = "author")
    private String author;
    @Basic
    @Column(name = "submitted")
    private Timestamp submitted;
    @Basic
    @Column(name = "comment")
    private String comment;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getGalleryphotographId()
    {
        return galleryphotographId;
    }

    public void setGalleryphotographId(long galleryphotographId)
    {
        this.galleryphotographId = galleryphotographId;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public Timestamp getSubmitted()
    {
        return submitted;
    }

    public void setSubmitted(Timestamp submitted)
    {
        this.submitted = submitted;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
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
        Comment comment1 = (Comment) o;
        return id == comment1.id && galleryphotographId == comment1.galleryphotographId && Objects.equals(author,
                comment1.author) && Objects.equals(submitted, comment1.submitted) && Objects.equals(comment,
                comment1.comment);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, galleryphotographId, author, submitted, comment);
    }
}
