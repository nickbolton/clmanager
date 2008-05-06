package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.MessagePhoto;
import net.deuce.clmanager.domain.MessagePhotoAssociation;
import net.deuce.clmanager.domain.MessageTemplate;
import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.model.ImageModel;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;

public class MessageTemplateServiceImpl extends BaseServiceImpl implements MessageTemplateService {
    
    public MessageTemplateServiceImpl() {
    }

    public List getMessageTemplates() throws Exception {
        openSession();
        try {
            List result = new ArrayList();
            for (MessageTemplate mt : getMessageTemplateDao().findAll()) {
                Category c = mt.getDefaultCategory();
                MessageTemplateModel model = new MessageTemplateModel(mt.getId(), mt.getName(), mt.getMessage(), c != null ? c.getName() : null);
                List photos = new ArrayList();
                for (MessagePhotoAssociation mp : (Set<MessagePhotoAssociation>)mt.getPhotos()) {
                    photos.add(new ImageModel(mp.getId(), "", mp.getMessagePhoto().getPath()));
                }
                model.setChildren(photos);
                result.add(model);
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

    public void deleteMessageTemplate(Long id) throws Exception {
        openSession();
        try {
            MessageTemplate mt = getMessageTemplateDao().read(id);
            if (mt != null) {
                getMessageTemplateDao().delete(mt);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public MessageTemplateModel createMessageTemplate(MessageTemplateModel mtm) throws Exception {
        openSession();
        try {
            MessageTemplate mt = new MessageTemplate();
            mt.setName(mtm.getName());
            mt.setMessage(mtm.getMessage());
            
            Set photos = new HashSet();
            for (ImageModel im : (List<ImageModel>)mtm.getChildren()) {
                MessagePhoto mp = getMessagePhotoDao().read(im.getId());
                MessagePhotoAssociation mpa = new MessagePhotoAssociation();
                mpa.setMessagePhoto(mp);
                mpa.setMessageTemplate(mt);
                getMessagePhotoAssociationDao().create(mpa);
                photos.add(mpa);
            }
            if (photos.size() > 0) {
                mt.setPhotos(photos);
            }
            getMessageTemplateDao().create(mt);
            mtm.setId(mt.getId());
            return mtm;
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }

    public void updateMessageTemplate(MessageTemplateModel mtm) throws Exception {
        openSession();
        try {
            MessageTemplate mt = getMessageTemplateDao().read(mtm.getId());
            mt.setName(mtm.getName());
            mt.setMessage(mtm.getMessage());
            if (mtm.getCategoryName() != null) {
                Category cat = getCategoryDao().findUniqueByName(mtm.getCategoryName());
                if (cat == null) {
                    getLog().warn("No category found named: " + mtm.getCategoryName());
                } else {
                    mt.setDefaultCategory(cat);
                }
            } else {
                mt.setDefaultCategory(null);
            }
            Set photos = new HashSet();
            for (MessagePhotoAssociation mpa : (Set<MessagePhotoAssociation>)mt.getPhotos()) {
                mpa.getId();
                getMessagePhotoAssociationDao().delete(mpa);
            }
            for (ImageModel im : (List<ImageModel>)mtm.getChildren()) {
                MessagePhoto mp = getMessagePhotoDao().read(im.getId());
                MessagePhotoAssociation mpa = new MessagePhotoAssociation();
                mpa.setMessagePhoto(mp);
                mpa.setMessageTemplate(mt);
                getMessagePhotoAssociationDao().create(mpa);
                photos.add(mp);
            }
            if (photos.size() > 0) {
                mt.setPhotos(photos);
            }
            getMessageTemplateDao().update(mt);
        } catch (Throwable e) {
            e.printStackTrace();
            getLog().error(e);
            throw new Exception(e);
        } finally {
            closeSession();
        }
    }
    
}
