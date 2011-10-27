package sample.controller.uploads;

import java.util.List;
import org.junit.Test;
import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import sample.meta.UploadMeta;
import sample.model.Upload;
import sample.model.UploadData;
import sample.test.UploadTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.*;

public class UploadControllerTest extends ControllerTestCase {

    private final UploadMeta meta = UploadMeta.get();

    @Test
    public void test1() throws Exception {
        tester.start("/uploads/upload");
        assertThat(tester.response.getStatus(),is(SC_METHOD_NOT_ALLOWED));
    }

    @Test
    public void test2() throws Exception {
        tester.request.setMethod("POST");
        tester.start("/uploads/upload");
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/uploads"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Upload> uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));
    }

    @Test
    public void test3() throws Exception {
        FileItem file = new FileItem("test.jpg","text/plain",UploadTestCaseUtil.getData());

        tester.request.setMethod("POST");
        tester.requestScope("file",file);
        tester.start("/uploads/upload");
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/uploads"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Upload> uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));
    }

    @Test
    public void test4() throws Exception {
        FileItem file = new FileItem("test.jpg","image/jpeg",UploadTestCaseUtil.getData());

        tester.request.setMethod("POST");
        tester.requestScope("file",file);
        tester.start("/uploads/upload");
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/uploads"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Upload> uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(1));

        Upload upload = uploads.get(0);
        assertThat(upload,is(notNullValue()));
        assertThat(upload.getFileName(),is("test.jpg"));
        assertThat(upload.getSize(),is(UploadTestCaseUtil.TEST_IMAGE_SIZE));

        UploadData uploadData = upload.getUploadDataRef().getModel();
        assertThat(uploadData,is(notNullValue()));
        assertThat(uploadData.getKey().getParent(),is(upload.getKey()));
    }
}