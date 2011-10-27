package sample.controller.uploads;

import java.util.List;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;
import org.junit.Test;

import sample.model.Upload;
import sample.model.UploadData;
import sample.test.UploadTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.SC_OK;

public class IndexControllerTest extends ControllerTestCase {
    @Test
    public void test1() throws Exception {
        tester.start("/uploads");
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.getDestinationPath(),is("/WEB-INF/jsp/uploads.jsp"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Upload> uploads = tester.requestScope("uploads");
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));
    }

    @Test
    public void test2() throws Exception {
        Key key = UploadTestCaseUtil.createTestImage();
        assertThat(key,is(notNullValue()));

        tester.start("/uploads");
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.getDestinationPath(),is("/WEB-INF/jsp/uploads.jsp"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Upload> uploads = tester.requestScope("uploads");
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(1));

        Upload upload = uploads.get(0);
        assertThat(upload,is(notNullValue()));
        assertThat(upload.getKey(),is(key));
        assertThat(upload.getFileName(),is("test.jpg"));
        assertThat(upload.getSize(),is(UploadTestCaseUtil.TEST_IMAGE_SIZE));

        UploadData uploadData = upload.getUploadDataRef().getModel();
        assertThat(uploadData,is(notNullValue()));
        assertThat(uploadData.getKey().getParent(),is(upload.getKey()));

        Transaction tx = null;

        try {
            tx = Datastore.beginTransaction();

            Datastore.deleteAll(upload.getKey());

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
        }

        tester.start("/uploads");
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.getDestinationPath(),is("/WEB-INF/jsp/uploads.jsp"));
        assertThat(tester.getController(),is(notNullValue()));

        uploads = tester.requestScope("uploads");
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));
    }
}