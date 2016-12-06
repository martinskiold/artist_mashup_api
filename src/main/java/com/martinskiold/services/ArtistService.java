package com.martinskiold.services;

import com.martinskiold.exceptions.ResourceNotFoundException;
import com.martinskiold.models.apis.MusicBrainzArtist;
import com.martinskiold.models.Album;
import com.martinskiold.models.Artist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Retrieves information about Artist's.
 *
 * Created by martinskiold on 11/26/16.
 */
@Service
public class ArtistService {
    private static final Logger log = LoggerFactory.getLogger(ArtistService.class);

    //RestTemplate is threadsafe according to http://stackoverflow.com/questions/22989500/is-resttemplate-thread-safe
    private final RestTemplate restTemplate;
    private final MusicBrainzService mbService;
    private final CoverArtArchiveService caaService;
    private final WikipediaService wikiService;

    public ArtistService(RestTemplateBuilder restTemplateBuilder, MusicBrainzService mbService, CoverArtArchiveService caaService, WikipediaService wikiService)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.mbService = mbService;
        this.caaService = caaService;
        this.wikiService = wikiService;
    }

    /**
     *  Retrieves an Artist by querying MusicBrainz, CoverArtArchive and Wikipedia API for information corresponding to the Artist's mbid (MusicBrainz identifier).
     *  The method runs on a separate thread using Spring MVC Async annotation.
     */
    @Async
    public Future<Artist> getArtist(String artistMbid) throws Exception
    {
        Artist artist = new Artist(artistMbid, null, null);

        try
        {
            //Gets Artist info from MusicBrainz API
            Future<MusicBrainzArtist> mbArtistResponse = mbService.getArtist(artistMbid);
            waitForResponses(Collections.singletonList(mbArtistResponse));
            MusicBrainzArtist mbArtist = mbArtistResponse.get();


            //Gets wiki description from Wikipedia API and album cover urls from CoverArtArchive API
            Future<String> wikiArtistDescriptionResponse = wikiService.getArtistDescription(mbArtist.getWikiId());
            List<Future<String>> caaAlbumCoverResponses = new ArrayList<Future<String>>();
            for (Album album : mbArtist.getAlbums())
            {
                caaAlbumCoverResponses.add(caaService.getAlbumCoverUrl(album.getId()));
            }


            //Wait for responses to finish UPDATE: NOT NEEDED due to wait in Future<..> get method
            //waitForResponses(Collections.singletonList(wikiArtistDescriptionResponse));
            //waitForResponses(caaAlbumCoverResponses);


            //Adding cover urls to albums
            int i = 0;
            for(Future<String> albumResponse : caaAlbumCoverResponses)
            {
                String albumCoverUrl = albumResponse.get();
                if(albumCoverUrl!=null) {
                    mbArtist.getAlbums().get(i).setImage(albumCoverUrl);
                }
                i++;
            }

            //Creates an Artist object from the information retrieved from different APIs
            artist = new Artist(artistMbid, wikiArtistDescriptionResponse.get(), mbArtist.getAlbums());

        }
        catch(Exception e)
        {
            //Rethrow exception only if its an ResourceNotFoundException
            //Or throw WebApplicationException
            if(e.getCause() instanceof ResourceNotFoundException)
            {
                throw e;
            }
        }

        return new AsyncResult<>(artist);
    }

    /**
     *  DEPRECATED - Future's get method automatically waits until response is ready.
     *  Waits until the responses are complete. No idling. Waiting is implemented by sleeping the thread at 20ms intervals.
     */
    private <T> void waitForResponses(List<Future<T>> responses)
    {
        boolean allThreadsFinished = true;
        while(true)
        {
            for (Future<T> response : responses)
            {
                if(!response.isDone())
                {
                    allThreadsFinished = false;
                    break;
                }
            }

            if(!allThreadsFinished)
            {
                //Resets flag
                allThreadsFinished = true;
                try
                {
                    //Check again in 20 milliseconds
                    Thread.sleep(20);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                //If all responses are finished - break outer loop
                break;
            }
        }

    }

}
