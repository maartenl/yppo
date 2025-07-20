[![Build Status](https://app.travis-ci.com/maartenl/yppo.svg?branch=master)](https://app.travis-ci.com/maartenl/yppo)

An attempt from me at creating a Photo gallery.

It has almost everything you need for home use, I do not recommend it for website use.

# Configuration

When deploying the war file onto an application server, the url to access
will probably be:

http://localhost:8080/yourpersonalphotographorganiser/

The Location table refers to the root where all the photos are stored on the filesystem.

It's possible to have more than one location.

For example: /home/maartenl/gallery3/var/albums

# Requirements

1. simple database, easy to make changes directly, if so required
2. used for home use
3. no authentication or authorization required
4. helps me to understand the jdk 17, jakarta 10, etc. by using
   all the new stuff in there.
5. absolutely NO changing of the photographs, all changes are done
   in java, in memory. *)
6. flexible in where these photographs are located (no need to keep them in
   the webdir, for example)
7. make it impossible to add the same photograph twice (make it easy to
   verify if you already have a photograph in there) **)
8. verify photographs, check to see that the photograph in the database, is
   equivalent to the photograph in the file (a nice check for possible harddrive failure)
9. quickly initialise an empty database, by importing a directory structure as galleries
10. automatically adapt the database if photographs are moved on the filesystem

*) I've had too many instances where:
1. changing files from webinterface is a security risk, and requires proper
   access rights.
2. changing files causes the extra data present in the jpegs put there by
   photocameras to be discarded
3. changing files potentially causes deterioration of the quality of the
   jpegs
4. changing files has sometimes caused the file to be damaged in some way
5. changing files makes it impossible (or at the very least, extremely hard)
   to determine if the photo is already present in your collection
6. webbrowsers currently have support for film clips in format .mp4 and .webm (without external plugins, I think)
   there's support in this package for mp4,webm and avi, but the avi stuff doen't work great.
7. 
**) Sure, you can have the same photograph in multiple galleries or even in
the same gallery, but these will refer to the SAME photograph. Every photograph
is unique.

# Creating a new Gallery

You can create a new gallery by typing the name of the new Gallery on the main Galleries page.

Then you can find the new Gallery probably at the bottom of the Galleries, en you can select it, and then edit
its settings (and the Gallery ID of its parent, if it's not supposed to be on the main branch).

# Adding photos to a Gallery

First of all, you can verify if there are any photographs that are not yet allocated to a gallery:

select * 
from Photograph 
where not exists (select 1 from GalleryPhotograph where photograph_id = Photograph.id);

You can simply add those photographs to a gallery of your choice, for instance:

insert into GalleryPhotograph (gallery_id, photograph_id, name, description, sortorder)
select 77, id, filename, null, 0 from Photograph where relativepath like 'huis/dak'
and not exists (select 1 from GalleryPhotograph where photograph_id=Photograph.id);

# Sorting photos in a Gallery

Usually, photos added to a gallery are sorted by their timestamp. Sometimes, that timestamp is not available
(for example when using photos retrieved from a social media platform). In that case, it might be possible to get
the timestamp from the filename for setting the sortorder.

For example if you have many files of the following format: "IMG-20191030-WA0020.jpg"

You could try something like this:

update GalleryPhotograph g inner join (
select * from Photograph p) as p on p.id = g.photograph_id
Set g.sortorder = concat(substring(p.filename, 5, 8), substring(p.filename, 16, 4))
where g.gallery_id = 76;

# Technology stack

It's a deployable .war file for application servers.

Uses the following technology:
- Backend
    - java 17
    - Maven
    - Jakarta EE 10 (to wit: Jakarta Servlets, Jakarta JPA, Jakarta Batch Processing, Jakarta REST Services)
    - Photograph metadata library from drewnoakes
- Frontend:
    - Bootstrap, jquery, fancybox

Should run fine on most application servers. I personally have good/easy results running it using Payara Micro.

# References 

https://drewnoakes.com/code/exif/
metadata-extractor lets you access the metadata in digital images and video via a simple API.

You can find some little more accessible info on
http://randomthoughtsonjavaprogramming.blogspot.com

Icons at:
http://www.iconarchive.com/show/folder-icons-by-iconshock/gallery-icon.html
http://findicons.com/icons/gallery.png
http://icons-search.com/icons/movie.aspx
http://www.designdownloader.com/i/?id=pen_5-20110811153719-00003

Fancybox javascript library at:
https://fancyapps.com/fancybox/plugins/html/#videos
