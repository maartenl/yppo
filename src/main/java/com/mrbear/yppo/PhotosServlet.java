package com.mrbear.yppo;

import com.mrbear.yppo.entities.Gallery;
import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.GalleryService;
import com.mrbear.yppo.services.LocationService;
import com.mrbear.yppo.services.PhotoService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.WebApplicationException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Shows all photos of a gallery.
 */
@WebServlet(name = "Photos", urlPatterns = { "/galleries/*" })
public class PhotosServlet extends HttpServlet {

  @Inject
  private PhotoService photoService;

  @Inject
  private GalleryService galleryService;

  @Inject
  private LocationService locationService;

  private String createTree(Gallery gallery, List<Gallery> allGalleries) {
    List<Gallery> kids = allGalleries.stream()
        .filter(x -> Objects.equals(x.getParentId(), gallery.getId()))
        .sorted(Comparator.comparingInt(Gallery::getSortorder))
        .toList();
    if (kids.isEmpty()) {
      return String.format("""
              <li class="list-group-item">
                  <div class="ms-2 me-auto">
                          <div class="fw-bold">%s</div>
                          <a href="/yourpersonalphotographorganiser/galleries/%s">%s</a>
                        </div>
              </li>
          """, gallery.getId(), gallery.getName(), gallery.getDescription());
    }
    String subTree = kids.stream()
        .map(x -> createTree(x, allGalleries))
        .collect(Collectors.joining());
    return String.format("""
            <li class="list-group-item"><div class="ms-2 me-auto">
                              <div class="fw-bold"><a href="/galleries/%s">%s</a></div>
                              %s
                            </div><ul class="list-group">%s</ul></li>
            """, gallery.getId(), gallery.getName(), gallery.getDescription(),
        subTree);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    // Setting up the content type of web page
    resp.setContentType("text/html");
    // Writing the message on the web page
    PrintWriter out = resp.getWriter();

    // for example: /yourpersonalphotographorganiser/galleries/1
    String[] requestURI = req.getRequestURI().split("/");
    Long id = Long.valueOf(requestURI[requestURI.length - 1]);
    Optional<Gallery> galleryOpt = galleryService.getGallery(id);
    if (galleryOpt.isEmpty()) {
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
    for (GalleryPhotograph photograph: photos) {
      description.append(createPhoto(photograph));
      counter++;
      if (counter == 3) {
        counter = 0;
        description.append("""
                  </div>
                </div>
                <div class="container text-center">
                  <div class="row">
                """);
      }
    }

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
                  <a href="/yourpersonalphotographorganiser/galleries" class="alert-link">Back to galleries</a>.
                </div>
                <div class="alert alert-primary" role="alert">
                  %s
                </div>
                %s
                  </div>
                </div>
              </div>
            <script src="../js/jquery-3.7.0.min.js"></script>
            <script src="../js/bootstrap.bundle.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.umd.js"></script>
          </body>
        </html>
        """, gallery.getName(), gallery.getDescription(), description.toString()));
  }

  private String createPhoto(GalleryPhotograph galleryPhotograph) {
    Photograph photograph = photoService.getPhotograph(galleryPhotograph)
        .orElseThrow(
            () -> new WebApplicationException("Photographs " + galleryPhotograph.getPhotographId() + " not found!"));
    return String.format("""
                <div class="col">
                  <img src="/yourpersonalphotographorganiser/images?id=%s&size=medium" alt="%s" loading="lazy"/>
                  <p>%s</p>
                </div>
                """,photograph.getId(), galleryPhotograph.getName(), galleryPhotograph.getName());

//    return "<div class=\"col\">gal:" + galleryPhotograph.getName() + ":" + galleryPhotograph.getDescription() + ":"
//        + galleryPhotograph.getPhotographId() + "<br/>fot:" + photograph.getFilename()
//        + ":" + photograph.getRelativepath() + ":" + photograph.getLocation() + "</br>loc:" + location.getId()
//        + ":" + location.getFilepath() + "</div>";
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doPost(req, resp);
  }
}
