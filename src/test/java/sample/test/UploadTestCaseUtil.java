package sample.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import sample.model.Upload;
import sample.model.UploadData;

import com.google.appengine.api.datastore.Key;

public class UploadTestCaseUtil {

    public static int TEST_IMAGE_SIZE = 360674;

    public static Key createTestImage() throws Exception {
        Key key = Datastore.allocateId(Upload.class);
        byte[] data = getData();

        Upload upload = new Upload();
        upload.setKey(key);
        upload.setFileName("test.jpg");
        upload.setSize(data.length);

        UploadData uploadData = new UploadData();
        uploadData.setKey(Datastore.allocateId(key,UploadData.class));
        uploadData.setData(data);

        upload.getUploadDataRef().setModel(uploadData);

        GlobalTransaction tx = null;

        try {
            tx = Datastore.beginGlobalTransaction();

            Datastore.put(uploadData,upload);

            tx.commit();

            return key;
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
        }

        return null;
    }

    public static byte[] getData() throws Exception {
        byte[] b = null;
        FileInputStream is = null;
        ByteArrayOutputStream baos = null;

        try {
            is = new FileInputStream(new File("war/test.jpg"));
            baos = new ByteArrayOutputStream();

            int c;

            while((c = is.read()) != -1) {
                baos.write(c);
            }

            b = baos.toByteArray();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(baos != null) {
                try {
                    baos.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
            if(is != null) {
                try {
                    is.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return b;
    }
}
