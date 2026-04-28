package com.mrbear.yppo;

import com.mrbear.yppo.entities.Photograph;
import com.mrbear.yppo.services.PhotoService;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "Information", urlPatterns = {"/information"})
public class InformationServlet extends HttpServlet
{

  @Inject
  private PhotoService photoService;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    List<Photograph> list = photoService.getUnallocatedPhotographs();
    // Setting up the content type of web page
    response.setContentType("text/html");
    // Writing the message on the web page
    PrintWriter out = response.getWriter();

    String description = list.stream()
            .map(this::createPhotographEntry)
            .collect(Collectors.joining());
    out.println(HtmlUtils.getHeader());
    out.println(String.format("""
                <div class="container">
                  <div class="row">
                    <div class="col">
                      <h1>Information</h1>
                      <a href="/yourpersonalphotographorganiser" class="btn btn-primary btn-sm">Back to main</a>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col">
                      <div class="form-group">
                        <form method="POST">
                          <div class="mb-3">
                            <label for="relativePath" class="form-label">Relative path</label>
                            <input type="text" class="form-control" name="relativePath" id="relativePath" aria-describedby="relativePathHelp" value="">
                            <small id="relativePathHelp" class="form-text text-muted">Fill out the relative path to match photographs to. (will accept %% wildcards)</small>
                          </div>
                          <div class="mb-3">
                            <label for="galleryId" class="form-label">Gallery Id</label>
                            <input type="number" class="form-control" name="galleryId" id="galleryId" aria-describedby="galleryIdHelp" value="">
                            <small id="galleryIdHelp" class="form-text text-muted">The Id of the Gallery to which the photographs matching the relative path entered above need to be assigned.</small>
                          </div>
                          <div>
                            <button type="submit" class="btn btn-primary">Submit</button>
                            <small class="form-text text-muted">Will only assign Photographs to the Gallery that have not already been assigned and match relative path.</small>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col">
                      <table class="table table-bordered">
                        <thead>
                          <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Path</th>
                            <th scope="col">Filename</th>
                          </tr>
                        </thead>
                        <tbody>
                        %s
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
            """, description));
    out.println(HtmlUtils.getFooter());
  }

  private String createPhotographEntry(Photograph photograph)
  {
    return String.format("""
                    <tr class="table-light">
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                    </tr>
                    """
            , photograph.getId(), photograph.getRelativepath(), photograph.getFilename());
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {

    String relativePath = req.getParameter("relativePath");
    String galleryId = req.getParameter("galleryId");
    if (relativePath != null && !relativePath.isBlank() && galleryId != null && !galleryId.isBlank())
    {
      photoService.assignPhotographsToGallery(relativePath, Long.valueOf(galleryId));
    }

    // Writing the message on the web page
    PrintWriter out = resp.getWriter();
    out.println(HtmlUtils.getHeader());
    out.println("""
            <div class="container">
              <div class="row">
                <div class="col">
                  <div class="alert alert-primary" role="alert">
                    Data submitted. <a href="/yourpersonalphotographorganiser/galleries" class="btn btn-primary btn-sm">Back to gallery</a>
                  </div>
                </div>
              </div>
            </div>""");
    out.println(HtmlUtils.getFooter());
  }
}
