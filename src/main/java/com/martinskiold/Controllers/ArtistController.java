package com.martinskiold.Controllers;

import com.martinskiold.Exceptions.ResourceNotFoundException;
import com.martinskiold.Services.ArtistService;
import com.martinskiold.Models.Artist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.concurrent.Future;

/**
 * RESTController that handles endpoints for the Artist resource.
 *
 * Created by martinskiold on 11/24/16.
 */
@RestController
public class ArtistController {
    private static final Logger log = LoggerFactory.getLogger(ArtistController.class);
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService)
    {
        this.artistService = artistService;
    }

    @RequestMapping(value = "/artist", method = RequestMethod.GET)
    public Artist artist(@RequestParam(value="mbid") String mbid) throws Exception
    {
        Artist artist = new Artist(mbid, null, null);
        try
        {
            Future<Artist> artistResponse = artistService.getArtist(mbid);

            while(!artistResponse.isDone()){Thread.sleep(20);}  //Waits for response completion

            artist = artistResponse.get();
        }
        catch (Exception e)
        {
            //Invoke 404 error message only if ResourceNotFoundException has been thrown
            if(e.getCause() instanceof ResourceNotFoundException)
            {
                throw e;
            }
        }
        return artist;
    }
}
