package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.List;

import net.deuce.clmanager.domain.MessagePhoto;
import net.deuce.clmanager.gwt.main.client.MessagePhotoService;
import net.deuce.clmanager.gwt.main.client.model.ImageModel;

public class MessagePhotoServiceImpl extends BaseServiceImpl implements MessagePhotoService {
    
    public MessagePhotoServiceImpl() {
    }


    public List getMessagePhotos() throws Exception {
        openSession();
        try {
            List result = new ArrayList();
            for (MessagePhoto mp : getMessagePhotoDao().findAll()) {
                result.add(new ImageModel(mp.getId(), "", mp.getPath()));
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
    
}
