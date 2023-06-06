package com.mrbear.yppo;

public class HtmlUtils
{

    private HtmlUtils()
    {
        // add a private constructor to hide the public one.
    }

    public static String getHeader()
    {
        return """
                <!doctype html>
                <html lang="en">
                  <head>
                    <meta charset="utf-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1">
                    <title>Your Personal Photograph Organiser</title>
                    <link href="/yourpersonalphotographorganiser/css/bootstrap.css" rel="stylesheet">
                    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
                    <link
                            rel="stylesheet"
                            href="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.css"
                    />
                  </head>
                  <body>
                  """;
    }

    public static String getFooter()
    {
        return """
                    <script src="/yourpersonalphotographorganiser/js/jquery-3.7.0.min.js"></script>
                    <script src="/yourpersonalphotographorganiser/js/bootstrap.bundle.js"></script>
                    <script src="https://cdn.jsdelivr.net/npm/@fancyapps/ui@5.0/dist/fancybox/fancybox.umd.js"></script>
                    <script>
                      Fancybox.bind("[data-fancybox]", {
                        // Your custom options
                        groupAll: true,
                      });
                    </script>
                  </body>
                </html>
                """;
    }
}
