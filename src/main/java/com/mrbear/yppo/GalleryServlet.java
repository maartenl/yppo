package com.mrbear.yppo;

import com.mrbear.yppo.entities.Gallery;
import com.mrbear.yppo.services.GalleryService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebServlet(name = "Galleries", urlPatterns = { "/galleries" })
public class GalleryServlet extends HttpServlet {

  @Inject
  private GalleryService galleryService;

  private String createTree(Gallery gallery, List<Gallery> allGalleries) {
    List<Gallery> kids = allGalleries.stream()
        .filter(x -> Objects.equals(x.getParentId(), gallery.getId()))
        .sorted(Comparator.comparingInt(Gallery::getSortorder))
        .collect(Collectors.toList());
    if (kids.isEmpty()) {
      return String.format("""
              <li class="list-group-item">
                  <div class="ms-2 me-auto">
                          <div class="fw-bold">%s</div>
                          %s
                        </div>
              </li>
          """, gallery.getName(), gallery.getDescription());
    }
    String subTree = kids.stream()
        .map(x -> createTree(x, allGalleries))
        .collect(Collectors.joining());
    return String.format("""
            <li class="list-group-item"><div class="ms-2 me-auto">
                              <div class="fw-bold">%s</div>
                              %s
                            </div><ul class="list-group">%s</ul></li>
            """, gallery.getName(), gallery.getDescription(),
        subTree);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    List<Gallery> galleries = galleryService.getGalleries();
    // Setting up the content type of web page
    resp.setContentType("text/html");
    // Writing the message on the web page
    PrintWriter out = resp.getWriter();
    String description = galleries.stream()
        .filter(x -> x.getParentId() == null)
        .map(x -> createTree(x, galleries))
        .collect(Collectors.joining());
    out.println(String.format("""
        <!doctype html>
        <html lang="en">
          <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>Your Personal Photograph Organiser</title>
            <link href="css/bootstrap.css" rel="stylesheet">
            <link
                    rel="stylesheet"
                    href="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.css"
            />
          </head>
          <body>
            <div class="container">
              <div class="row">
                <div class="col">
                <h1><a href ="galleries">Galleries</a></h1>
                  <ul class="list-group">
                    %s
                  </ul>
                  </div>
                </div>
              </div>
            <script src="js/jquery-3.7.0.min.js"></script>
            <script src="js/bootstrap.bundle.js"></script>
            <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.umd.js"></script>
          </body>
        </html>
        """, description));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doPost(req, resp);
  }
}
