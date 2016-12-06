package com.martinskiold.services;

import com.martinskiold.services.jsonextractors.CoverArtArchiveResponseExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * Retrieves information from the CoverArtArchive API.
 *
 * Created by martinskiold on 11/26/16.
 */
@Service
public class CoverArtArchiveService {
    private static final Logger log = LoggerFactory.getLogger(CoverArtArchiveService.class);
    private final RestTemplate restTemplate;

    public CoverArtArchiveService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     *  Retrieves an album cover url.
     *  The method runs on a separate thread using Spring MVC Async annotation.
     */
    @Async
    public Future<String> getAlbumCoverUrl(String albumMbid) throws Exception
    {
        log.info("Looking up album with mbid: [" + albumMbid + "]");

        try
        {
            ResponseEntity<String> albumResponse  = restTemplate.getForEntity("http://coverartarchive.org/release-group/{mbid}", String.class, albumMbid);

            CoverArtArchiveResponseExtractor albumExtractor = new CoverArtArchiveResponseExtractor(albumResponse);
            return new AsyncResult<>(albumExtractor.extractAlbumCoverUrl());
        }
        catch(HttpClientErrorException e)
        {
            return new AsyncResult<>(null);
        }

    }
}
