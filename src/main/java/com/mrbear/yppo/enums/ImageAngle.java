package com.mrbear.yppo.enums;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

/**
 *
 * <p>
 * Indicates the rotation required for a picture, to display it properly.
 * Also indicates the angle at which the original pictures were taken,
 * if supported by the camera/mobile/device.</p>
 * <img src="../../images/ImageAngle.png"/>
 *
 * @author maartenl
 *
 * @startuml
 * "java.lang.Enum<ImageAngle>" <|-- enum ImageAngle
 * ImageAngle : +NORMAL
 * ImageAngle : +UPSIDE_DOWN
 * ImageAngle : +NINETYDEGREE_CLOCKWISE
 * ImageAngle : +NINETYDEGREE_COUNTER_CLOCKWISE
 * ImageAngle : +getAngle() : Integer
 * ImageAngle : +toString() : String
 * ImageAngle : +getAngle(angle : int) : ImageAngle
 * @enduml
 * @see <a href="http://www.impulseadventure.com/photo/exif-orientation.html">EXIF-orientation</a>
 */
public enum ImageAngle
{

    /**
     * Normal angle. (1)
     * No rotation is required.
     * <pre>        ___
     *  _____/___|
     * /         |
     * |    O    |
     * |_________|</pre>
     */
    NORMAL(1, "Top/Left side"),
    /**
     * Not used. (2)
     */
    TOP_RIGHTSIDE(2, "Top/Right side"),
    /**
     * Photocamera was upside down. (3)
     * 180 degrees rotation either way.
     * <pre>  _________
     * |         |
     * |    O    |
     * |_________|
     * \___|
     * </pre>
     */
    UPSIDE_DOWN(3, "Bottom/Right side"),
    /**
     * Not used.
     */
    BOTTOM_LEFTSIDE(4, "Bottom/Left side"),
    /**
     * Not used.
     */
    LEFTSIDE_TOP(5, "Left side/Top"),
    /**
     * Photocamera was turned 90 degrees counter clockwise originally. (6)
     * Rotation 90 degrees clockwise required.
     * <pre>   _______
     *  |       |
     *  |       |
     * /|   O   |
     * ||       |
     * |________|</pre>
     */
    NINETYDEGREE_CLOCKWISE(6, "Right side/Top"),
    /**
     * Not used.
     */
    RIGHTSIDE_BOTTOM(7, "Right side/Bottom"),
    /**
     * Photocamera was turned 90 degrees clockwise originally. (8)
     * Rotation 90 degrees counter clockwise required.
     * <pre>   _______
     *  |       |
     *  |       |
     *  |   O   |\
     *  |       ||
     *  |________|</pre>
     */
    NINETYDEGREE_COUNTER_CLOCKWISE(8, "Left side/Bottom");

    private final Integer angle;
    private final String description;

    ImageAngle(Integer angle, String description)
    {
        this.angle = angle;
        this.description = description;
    }

    @Override
    public String toString()
    {
        return description;
    }

    /**
     * Retrieves the angle of the picture as stored in image information.
     *
     * @return Integer indicating the angle.
     */
    public Integer getAngle()
    {
        return angle;
    }

    /**
     * Retrieves the enum corresponding to the image angle as stored
     * in the picture.
     *
     * @return ImageAngle indicating the angle.
     */
    public static Optional<ImageAngle> getAngle(Integer angle)
    {
        return Arrays.stream(ImageAngle.values())
            .filter(imageAngle -> Objects.equals(angle, imageAngle.getAngle()))
            .findFirst();
    }

    public String getName()
    {
        return name();
    }
}
