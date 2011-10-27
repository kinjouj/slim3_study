package sample.controller.uploads;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import sample.test.ImagesServiceControllerTester;
import sample.test.UploadTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.*;

public class ThumbControllerTest extends ControllerTestCase {

    @Before
    public void setUp() throws Exception {
        tester = new ImagesServiceControllerTester(this.getClass());
        tester.setUp();
    }

    @After
    public void tearDown() throws Exception {
        tester.tearDown();
    }

    @Test
    public void test1() throws Exception {
        tester.start("/uploads/thumb/dummy");
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void test2() throws Exception {
        Key key = UploadTestCaseUtil.createTestImage();
        assertThat(key,is(notNullValue()));

        tester.start("/uploads/thumb/" + KeyFactory.keyToString(key));
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.response.getContentType(),is("image/png"));
        assertThat(tester.getController(),is(notNullValue()));
    }

    @Test
    public void test3() throws Exception {
        Key key = UploadTestCaseUtil.createTestImage();
        assertThat(key,is(notNullValue()));

        Transaction tx = null;

        try {
            tx = Datastore.beginTransaction();

            Datastore.deleteAll(key);

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }

            throw e;
        }

        tester.start("/uploads/thumb/" + KeyFactory.keyToString(key));
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }
}