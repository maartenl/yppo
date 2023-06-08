package com.mrbear.yppo;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.MetadataException;
import com.mrbear.yppo.entities.Comment;
import com.mrbear.yppo.entities.GalleryPhotograph;
import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.enums.ImageAngle;
import com.mrbear.yppo.images.GenericMetadata;
import com.mrbear.yppo.images.ImageOperations;
import com.mrbear.yppo.services.CommentService;
import com.mrbear.yppo.services.GalleryService;
import com.mrbear.yppo.services.PhotoService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

  @Inject
  private CommentService commentService;

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    // Setting up the content type of web page
    resp.setContentType("text/html");

    // for example: /yourpersonalphotographorganiser/photos/1
    String[] requestURI = req.getRequestURI().split("/");
    Long id = Long.valueOf(requestURI[requestURI.length - 1]);
    Optional<GalleryPhotograph> galleryPhotographOptional = photoService.getGalleryPhotograph(id);
    if (galleryPhotographOptional.isEmpty())
    {
      PrintWriter out = resp.getWriter();
      out.println("GalleryPhotograph not found.");
      return;
    }
    GalleryPhotograph galleryPhotograph = galleryPhotographOptional.get();

    List<GalleryPhotograph> galleryPhotographs = photoService.getGalleryPhotographs(galleryPhotograph.getGalleryId());
    int index = IntStream.range(0, galleryPhotographs.size())
            .filter(i -> Objects.equals(galleryPhotographs.get(i).getId(), galleryPhotograph.getId()))
            .findFirst()
            .orElse(-1);
    if (index == -1)
    {
      LOGGER.severe("Index of galleryPhotograph " + galleryPhotograph.getId() + " not found in list.");
    }
    Optional<GalleryPhotograph> previous = Optional.empty();
    Optional<GalleryPhotograph> next = Optional.empty();
    if (index > 0)
    {
      previous = Optional.of(galleryPhotographs.get(index - 1));
    }
    if (index < galleryPhotographs.size() - 1 && index >= 0)
    {
      next = Optional.of(galleryPhotographs.get(index + 1));
    }

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
                            <div class="col align-middle">
                              %s
                            </div>
                            <div class="col">
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
                                      <div class="mb-3">
                                        <label for="galleryPhotographAngle">Angle select</label>
                                        <select class="form-control" name="galleryPhotographAngle" id="galleryPhotographAngle">
                                          <option value="unchanged">unchanged</option>
                                          <option value="1">Normal</option>
                                          <option value="2">Top/Rightside</option>
                                          <option value="3">Upside/Down</option>
                                          <option value="4">Bottom/Leftside</option>
                                          <option value="5">Leftside/Top</option>
                                          <option value="6">Ninetydegree/Clockwise</option>
                                          <option value="7">Rightside/Bottom</option>
                                          <option value="8">Ninetydegree/CounterClockwise</option>
                                        </select>
                                      </div>
                                      <div>
                                        <button type="submit" class="btn btn-primary">Submit</button>
                                      </div>
                                    </form>
                                  </div>
                              <img src="/yourpersonalphotographorganiser/images?id=%s&size=large" alt="%s"/>
                              <p>%s</p>
                              %s                                                 
                            </div>
                            <div class="col align-middle">
                              %s
                            </div>
                          </div>  
                        </div>                                   
                    """, previous.map(photo -> "<a href=\"/yourpersonalphotographorganiser/photos/" + photo.getId() + "\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-arrow-left-square-fill\"></i></a>").orElse(""),
            galleryPhotograph.getId(), galleryPhotograph.getName(), galleryPhotograph.getDescription(),
            galleryPhotograph.getPhotograph().getId(), galleryPhotograph.getName(), galleryPhotograph.getName(), description
            , next.map(photo -> "<a href=\"/yourpersonalphotographorganiser/photos/" + photo.getId() + "\" class=\"btn btn-primary btn-sm\"><i class=\"bi bi-arrow-right-square-fill\"></i></a>").orElse("")
    ));

    List<Comment> comments = commentService.getComments(galleryPhotograph);

    String commentsDescription = comments.stream().map(comment -> String.format("""
                                <blockquote class="blockquote mb-0">
                                    <p>%s</p>
                                    <footer class="blockquote-footer">%s (%s)</footer>
                                  </blockquote>
            """, comment.getComment(), comment.getAuthor(), comment.getSubmittedString())).collect(Collectors.joining());
//                              <p class="card-text"></p>
  //                            <h6 class="card-subtitle mb-2 text-muted"></h6>

    out.println(String.format("""
            <div class="container">
              <div class="row">
                <div class="col text-center">
                  <div class="card">
                    <div class="card-header">
                      Comments
                    </div>
                            <div class="card-body">
                    %s
                            </div>   
                  </div>             
                </div>
              </div>
            </div>
              """, commentsDescription));

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
            new Property("Taken", photograph.getTaken()),
            new Property("Hashstring", photograph.getHashstring()),
            new Property("Angle", angle),
            new Property("Angle integer", angle == null ? null : angle.getAngle())
    ).map(property ->
    {
      if (Objects.equals(EMPTY, property.value()))
      {
        if (Objects.equals(EMPTY, property.name()))
        {
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

    File file = photoService.getFile(photograph.getId()).orElseThrow(() -> new WebApplicationException("File with id " + photograph.getId() + " not found!"));
    try
    {
      List<GenericMetadata> metadata = ImageOperations.getAllMetadata(file);
      String contents = metadata.stream().map(GenericMetadata::toHtml).collect(Collectors.joining());
      out.println(String.format("""
              <div class="container">
                <div class="row">
                  <div class="col">
                    <table class="table table-bordered">
                      <thead>
                        <tr>
                          <th scope="col">Tagname</th>
                          <th scope="col">Tagtype</th>
                          <th scope="col">Description</th>
                          <th scope="col">Directory name</th>
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
                """, contents));

    } catch (ImageProcessingException e)
    {
    }

    out.println(HtmlUtils.getFooter());
  }

  @Transactional
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    String[] requestURI = req.getRequestURI().split("/");
    Long id = Long.valueOf(requestURI[requestURI.length - 1]);

    String galleryPhotographId = req.getParameter("galleryPhotographId");
    String galleryPhotographName = req.getParameter("galleryPhotographName");
    String galleryPhotographDescription = req.getParameter("galleryPhotographDescription");
    String angle = req.getParameter("galleryPhotographAngle");
    ImageAngle galleryPhotographAngle = angle == null || angle.equals("unchanged") ? null :
            ImageAngle.getAngle(Integer.valueOf(angle)).orElse(null);
    if (galleryPhotographId != null && !galleryPhotographId.isBlank())
    {
      LOGGER.finest(String.format("doPost galleryPhotograph %s,%s,%s", galleryPhotographId, galleryPhotographName, galleryPhotographDescription));
      photoService.updateGalleryPhotograph(Long.valueOf(galleryPhotographId), galleryPhotographName, galleryPhotographDescription, galleryPhotographAngle);
    }

    // Writing the message on the web page
    PrintWriter out = resp.getWriter();
    out.println(HtmlUtils.getHeader());
    out.println(String.format("""
            <div class="container">
              <div class="row">
                <div class="col">
                  <div class="alert alert-primary" role="alert">
                    Data submitted. <a href="/yourpersonalphotographorganiser/photos/%s" class="btn btn-primary btn-sm">Back to photo</a>
                  </div>
                </div>
              </div>
            </div>""", id));
    out.println(HtmlUtils.getFooter());
  }
}
