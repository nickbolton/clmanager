package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.deuce.clmanager.gwt.main.client.FlickrService;
import net.deuce.clmanager.gwt.main.client.model.ExternalPhoto;
import net.deuce.clmanager.gwt.main.client.model.PhotoServiceResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.people.User;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FlickrServiceImpl extends RemoteServiceServlet implements FlickrService {

    private List EMTPY_RESULT = new ArrayList(0);
    
    private Log log = LogFactory.getLog(getClass());

    public PhotoServiceResponse getPhotos(String username) {
        try {
            List photos = new ArrayList();
            Flickr flickr = new Flickr("cfb3fa6e836b5f2402e5d706ce5aef25");
            username = "badshowersinger";
            User user = flickr.getPeopleInterface().findByUsername(username);
            String userId = user.getId();

            // prepare our photo search
            SearchParameters usernameSearch = new SearchParameters();
            usernameSearch.setUserId(userId);
            PhotosInterface photosInterface = flickr.getPhotosInterface();

            PhotoList result = photosInterface.search(usernameSearch, 100, 1);
            Iterator itr = result.iterator();
            while (itr.hasNext()) {
                com.aetrion.flickr.photos.Photo entry = (com.aetrion.flickr.photos.Photo)itr.next();
                ExternalPhoto photo = new ExternalPhoto();
                photo.setId(entry.getId());
                photo.setTitle(entry.getTitle());
                photo.setThumbnail(entry.getThumbnailUrl());
                photo.setStandard(entry.getMediumUrl());
                photo.setService("flickr");
                photos.add( photo );
            }
            return new PhotoServiceResponse(photos);
        } catch (Exception fe) {
            log.error("Failed flickr call for user: " + username, fe);
            return new PhotoServiceResponse(fe.getMessage());
        }
    }
}
