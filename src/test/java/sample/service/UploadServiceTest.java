package sample.service;

import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService.OutputEncoding;
import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;
import org.slim3.tester.AppEngineTestCase;

import sample.meta.UploadMeta;
import sample.model.Upload;
import sample.model.UploadData;
import sample.test.ImagesServiceAppEngineTester;
import sample.test.UploadTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class UploadServiceTest extends AppEngineTestCase {

    private UploadMeta meta = UploadMeta.get();
    private UploadService service = new UploadService();

    @Before
    public void setUp() throws Exception {
        tester = new ImagesServiceAppEngineTester();
        tester.setUp();
    }

    @After
    public void tearDown() throws Exception {
        tester.tearDown();
    }

    @Test
    public void findAllTest() {
        List<Upload> uploads = service.findAll();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));
    }

    @Test
    public void findByKeyTest1() throws Exception {
        byte[] data = UploadTestCaseUtil.getData();
        FileItem item = getFileItem(data);

        service.upload(item);

        List<Upload> uploads = service.findAll();
        assertThat(uploads.size(),is(1));

        Upload upload = uploads.get(0);
        doUploadObjectTest(upload,data);

        upload = service.findByKey(upload.getKey());
        doUploadObjectTest(upload,data);

        upload = service.findByKey(null);
        assertThat(upload,is(nullValue()));
    }

    @Test(expected=EntityNotFoundRuntimeException.class)
    public void findByKeyTest2() throws Exception {
        service.findByKey(Datastore.allocateId(Upload.class));
    }

    @Test
    public void findAll_upload_thumbnail_Test() throws Exception {
        List<Upload> uploads = service.findAll();
        assertThat(uploads.size(),is(0));

        byte[] data = UploadTestCaseUtil.getData();
        FileItem item = getFileItem(data);

        service.upload(item);

        uploads = service.findAll();
        assertThat(uploads.size(),is(1));

        Upload upload = uploads.get(0);
        doUploadObjectTest(upload,data);

        service.upload(null);

        uploads = service.findAll();
        assertThat(uploads.size(),is(1));

        service.upload(new FileItem("","",new byte[0]));

        uploads = service.findAll();
        assertThat(uploads.size(),is(1));

        service.upload(new FileItem("test.jpg","",new byte[0]));

        uploads = service.findAll();
        assertThat(uploads.size(),is(1));

        service.upload(new FileItem("test.jpg","image/jpeg",new byte[0]));

        uploads = service.findAll();
        assertThat(uploads.size(),is(1));

        upload = uploads.get(0);
        doUploadObjectTest(upload,data);

        Image image = service.thumbnail(upload.getKey());

        assertThat(image,is(notNullValue()));
        assertThat(image.getWidth(),is(250));
        assertThat(image.getWidth(),is(250));
        assertThat(image.getFormat().name(),is(OutputEncoding.PNG.name()));
    }

    @Test
    public void deleteUploadTest1() throws Exception {
        byte[] data = UploadTestCaseUtil.getData();
        FileItem item = getFileItem(data);

        service.upload(item);

        List<Upload> uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(1));

        Upload upload = uploads.get(0);
        doUploadObjectTest(upload,data);

        Key key = upload.getKey();
        assertThat(key,is(notNullValue()));

        service.deleteUpload(key);

        uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));
    }

    @Test(expected=IllegalArgumentException.class)
    public void deletUploadTest2() throws Exception {
        service.deleteUpload(null);
    }

    @Test(expected=EntityNotFoundRuntimeException.class)
    public void deleteUploadTest3() throws Exception {
        service.deleteUpload(Datastore.allocateId(Upload.class));
    }

    private void doFileItemObjectTest(FileItem item,byte[] data) {
        assertThat(item,is(notNullValue()));
        assertThat(item.getShortFileName(),is("test.jpg"));
        assertThat(item.getContentType(),is("image/jpeg"));
        assertThat(item.getData(),is(data));
    }

    private void doUploadObjectTest(Upload upload,byte[] data) {
        assertThat(upload,is(notNullValue()));
        assertThat(upload.getFileName(),is("test.jpg"));
        assertThat(upload.getSize(),is(data.length));

        UploadData uploadData = upload.getUploadDataRef().getModel();
        assertThat(uploadData,is(notNullValue()));
        assertThat(uploadData.getKey().getParent(),is(upload.getKey()));
        assertThat(uploadData.getData(),is(data));
    }

    private FileItem getFileItem(byte[] data) throws Exception {
        FileItem item = new FileItem("test.jpg","image/jpeg",data);
        doFileItemObjectTest(item,data);

        return item;
    }
}