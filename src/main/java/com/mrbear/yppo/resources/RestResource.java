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

import com.mrbear.yppo.services.GalleryService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Path("hello")
public class RestResource {

    @Inject
    private GalleryService galleryService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GalleryRecord> hello(){
        return galleryService.getGalleries().stream()
            .map(x -> new GalleryRecord(x))
            .collect(Collectors.toList());
    }
}
