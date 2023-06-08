package com.mrbear.yppo;

import com.mrbear.yppo.entities.Log;
import com.mrbear.yppo.services.LogService;
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

@WebServlet(name = "Logs", urlPatterns = {"/logs"})
public class LogServlet extends HttpServlet
{

  @Inject
  private LogService logService;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    boolean all = request.getParameter("all") != null;

    List<Log> logs = all ? logService.getPersistedLogs() : logService.getLog();
    // Setting up the content type of web page
    response.setContentType("text/html");
    // Writing the message on the web page
    PrintWriter out = response.getWriter();

    String description = logs.stream()
            .map(this::createLog)
            .collect(Collectors.joining());
    out.println(HtmlUtils.getHeader());
    out.println(String.format("""
                <div class="container">
                  <div class="row">
                    <div class="col">
                      <h1>Logs</h1>
                      <a href="/yourpersonalphotographorganiser" class="btn btn-primary btn-sm">Back to main</a>
                      <table class="table table-bordered">
                        <thead>
                          <tr>
                            <th scope="col">Id</th>
                            <th scope="col">Resource</th>
                            <th scope="col">Message</th>
                            <th scope="col">Datetime</th>
                            <th scope="col">Level</th>
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

  private String createLog(Log log)
  {
    String description = "";
    if (log.getDescription() != null && !log.getDescription().isBlank())
    {
      description = String.format("""
              <tr>
                <td colspan="5">%s</td>
              </tr>
              """, log.getDescription());
    }
    String level = switch(log.getLoglevel()){
      case INFO -> "table-light";
      case WARNING -> "table-warning";
      case ERROR -> "table-danger";
    };
    return String.format("""
                    <tr class="%s">
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                      <td>%s</td>
                    </tr>
                    """
            , level, log.getId(), log.getSource(), log.getMessage(), log.getCreationDate(), log.getLoglevel()) + description;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    super.doPost(req, resp);
  }
}
