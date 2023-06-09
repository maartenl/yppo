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

@WebServlet(name = "Galleries", urlPatterns = {"/galleries"})
public class GalleryServlet extends HttpServlet
{

    @Inject
    private GalleryService galleryService;

    private String createTree(Gallery gallery, List<Gallery> allGalleries)
    {
        List<Gallery> kids = allGalleries.stream()
                .filter(x -> Objects.equals(x.getParentId(), gallery.getId()))
                .sorted(Comparator.comparingInt(Gallery::getSortorder).thenComparing(Gallery::getCreationDate))
                .toList();
        String highlight = "/yourpersonalphotographorganiser/images/gallery.png";
        if (gallery.getHighlight() != null) {
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
                              <img width="45px" height="45px" src="%s"><a href="galleries/%d">%s</a>
                            </div>
                            %s
                            <ul class="list-group">%s</ul>
                          </div>  
                        </li>
                        """, highlight, gallery.getId(), gallery.getName(), gallery.getDescription(),
                subTree);
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
                """, description));
        out.println(HtmlUtils.getFooter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        super.doPost(req, resp);
    }
}
