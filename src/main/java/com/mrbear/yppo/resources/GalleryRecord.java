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

import com.mrbear.yppo.entities.Gallery;

public record GalleryRecord(long id, String name, String description, Long highlight, java.sql.Timestamp creationDate,
                            Long parentId, int sortorder) {

  public GalleryRecord(Gallery gallery) {
    this(gallery.getId(), gallery.getName(), gallery.getDescription(), gallery.getHighlight(), gallery.getCreationDate(), gallery.getParentId(), gallery.getSortorder());
  }
}
