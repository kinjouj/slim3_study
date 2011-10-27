package sample.controller.uploads;

import java.util.List;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import sample.meta.UploadMeta;
import sample.model.Upload;
import sample.test.UploadTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;;

public class DeleteControllerTest extends ControllerTestCase {

    @Test
    public void test1() throws Exception {
        tester.start("/uploads/delete/abc");
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void test2() throws Exception {
        Key key = UploadTestCaseUtil.createTestImage();
        assertThat(key,is(notNullValue()));

        UploadMeta meta = UploadMeta.get();

        List<Upload> uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(1));

        tester.start("/uploads/delete/" + KeyFactory.keyToString(key));
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/uploads"));
        assertThat(tester.getController(),is(notNullValue()));

        uploads = Datastore.query(meta).asList();
        assertThat(uploads,is(notNullValue()));
        assertThat(uploads.size(),is(0));

        tester.start("/uploads/delete/" + KeyFactory.keyToString(key));
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }
}