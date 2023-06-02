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
        out.println(String.format("""
                <!doctype html>
                <html lang="en">
                  <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Your Personal Photograph Organiser</title>
                    <link href="../css/bootstrap.css" rel="stylesheet">
                    <link
                            rel="stylesheet"
                            href="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.css"
                    />
                  </head>
                  <body>
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
                          <form method="POST" onsubmit="return false;">
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
                    <script src="../js/jquery-3.7.0.min.js"></script>
                    <script src="../js/bootstrap.bundle.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.umd.js"></script>
                  </body>
                </html>
                """, gallery.getName(), gallery.getDescription(), gallery.getId(), gallery.getName(), gallery.getDescription(), description.toString()));
    }

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
                          <img src="/yourpersonalphotographorganiser/images?id=%s&size=medium" alt="%s" loading="lazy"/>
                          <p>%s</p>
                          %s                  
                                <div class="collapse collapsed-forms">
                                    <form method="POST">
                                      <input type="hidden" class="form-control" name="galleryPhotographId" id="galleryPhotographId" value="%s">
                                      <div class="mb-3">
                                        <label for="galleryPhotographName" class="form-label">Name</label>
                                        <input type="text" class="form-control" name="galleryPhotographName" id="galleryPhotographName" value="%s">
                                      </div>
                                      <div class="mb-3">
                                        <label for="galleryPhotographDescription" class="form-label">Description</label>
                                        <textarea class="form-control" name="galleryPhotographDescription" id="galleryPhotographDescription" rows="6">%s</textarea>
                                      </div>
                                      <div>
                                        <button type="submit" class="btn btn-primary">Submit</button>
                                      </div>
                                    </form>
                                  </div>
                        </div>
                        """, photograph.getId(), galleryPhotograph.getName(), galleryPhotograph.getName(), description,
                galleryPhotograph.getId(), galleryPhotograph.getName(), galleryPhotograph.getDescription());
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

        String galleryPhotographId = req.getParameter("galleryPhotographId");
        String galleryPhotographName = req.getParameter("galleryPhotographName");
        String galleryPhotographDescription = req.getParameter("galleryPhotographDescription");
        if (galleryPhotographId != null && !galleryPhotographId.isBlank())
        {
            LOGGER.finest(String.format("doPost galleryPhotograph %s,%s,%s", galleryPhotographId, galleryPhotographName, galleryPhotographDescription));
            photoService.updateGalleryPhotograph(Long.valueOf(galleryPhotographId), galleryPhotographName, galleryPhotographDescription);
        }

        // Writing the message on the web page
        PrintWriter out = resp.getWriter();
        out.println(String.format("""
                <!doctype html>
                <html lang="en">
                  <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Your Personal Photograph Organiser</title>
                    <link href="../css/bootstrap.css" rel="stylesheet">
                    <link
                            rel="stylesheet"
                            href="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.css"
                    />
                  </head>
                  <body>
                    <div class="container">
                      <div class="row">
                        <div class="col">
                          <div class="alert alert-primary" role="alert">
                            Data submitted. <a href="/yourpersonalphotographorganiser/galleries/%s" class="btn btn-primary btn-sm">Back to gallery</a>
                          </div>
                        </div>
                      </div>
                    </div>""", id));
    }
}
