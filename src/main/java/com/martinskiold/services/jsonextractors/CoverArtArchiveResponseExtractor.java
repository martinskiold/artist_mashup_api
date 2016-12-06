package com.martinskiold.services.jsonextractors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Extracts information from a JSON response from the CoverArtArchive API.
 *
 * Created by martinskiold on 11/24/16.
 */
public class CoverArtArchiveResponseExtractor {

    private static final Logger log = LoggerFactory.getLogger(CoverArtArchiveResponseExtractor.class);
    private ObjectMapper mapper;
    private JsonNode root;

    public CoverArtArchiveResponseExtractor(ResponseEntity<String> response)
    {
        mapper = new ObjectMapper();
        try
        {
            //Parses body of response to JSON
            root = mapper.readTree(response.getBody());
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String extractAlbumCoverUrl()
    {
        return root.findValue("image").asText();
    }


}
