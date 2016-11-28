package com.martinskiold.Models;

/**
 * A music album.
 *
 * Created by martinskiold on 11/25/16.
 */
public class Album {
    private final String id;
    private final String title;
    private String image;

    public Album(String id, String title, String image)
    {
        this.id = id;
        this.title = title;
        if(image != null)
        {
            this.image = image;
        }
        else
        {
            this.image = "N/A";
        }
    }

    public String getId()
    {
        return id;
    }
    public String getTitle()
    {
        return title;
    }
    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        if(image != null)
        {
            this.image=image;
        }
        else
        {
            this.image = "N/A";
        }
    }
}
