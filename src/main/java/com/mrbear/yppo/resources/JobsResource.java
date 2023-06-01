/********************************************************************************
 * Copyright (c) 10/19/2022 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0, or the Eclipse Distribution License
 * v1.0 which is available at
 * https://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: EPL-2.0 OR BSD-3-Clause
 ********************************************************************************/
package com.mrbear.yppo.resources;

import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Transactional
@Path("jobs")
public class JobsResource
{

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<JobName> list() {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        return jobOperator.getJobNames().stream().map(JobName::new).collect(Collectors.toList());
    }

    @GET
    @Path("add/start")
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionJobRecord start(){
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        long executionId = jobOperator.start("AddPhotographs", new Properties());
        return new ExecutionJobRecord(executionId, "add", "started");
    }

    @GET
    @Path("add/stop/{executionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionJobRecord stop(long executionId){
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        jobOperator.stop(executionId);
        return new ExecutionJobRecord(executionId, "add", "stopped");
    }

    @GET
    @Path("add/restart/{executionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public ExecutionJobRecord restart(long executionId){
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        jobOperator.restart(executionId, new Properties());
        return new ExecutionJobRecord(executionId, "add", "restarted");
    }
}
