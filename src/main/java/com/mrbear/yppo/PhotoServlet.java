package com.mrbear.yppo;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.enums.ImageAngle;
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
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Shows all photos of a gallery.
 */
@WebServlet(name = "Photo", urlPatterns = {"/photos/*"})
public class PhotoServlet extends HttpServlet
{
  private static final Logger LOGGER = Logger.getLogger(PhotoServlet.class.getName());
  public static final String EMPTY = "empty";

  @Inject
  private PhotoService photoService;

  @Inject
  private GalleryService galleryService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    // Setting up the content type of web page
    resp.setContentType("text/html");

    // for example: /yourpersonalphotographorganiser/photos/1
    String[] requestURI = req.getRequestURI().split("/");
    Long id = Long.valueOf(requestURI[requestURI.length - 1]);
    Optional<GalleryPhotograph> galleryOpt = photoService.getGalleryPhotograph(id);
    if (galleryOpt.isEmpty())
    {
      PrintWriter out = resp.getWriter();
      out.println("GalleryPhotograph not found.");
      return;
    }
    GalleryPhotograph galleryPhotograph = galleryOpt.get();

    String description = "";
    if (galleryPhotograph.getDescription() != null && !galleryPhotograph.getDescription().isBlank())
    {
      description = String.format("<p>%s</p>", galleryPhotograph.getDescription());
    }

    // Writing the message on the web page
    PrintWriter out = resp.getWriter();
    out.println(HtmlUtils.getHeader());
    // header buttons etc.
    out.println(String.format("""               
            <div class="container">
              <div class="row">
                <div class="col">
                  <h1>%s</h1>
                  <div class="alert alert-primary" role="alert">
                    <a href="/yourpersonalphotographorganiser/galleries/%s" class="btn btn-primary btn-sm">Back to gallery</a>
                    <a class="btn btn-primary btn-sm" data-bs-toggle="collapse" data-bs-target=".collapsed-forms" role="button" aria-expanded="false" aria-controls=".collapsed-forms">Edit</a>
                  </div>
                </div>
              </div>
            </div>
            """, galleryPhotograph.getName(), galleryPhotograph.getGalleryId()));
    // real photo
    out.println(String.format("""               
                        <div class="container text-center">
                          <div class="row">
                            <div class="col">
                              <img src="/yourpersonalphotographorganiser/images?id=%s&size=large" alt="%s"/>
                              <p>%s</p>
                              %s                                                 
                            </div>
                          </div>  
                        </div>                                   
                    """,
            galleryPhotograph.getPhotograph().getId(), galleryPhotograph.getName(), galleryPhotograph.getName(), description));
    Photograph photograph = galleryPhotograph.getPhotograph();
    ImageAngle angle = null;
    try
    {
      angle = photograph.getAngle().orElse(null);
    } catch (ImageProcessingException | MetadataException e)
    {
      throw new RuntimeException(e);
    }
    String tableContents = Stream.of(
            new Property("Gallery photograph", EMPTY),
            new Property("Id", galleryPhotograph.getId()),
            new Property("Name", galleryPhotograph.getName()),
            new Property("Description", galleryPhotograph.getDescription()),
            new Property("Sortorder", galleryPhotograph.getSortorder()),
            new Property(EMPTY, EMPTY),
            new Property("Photograph", EMPTY),
            new Property("Id", photograph.getId()),
            new Property("Filename", photograph.getFilename()),
            new Property("Filesize", photograph.getFilesize()),
            new Property("Full path", photograph.getFullPath()),
            new Property("Relative path", photograph.getRelativepath()),
            new Property("Hashstring", photograph.getHashstring()),
            new Property("Angle", angle)
    ).map(property ->
    {
      if (Objects.equals(EMPTY, property.value())) {
        if (Objects.equals(EMPTY, property.name())) {
          return "<tr><td colspan=\"2\">&nbsp;</td></tr>";
        }
        return String.format("""
                      <tr>
                        <td colspan="2">%s</td>
                      </tr>
                      """
                , property.name());
      }
      return String.format("""
                      <tr>
                        <td>%s</td>
                        <td>%s</td>
                      </tr>
                      """
              , property.name(), property.value());
    }).collect(Collectors.joining());
    out.println(String.format("""
            <div class="container">
              <div class="row">
                <div class="col">
                  <table class="table table-bordered">
                    <thead>
                      <tr>
                        <th scope="col">Property</th>
                        <th scope="col">Value</th>
                      </tr>
                    </thead>
                    <tbody>
                    %s
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
              """, tableContents));
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
