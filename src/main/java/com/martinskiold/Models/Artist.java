package com.martinskiold.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist.
 *
 * Created by martinskiold on 11/21/16.
 */
public class Artist {
    private final String mbid;
    private final String description;
    private final List<Album> albums;

    public Artist(String mbid, String description, List<Album> albums)
    {
        if(mbid == null)
        {
            this.mbid = "N/A";
        }
        else
        {
            this.mbid = mbid;
        }

        if(description == null)
        {
            this.description = "N/A";
        }
        else
        {
            this.description = description;
        }

        if(albums == null)
        {
            this.albums = new ArrayList<Album>();
        }
        else
        {
            this.albums = albums;
        }
    }

    public String getMbid()
    {
        return mbid;
    }
    public String getDescription()
    {
        return description;
    }
    public List<Album> getAlbums(){
        return albums;
    }
}
