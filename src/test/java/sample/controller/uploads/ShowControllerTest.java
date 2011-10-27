package sample.controller.uploads;

import org.junit.Test;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;

import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

import sample.test.UploadTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.*;

public class ShowControllerTest extends ControllerTestCase {

    @Test
    public void test1() throws Exception {
        tester.start("/uploads/show/a");
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void test2() throws Exception {
        Key key = UploadTestCaseUtil.createTestImage();
        assertThat(key,is(notNullValue()));

        tester.start("/uploads/show/" + KeyFactory.keyToString(key));
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.getController(),is(notNullValue()));
    }

    @Test
    public void test3() throws Exception {
        Key key = UploadTestCaseUtil.createTestImage();

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

        tester.start("/uploads/show/" + KeyFactory.keyToString(key));
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }
}