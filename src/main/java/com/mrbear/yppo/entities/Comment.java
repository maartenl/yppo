package com.mrbear.yppo.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Comments provided by people on photographs in a gallery.
 *
 * @author maartenl
 */
@Entity
@Table(name = "Comment")
@NamedQuery(name = "Comment.findByPhotograph", query = "SELECT c FROM Comment c where c.galleryphotographId = :galleryphotographid order by c.submitted desc")
public class Comment
{
  private static final String PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss z";

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

  public Instant getSubmitted()
  {
    return submitted == null ? null : submitted.toInstant();
  }

  public void setSubmitted(Instant submitted)
  {
    this.submitted = submitted == null ? null : Timestamp.from(submitted);
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
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
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

  /**
   * Get the date and time when the comment was submitted.
   * @return date and time when comment was submitted
   */
  public String getSubmittedString()
  {
    if (getSubmitted() == null)
    {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN_FORMAT)
            .withZone(ZoneId.systemDefault());
    return formatter.format(getSubmitted());
  }
}
