package com.mrbear.yppo;

import com.mrbear.yppo.entities.Gallery;
import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.GalleryService;
import com.mrbear.yppo.services.PhotoService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Shows all photos of a gallery.
 */
@WebServlet(name = "Photos", urlPatterns = {"/galleries/*"})
public class PhotosServlet extends HttpServlet
{
    private static final Logger LOGGER = Logger.getLogger(PhotosServlet.class.getName());

    @Inject
    private PhotoService photoService;

    @Inject
    private GalleryService galleryService;

    private String createPhoto(GalleryPhotograph galleryPhotograph)
    {
        Photograph photograph = galleryPhotograph.getPhotograph();
        String description = "";
        if (galleryPhotograph.getDescription() != null && !galleryPhotograph.getDescription().isBlank())
        {
            description = String.format("<p>%s</p>", galleryPhotograph.getDescription());
        }
            return String.format("""
                    <div class="col">
                          <a href="/yourpersonalphotographorganiser/images?id=%s&extension=%s" data-fancybox data-caption="%s">                                              
                            <img src="/yourpersonalphotographorganiser/images?id=%s&size=medium" alt="%s" loading="lazy"/>
                          </a>
                          <p>
                            %s
                            <a href="/yourpersonalphotographorganiser/photos/%s" class="btn btn-sm btn-outline-primary">View</a>
                          </p>
                          %s                  
                        </div>
                        """, photograph.getId(), photograph.getFilename(), galleryPhotograph.getName(), photograph.getId(), galleryPhotograph.getName(), galleryPhotograph.getName(), galleryPhotograph.getId(), description,
                galleryPhotograph.getId(), galleryPhotograph.getName(), galleryPhotograph.getDescription());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // Setting up the content type of web page
        resp.setContentType("text/html");

        // for example: /yourpersonalphotographorganiser/galleries/1
        String[] requestURI = req.getRequestURI().split("/");
        Long id = Long.valueOf(requestURI[requestURI.length - 1]);
        Optional<Gallery> galleryOpt = galleryService.getGallery(id);
        if (galleryOpt.isEmpty())
        {
            PrintWriter out = resp.getWriter();
            out.println("Gallery not found.");
            return;
        }
        Gallery gallery = galleryOpt.get();

        List<GalleryPhotograph> photos = photoService.getGalleryPhotographs(id);

        StringBuilder description = new StringBuilder("""
                <div class="container text-center">
                  <div class="row">
                  """);
        int counter = 0;
        for (GalleryPhotograph photograph : photos)
        {
            description.append(createPhoto(photograph));
            counter++;
            if (counter == 3)
            {
                counter = 0;
                description.append("""
                          </div>
                        </div>
                        <div class="container text-center">
                          <div class="row">
                        """);
            }
        }

        // Writing the message on the web page
        PrintWriter out = resp.getWriter();
        out.println(HtmlUtils.getHeader());
        out.println(String.format("""
                    <div class="container">
                      <div class="row">
                        <div class="col">
                          <h1>%s</h1>
                          <div class="alert alert-primary" role="alert">
                            <a href="/yourpersonalphotographorganiser/galleries" class="btn btn-primary btn-sm">Back to galleries</a>
                            <a class="btn btn-primary btn-sm" data-bs-toggle="collapse" data-bs-target=".collapsed-forms" role="button" aria-expanded="false" aria-controls=".collapsed-forms">Edit</a>
                          </div>
                          <div class="alert alert-primary" role="alert">
                            %s
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="container collapse collapsed-forms">
                      <div class="row">
                        <div class="col">
                          <form method="POST">
                            <input type="hidden" class="form-control" name="galleryId" id="galleryId" value="%s">
                            <div class="mb-3">
                              <label for="galleryName" class="form-label">Gallery name</label>
                              <input type="text" class="form-control" name="galleryName" id="galleryName" aria-describedby="galleryNameHelp" value="%s">
                              <div id="galleryNameHelp" class="form-text">The (short) name of the gallery.</div>
                            </div>
                            <div class="mb-3">
                              <label for="galleryDescription" class="form-label">Description</label>
                              <textarea class="form-control" name="galleryDescription" id="galleryDescription" rows="6">%s</textarea>
                            </div>
                            <div>
                              <button type="submit" class="btn btn-primary">Submit</button>
                            </div>
                          </form>
                        </div>
                      </div>
                    </div>
                    %s
                """, gallery.getName(), gallery.getDescription(), gallery.getId(), gallery.getName(), gallery.getDescription(), description));
        out.println(HtmlUtils.getFooter());
    }

    @Transactional
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String[] requestURI = req.getRequestURI().split("/");
        Long id = Long.valueOf(requestURI[requestURI.length - 1]);

        String galleryId = req.getParameter("galleryId");
        String galleryName = req.getParameter("galleryName");
        String galleryDescription = req.getParameter("galleryDescription");
        if (galleryId != null && !galleryId.isBlank())
        {
            LOGGER.finest(String.format("doPost Gallery %s,%s,%s", galleryId, galleryName, galleryDescription));
            galleryService.updateGallery(Long.valueOf(galleryId), galleryName, galleryDescription);
        }

        // Writing the message on the web page
        PrintWriter out = resp.getWriter();
        out.println(HtmlUtils.getHeader());
        out.println(String.format("""
                    <div class="container">
                      <div class="row">
                        <div class="col">
                          <div class="alert alert-primary" role="alert">
                            Data submitted. <a href="/yourpersonalphotographorganiser/galleries/%s" class="btn btn-primary btn-sm">Back to gallery</a>
                          </div>
                        </div>
                      </div>
                    </div>""", id));
        out.println(HtmlUtils.getFooter());
    }
}
