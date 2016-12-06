package com.martinskiold.models.apis;

import com.martinskiold.models.Album;

import java.util.ArrayList;
import java.util.List;

/**
 * An artist from the MusicBrainz API.
 *
 * Created by martinskiold on 11/27/16.
 */
public class MusicBrainzArtist {

    private String wikiId;
    private List<Album> albums;

    public MusicBrainzArtist(String wikiId, List<Album> albums)
    {
        this.wikiId = wikiId;
        if(albums == null)
        {
            this.albums = new ArrayList<Album>();
        }
        else
        {
            this.albums = albums;
        }
    }

    public String getWikiId()
    {
        return wikiId;
    }
    public List<Album> getAlbums()
    {
        return albums;
    }

    public void setWikiId(String wikiId)
    {
        this.wikiId = wikiId;
    }
    public void setAlbums(List<Album> albums)
    {
        if(albums != null)
        {
            this.albums = albums;
        }
    }

}
