package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.domain.Image;
import net.deuce.clmanager.domain.Post;
import net.deuce.clmanager.gwt.main.client.ImageService;
import net.deuce.clmanager.gwt.main.client.model.ImageModel;

public class ImageServiceImpl extends BaseServiceImpl implements ImageService {
    
    public ImageServiceImpl() {
    }

    public List getImages(Long postId) throws Exception {
        openSession();
        try {
            Post post = getPostDao().read(postId);
            if (post == null) {
                throw new RuntimeException("Post not found with id: "+ postId);
            }
            List<Image> images = (List<Image>)getImageDao().findByPost(post);
            List result = new ArrayList(images.size());
            for (Image i : images) {
                result.add(buildImageModel(i));
            }
            return result;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
    private ImageModel buildImageModel(Image image) {
        ImageModel model = new ImageModel();
        model.setId(image.getId());
        model.setPath(image.getPath());
        model.setUrl(image.getUrl());
        return model;
    }
    
    @Override
    protected void doUnexpectedFailure(Throwable e) {
        e.printStackTrace();
    }
    
}
