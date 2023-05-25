package com.mrbear.yppo.enums;

/**
 *
 * <p>Indicates the different sizes that are possible in the displaying
 * of pictures. BIG being un-scaled.</p>
 * <img src="../../images/ImageSize.png"/>
 * @author maartenl
 *
 * @startuml
 * "java.lang.Enum<ImageSize>" <|-- enum ImageSize
 * ImageSize : +BIG
 * ImageSize : +LARGE
 * ImageSize : +MEDIUM
 * ImageSize : +THUMB
 * ImageSize : -maxHeight
 * ImageSize : -maxWidth
 * ImageSize : +getHeight() : Integer
 * ImageSize : +getWidth() : Integer
 * @enduml
 */
public enum ImageSize
{

    /**
     * Indicates the pictures is not to be modified, but shown
     * in its true dimensions.
     */
    BIG(),
    MEDIUM(350, 350), THUMB(100, 100), LARGE(1024,1024);

    private final Integer maxHeight;
    private final Integer maxWidth;

    ImageSize()
    {
        maxHeight = null;
        maxWidth = null;
    }

    ImageSize(Integer width, Integer height)
    {
        this.maxHeight = height;
        this.maxWidth = width;
    }

    /**
     * Retrieves the height of the picture.
     * @return Integer indicating the height in pixels.
     */
    public Integer getHeight()
    {
        return maxHeight;
    }

     /**
      * Retrieves the width of the picture.
      * @return Integer indicating the width in pixels.
      */
    public Integer getWidth()
    {
        return maxWidth;
    }
}
