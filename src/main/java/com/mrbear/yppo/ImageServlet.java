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
import com.mrbear.yppo.entities.Photograph;
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

    @Inject
    private PhotoService photoService;

    private void writeError(String error, HttpServletResponse response) throws IOException
    {

        try (PrintWriter out
                     = response.getWriter();) {
            response.setContentType("text/html;charset=UTF-8");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ImageServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + error + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
            angle = photoService.getAngle(id).orElseThrow(() -> new WebApplicationException("Angle not found!"));
        } catch (ImageProcessingException ex) {
            throw new IOException(ex);
        }

        String filename = file.getName();
        String contentType = "text/html;charset=UTF-8";
        if (filename.toLowerCase().endsWith(".jpg")) {
            contentType = "image/jpeg";
        }
        if (filename.toLowerCase().endsWith(".jpeg")) {
            contentType = "image/jpeg";
        }
        if (filename.toLowerCase().endsWith(".gif")) {
            contentType = "image/gif";
        }
        if (filename.toLowerCase().endsWith(".png")) {
            contentType = "image/png";
        }
        if (filename.toLowerCase().endsWith(".avi")) {
            if (request.getParameter("size") != null) {
                contentType = "image/png";
                response.setContentType(contentType);
                FileOperations.dumpFile(getServletContext().getResourceAsStream("/resources/images/movie.png"), response.getOutputStream());
            } else {
                contentType = "video/avi";
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
