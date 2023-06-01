package com.mrbear.yppo.resources;

import com.mrbear.yppo.entities.LogLevel;

import java.time.Instant;

public record LogRecord(long id, String message, String description, Instant creationDate, LogLevel loglevel)
{
}
