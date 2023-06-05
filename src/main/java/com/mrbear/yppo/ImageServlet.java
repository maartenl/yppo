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
package com.mrbear.yppo;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mrbear.yppo.enums.ImageAngle;
import com.mrbear.yppo.services.PhotoService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.WebApplicationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The image servlet, returns images properly scaled and rotated, based on a
 * photograph id provided in the parameters as "id".
 * Accessible at for example /YourPersonalPhotographOrganiser/images?id=123.
 *
 * @author maartenl
 */
@WebServlet(name = "ImageServlet", urlPatterns =
        {
                "/images"
        })
public class ImageServlet extends HttpServlet
{

    private static final Logger LOGGER = Logger.getLogger(ImageServlet.class.getName());
    public static final String JPEG_CONTENTTYPE = "image/jpeg";
    public static final String GIF_CONTENTTYPE = "image/gif";
    public static final String PNG_CONTENTTYPE = "image/png";
    public static final String AVI_CONTENTTYPE = "video/x-msvideo";
    public static final String MP4_CONTENTTYPE = "video/mp4";
    public static final String DEFAULT_CONTENTTYPE = "text/html;charset=UTF-8";

    @Inject
    private PhotoService photoService;

    private void writeError(String error, HttpServletResponse response) throws IOException
    {

        try (PrintWriter out
                     = response.getWriter();) {
            response.setContentType(DEFAULT_CONTENTTYPE);
            out.println(HtmlUtils.getHeader());
            out.println("<h1>" + error + "</h1>");
            out.println(HtmlUtils.getFooter());
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException  if a servlet-specific error occurs
     * @throws IOException       if an I/O error occurs
     * @throws MetadataException when not able to retrieve the meta data of a photo.
     */
    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException, MetadataException
    {
        String idString = request.getParameter("id");
        if (idString == null || "".equals(idString.trim())) {
            writeError("Photograph id not found.", response);
            return;
        }
        Logger.getLogger(ImageServlet.class.getName()).log(Level.FINE, "processRequest {0}", idString);
        Long id = null;

        try {
            id = Long.valueOf(idString);
        } catch (NumberFormatException e) {
            writeError("Photograph id not a number.", response);
            return;
        }
        if (id <= 0L) {
            writeError("Photograph id invalid.", response);
            return;
        }

        Long finalId = id;
        File file = photoService.getFile(id).orElseThrow(() -> new WebApplicationException("File with id " + finalId + " not found!"));
        ImageAngle angle;
        try {
            angle = photoService.getAngle(id).orElseGet(() ->
            {
                LOGGER.fine(String.format("Angle not found for file %s!", file.getAbsolutePath()));
                return null;
            });
        } catch (ImageProcessingException ex) {
            throw new IOException(ex);
        }

        String filename = file.getName();
        String contentType = DEFAULT_CONTENTTYPE;
        if (filename.toLowerCase().endsWith(".jpg")) {
            contentType = JPEG_CONTENTTYPE;
        }
        if (filename.toLowerCase().endsWith(".jpeg")) {
            contentType = JPEG_CONTENTTYPE;
        }
        if (filename.toLowerCase().endsWith(".gif")) {
            contentType = GIF_CONTENTTYPE;
        }
        if (filename.toLowerCase().endsWith(".png")) {
            contentType = PNG_CONTENTTYPE;
        }
        if (filename.toLowerCase().endsWith(".avi")) {
            if (request.getParameter("size") != null) {
                contentType = PNG_CONTENTTYPE;
                response.setContentType(contentType);
                FileOperations.dumpFile(getServletContext().getResourceAsStream("/images/movie.png"), response.getOutputStream());
            } else {
                contentType = AVI_CONTENTTYPE;
                response.setContentType(contentType);
                // JDK7 : try-with-resources
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    FileOperations.dumpFile(inputStream, response.getOutputStream());
                } catch (IOException e) {
                    LOGGER.throwing(ImageServlet.class.getName(), "Avi file.", e);
                }
            }
            return;
        }
        if (filename.toLowerCase().endsWith(".mp4")) {
            if (request.getParameter("size") != null) {
                contentType = PNG_CONTENTTYPE;
                response.setContentType(contentType);
                FileOperations.dumpFile(getServletContext().getResourceAsStream("/images/movie.png"), response.getOutputStream());
            } else {
                contentType = MP4_CONTENTTYPE;
                response.setContentType(contentType);
                // JDK7 : try-with-resources
                try (FileInputStream inputStream = new FileInputStream(file)) {
                    FileOperations.dumpFile(inputStream, response.getOutputStream());
                } catch (IOException e) {
                    LOGGER.throwing(ImageServlet.class.getName(), "Mp4 file.", e);
                }
            }
            return;
        }
        response.setContentType(contentType);
        FileOperations.outputImage(file, response.getOutputStream(), request.getParameter("size"), angle);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try {
            processRequest(request, response);
        } catch (MetadataException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        try {
            processRequest(request, response);
        } catch (MetadataException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
