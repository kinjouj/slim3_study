package sample.service;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.OutputSettings;
import com.google.appengine.api.images.Transform;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import org.slim3.util.StringUtil;
import org.slim3.controller.upload.FileItem;

import sample.model.Upload;
import sample.model.UploadData;
import sample.meta.UploadMeta;

public class UploadService {

    private UploadMeta uploadMeta = UploadMeta.get();
    private ImagesService imageService = ImagesServiceFactory.getImagesService();

    public List<Upload> findAll() {
        return Datastore.query(uploadMeta).sortInMemory(uploadMeta.createdAt.desc).asList();
    }

    public Upload findByKey(Key key) throws EntityNotFoundException {
        if(key == null) {
            return null;
        }

        Upload upload = Datastore.get(Upload.class,key);

        return upload;
    }

    public void upload(FileItem file) throws Exception {
        if(file == null) {
            return;
        }

        if(StringUtil.isEmpty(file.getShortFileName()) || StringUtil.isEmpty(file.getContentType()) || file.getData().length <= 0) {
            return;
        }

        byte[] data = file.getData();
        Key key = Datastore.allocateId(Upload.class);

        Upload upload = new Upload();
        upload.setKey(key);
        upload.setFileName(file.getShortFileName());
        upload.setSize(data.length);
        upload.setCreatedAt(new Date());

        UploadData uploadData = new UploadData();
        uploadData.setKey(Datastore.allocateId(key,UploadData.class));
        uploadData.setData(data);

        upload.getUploadDataRef().setModel(uploadData);

        GlobalTransaction tx = null;

        try {
            tx = Datastore.beginGlobalTransaction();

            /* 
            if(!Datastore.putUniqueValue("uniqueFileName",upload.getFileName())) {
                throw new Exception("unique");
            }
            */

            Datastore.put(uploadData,upload);

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }

            throw e;
        }
    }

    public Image thumbnail(Key key) throws Exception {
        if(key == null) {
            throw new NullPointerException("key is null");
        }

        Upload upload = findByKey(key);

        if(upload == null) {
            return null;
        }

        UploadData uploadData = upload.getUploadDataRef().getModel();

        if(uploadData == null) {
            return null;
        }

        OutputSettings settings = new OutputSettings(ImagesService.OutputEncoding.PNG);
        settings.setQuality(100);

        Transform trans = ImagesServiceFactory.makeResize(250,250);

        if(trans == null) {
            return null;
        }

        Image image = ImagesServiceFactory.makeImage(uploadData.getData());

        if(image == null) {
            return null;
        }

        return imageService.applyTransform(trans,image,settings);
    }

    public void deleteUpload(Key key) throws Exception {
        if(key == null) {
            throw new IllegalArgumentException("key is null");
        }

        Upload upload = Datastore.get(Upload.class,key);

        Transaction tx = null;

        try {
            tx = Datastore.beginTransaction();

            Datastore.deleteAll(upload.getKey());

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }

            throw e;
        }
    }
}
