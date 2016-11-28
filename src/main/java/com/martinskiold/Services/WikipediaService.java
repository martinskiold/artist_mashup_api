package com.martinskiold.Services;

import com.martinskiold.Services.JSONResponseExtractors.WikipediaResponseExtractor;
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
 * Retrieves information from the Wikipedia API.
 *
 * Created by martinskiold on 11/26/16.
 */
@Service
public class WikipediaService {
    private static final Logger log = LoggerFactory.getLogger(WikipediaService.class);
    private final RestTemplate restTemplate;

    public WikipediaService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     *  Retrieves artist description.
     *  The method runs on a separate thread using Spring MVC Async annotation.
     */
    @Async
    public Future<String> getArtistDescription(String wikiId) throws Exception
    {
        log.info("Looking up artist on wikipedia with wikipedia id: [" + wikiId + "]");

        try{
            ResponseEntity<String> artistResponse = restTemplate.getForEntity("https://en.wikipedia.org/w/api.php?action=query&format=json&prop=extracts&exintro=true&redirects=true&titles={artistId}",String.class, wikiId);

            WikipediaResponseExtractor wikiExtractor = new WikipediaResponseExtractor(artistResponse);
            return new AsyncResult<>(wikiExtractor.extractWikiText());
        }
        catch(HttpClientErrorException e)
        {
            return new AsyncResult<>(null);
        }
    }
}
