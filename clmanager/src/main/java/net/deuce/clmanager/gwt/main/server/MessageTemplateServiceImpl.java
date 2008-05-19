package net.deuce.clmanager.gwt.main.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.deuce.clmanager.domain.Category;
import net.deuce.clmanager.domain.MessagePhoto;
import net.deuce.clmanager.domain.MessageTemplate;
import net.deuce.clmanager.gwt.main.client.MessageTemplateService;
import net.deuce.clmanager.gwt.main.client.model.MessageTemplateModel;
import net.deuce.clmanager.gwt.main.client.model.PhotoWrapper;

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
                for (MessagePhoto mp : (Set<MessagePhoto>)mt.getPhotos()) {
                    photos.add(new PhotoWrapper(mp.getId(), mp.getService(), mp.getExternalId(), mp.getUrl()));
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
            for (PhotoWrapper pw : (List<PhotoWrapper>)mtm.getChildren()) {
                MessagePhoto mp = new MessagePhoto();
                mp.setService(pw.getService());
                mp.setExternalId(pw.getExternalId());
                mp.setUrl(pw.getUrl());
                mp.setMessageTemplate(mt);
                getMessagePhotoDao().create(mp);
                photos.add(mp);
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
            for (MessagePhoto mp : (Set<MessagePhoto>)mt.getPhotos()) {
                mp.getId();
                getMessagePhotoDao().delete(mp);
            }
            for (PhotoWrapper pw : (List<PhotoWrapper>)mtm.getChildren()) {
                MessagePhoto mp = new MessagePhoto();
                mp.setService(pw.getService());
                mp.setExternalId(pw.getExternalId());
                mp.setUrl(pw.getUrl());
                mp.setMessageTemplate(mt);
                getMessagePhotoDao().create(mp);
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