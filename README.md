
# Configuration

The Location table refers to the root where all the photoslocations are stored on the filesystem.

It's possible to have more than one location.

For example: /home/maartenl/gallery3/var/albums

# Technology stack

It's a deployable .war file for application servers.

Uses the following technology:
- Backend
    - java 17
    - Maven
    - Jakarta EE 10 (to wit: Jakarta Servlets, Jakarta JPA, Jakarta Batch Processing, Jakarta REST Services)
- Frontend:
    - Bootstrap, jquery, fancybox

Should run fine on most application servers. I personally have good/easy results running it using Payara Micro.

