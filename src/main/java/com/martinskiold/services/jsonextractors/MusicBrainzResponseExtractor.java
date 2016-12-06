package com.martinskiold.services.jsonextractors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinskiold.models.Album;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts information from a JSON response from the MusicBrainz API.
 *
 * Created by martinskiold on 11/24/16.
 */
public class MusicBrainzResponseExtractor {

    private static final Logger log = LoggerFactory.getLogger(MusicBrainzResponseExtractor.class);
    private ObjectMapper mapper;
    private JsonNode root;

    public MusicBrainzResponseExtractor(ResponseEntity<String> response)
    {
        mapper = new ObjectMapper();
        try {
            root = mapper.readTree(response.getBody());
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Extracts all albums from the response.
     */
    public List<Album> extractAllAlbums()
    {
        ArrayList<Album> albums = new ArrayList<Album>();

        JsonNode mbAlbums = root.findPath("release-groups");
        if(mbAlbums.isArray())
        {
            for(JsonNode release : mbAlbums)
            {
                if(release.findValue("primary-type").asText().equals("Album"))
                {
                    Album album = new Album(release.findValue("id").asText(),release.findValue("title").asText(), null);
                    albums.add(album);
                }
            }
        }
        else
        {
            if(mbAlbums.findValue("primary-type").asText().equals("Album"))
            {
                Album album = new Album(mbAlbums.findValue("id").asText(),mbAlbums.findValue("title").asText(), null);
                albums.add(album);
            }
        }
        return albums;
    }

    /**
     * Extracts the artist's Wikipedia id from the response.
     */
    public String extractWikiArtistId()
    {
        String wikiArtistId = null;
        JsonNode mbApiRelations = root.findPath("relations");
        if(mbApiRelations.isArray())
        {
            for(JsonNode relation : mbApiRelations)
            {
                if(relation.findValue("type").asText().equals("wikipedia"))
                {
                    String urlResource = relation.findValue("resource").asText();
                    String[] extractedParts = urlResource.split("/");
                    if(extractedParts.length > 0)
                    {
                        wikiArtistId = extractedParts[extractedParts.length-1];
                    }
                }
            }
        }
        else
        {
            if(mbApiRelations.findPath("type").asText().equals("wikipedia"))
            {
                String urlResource = mbApiRelations.findValue("resource").asText();
                String[] extractedParts = urlResource.split("/");
                if(extractedParts.length > 0)
                {
                    wikiArtistId = extractedParts[extractedParts.length-1];
                }
            }
        }
        return wikiArtistId;
    }
}
