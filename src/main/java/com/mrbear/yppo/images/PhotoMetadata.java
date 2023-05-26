/*
 *  Copyright (C) 2012 maartenl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mrbear.yppo.images;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data Object for returning information as JSON to the client.
 *
 * @author maartenl
 */
public class PhotoMetadata
{

    /**
     * Name of the photo.
     */
    public String name;

    /**
     * The date the photo was taken, or null.
     */
    public Date taken;

    /**
     * The angle at which the photo was taken, or null.
     * See {@link com.mrbear.yppo.enums.ImageAngle}
     */
    public Integer angle;

    /**
     * A list of all the metadata of the image.
     */
    public List<PhotoTag> tags = new ArrayList<>();

    public PhotoMetadata()
    {
        // needs to be here for JSON-ification.
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for (PhotoTag tag : tags)
        {
            builder.append(tag).append(",");
        }
        return builder.toString();
    }
}
