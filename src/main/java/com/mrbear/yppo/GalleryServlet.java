package com.mrbear.yppo;

import com.mrbear.yppo.entities.Gallery;
import com.mrbear.yppo.entities.Utils;
import com.mrbear.yppo.services.GalleryService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet(name = "Galleries", urlPatterns = {"/galleries"})
public class GalleryServlet extends HttpServlet
{

  private static final Logger LOGGER = Logger.getLogger(GalleryServlet.class.getName());

  @Inject
  private GalleryService galleryService;

  private String createTree(Gallery gallery, List<Gallery> allGalleries)
  {
    List<Gallery> kids = allGalleries.stream()
        .filter(x -> Objects.equals(x.getParentId(), gallery.getId()))
        .sorted(Comparator.comparingInt(Gallery::getSortorder).thenComparing(Gallery::getCreationDate))
        .toList();
    String highlight = "/yourpersonalphotographorganiser/images/gallery.png";
    if (gallery.getHighlight() != null)
    {
      highlight = String.format("/yourpersonalphotographorganiser/images?id=%s&size=thumb", gallery.getHighlight());
    }
    if (kids.isEmpty())
    {
      return String.format("""
              <li class="list-group-item">
                  <div class="ms-2 me-auto">
                    <div class="fw-bold">
                      <img width="45px" height="45px" src="%s"><a href="galleries/%d">%s</a>
                    </div>
                    %s
                  </div>  
              </li>
          """, highlight, gallery.getId(), gallery.getName(), gallery.getDescription());
    }
    String subTree = kids.stream()
        .map(x -> createTree(x, allGalleries))
        .collect(Collectors.joining());
    return String.format("""
            <li class="list-group-item">
              <div class="ms-2 me-auto">
                <div class="fw-bold">
                  <img width="45px" height="45px" src="%s">
                  <a class="btn btn-outline-primary btn-sm" data-bs-toggle="collapse" data-bs-target="#kidsOf%d" role="button" aria-expanded="false" aria-controls="#kidsOf%d">Toggle</a>
                  <a href="galleries/%d">%s</a>
                </div>
                %s
                <ul class="list-group collapse" id="kidsOf%d">%s</ul>
              </div>  
            </li>
            """, highlight, gallery.getId(), gallery.getId(), gallery.getId(), gallery.getName(), gallery.getDescription(),
        gallery.getId(), subTree);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    List<Gallery> galleries = galleryService.getGalleries();
    // Setting up the content type of web page
    resp.setContentType("text/html");
    // Writing the message on the web page
    PrintWriter out = resp.getWriter();
    String description = galleries.stream()
        .filter(x -> x.getParentId() == null)
        .sorted(Comparator.comparingInt(Gallery::getSortorder).thenComparing(Gallery::getCreationDate))
        .map(x -> createTree(x, galleries))
        .collect(Collectors.joining());
    out.println(HtmlUtils.getHeader());
    out.println(String.format("""
            <div class="container">
              <div class="row">
                <div class="col">
                  <h1>Galleries</h1>
                  <a href="/yourpersonalphotographorganiser" class="btn btn-primary btn-sm">Back to main</a>
                  <ul class="list-group">
                    %s
                  </ul>
                </div>
              </div>
            </div>
            <div class="container">
              <div class="row">
                <div class="col">
                  <form method="POST">
                    <div class="mb-3">
                      <label for="newGalleryName" class="form-label">New gallery name</label>
                      <input type="text" class="form-control" name="newGalleryName" id="newGalleryName">
                    </div>
                    <div>
                      <button type="submit" class="btn btn-primary">Submit</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
        """, description));
    out.println(HtmlUtils.getFooter());
  }

  @Transactional
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    String galleryName = req.getParameter("newGalleryName");
    if (galleryName != null && !galleryName.isBlank())
    {
      LOGGER.finest(String.format("doPost New Gallery %s", galleryName));
      galleryService.createGallery(galleryName);
    }

    // Writing the message on the web page
    PrintWriter out = resp.getWriter();
    out.println(HtmlUtils.getHeader());
    out.println(String.format("""
                    <div class="container">
                      <div class="row">
                        <div class="col">
                          <div class="alert alert-primary" role="alert">
                            Data submitted. <a href="/yourpersonalphotographorganiser/galleries" class="btn btn-primary btn-sm">Back to gallery</a>
                          </div>
                        </div>
                      </div>
                    </div>"""));
    out.println(HtmlUtils.getFooter());
  }
}
