package com.martinskiold.Services;

import com.martinskiold.Exceptions.ResourceNotFoundException;
import com.martinskiold.Models.APIs.MusicBrainzArtist;
import com.martinskiold.Models.Album;
import com.martinskiold.Services.JSONResponseExtractors.MusicBrainzResponseExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Retrieves information from the MusicBrainz API.
 *
 * Created by martinskiold on 11/26/16.
 */
@Service
public class MusicBrainzService {
    private static final Logger log = LoggerFactory.getLogger(MusicBrainzService.class);
    private final RestTemplate restTemplate;

    public MusicBrainzService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     *  Retrieves artist information.
     *  The method runs on a separate thread using Spring MVC Async annotation.
     */
    @Async
    public Future<MusicBrainzArtist> getArtist(String artistMbid) throws Exception
    {
        log.info("Looking up artist with mbid: [" + artistMbid + "]");

        try {
            ResponseEntity<String> artistResponse = restTemplate.getForEntity("http://musicbrainz.org/ws/2/artist/{mbid}?&fmt=json&inc=url-rels+release-groups", String.class, artistMbid);

            //Extracts information from response
            MusicBrainzResponseExtractor mbExtractor = new MusicBrainzResponseExtractor(artistResponse);
            String wikiId = mbExtractor.extractWikiArtistId();
            List<Album> albums = mbExtractor.extractAllAlbums();

            return new AsyncResult<>(new MusicBrainzArtist(wikiId,albums));

        }catch(HttpClientErrorException e)
        {
            //Invoke 404 Not Found error message
            String eMessage = "Artist with mbid '" + artistMbid + "' do not exist";
            throw new ResourceNotFoundException(eMessage, eMessage);
        }
    }
}
