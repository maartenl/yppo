package com.mrbear.yppo.entities;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

/**
 * For storing log messages. In our case these are mostly concerned with the
 * logging of Batch operations.
 *
 * @author maartenl
 */
@Entity
@Table(name = "Log")
@NamedQuery(name = "Log.findAll", query = "SELECT l FROM Log l")
@NamedQuery(name = "Log.findById", query = "SELECT l FROM Log l WHERE l.id = :id")
@NamedQuery(name = "Log.findFirstHundred", query = "SELECT l FROM Log l ORDER BY l.creationDate desc")
@NamedQuery(name = "Log.deleteAll", query = "DELETE FROM Log l")
public class Log
{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;

    @Basic
    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "loglevel")
    private LogLevel loglevel;

    @Basic
    @Column(name = "message")
    private String message;

    @Basic
    @Column(name = "source")
    private String source;

    public Log()
    {
        // used for ORM, to instantiate stuff.
    }

    public Log(String source, String message, String description, LogLevel logLevel)
    {
        this.source = source;
        this.message = message;
        this.description = description;
        this.loglevel = logLevel;
        this.creationDate = Timestamp.from(Instant.now());
    }

    public Instant getCreationDate()
    {
        return creationDate == null ? null : creationDate.toInstant();
    }

    public void setCreationDate(Instant creationDate)
    {
        this.creationDate = creationDate == null ? null : Timestamp.from(creationDate);
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public LogLevel getLoglevel()
    {
        return loglevel;
    }

    public void setLoglevel(LogLevel loglevel)
    {
        this.loglevel = loglevel;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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
        Log log = (Log) o;
        return id == log.id && Objects.equals(creationDate, log.creationDate) && Objects.equals(
                description, log.description) && Objects.equals(loglevel, log.loglevel) && Objects.equals(
                message, log.message) && Objects.equals(source, log.source);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, creationDate, description, loglevel, message, source);
    }
}
