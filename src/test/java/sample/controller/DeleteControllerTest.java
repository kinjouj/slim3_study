package sample.controller;

import java.util.List;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import org.slim3.tester.ControllerTestCase;
import org.slim3.datastore.Datastore;
import org.junit.Test;

import sample.meta.SampleMeta;
import sample.model.Item;
import sample.model.Profile;
import sample.model.Sample;
import sample.test.SampleTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.*;

public class DeleteControllerTest extends ControllerTestCase {

    private SampleMeta meta = SampleMeta.get();

    @Test
    public void test1() throws Exception {
        tester.start("/delete/a");
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void test2() throws Exception {
        Key key = SampleTestCaseUtil.createTestSample();

        Transaction tx = null;

        try {
            tx = Datastore.beginTransaction();

            Datastore.delete(key);

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
        }

        tester.start("/delete/" + KeyFactory.keyToString(key));
        assertThat(tester.response.getStatus(),is(SC_INTERNAL_SERVER_ERROR));
    }

    @Test
    public void test3() throws Exception {
        Key key = SampleTestCaseUtil.createTestSample();

        List<Sample> samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(1));

        Sample sample2 = samples.get(0);
        assertThat(sample2,is(notNullValue()));
        assertThat(sample2.getName(),is("test"));

        Profile profile2 = sample2.getProfileRef().getModel();
        assertThat(profile2,is(notNullValue()));
        assertThat(profile2.getScreenName(),is("test"));
        assertThat(profile2.getKey().getParent(),is(key));

        List<Item> items = sample2.getItemRef().getModelList();
        assertThat(items,is(notNullValue()));
        assertThat(items.size(),is(1));

        Item item = items.get(0);
        assertThat(item,is(notNullValue()));
        assertThat(item.getItemName(),is("test"));
        assertThat(item.getKey().getParent(),is(key));

        String stringKey = KeyFactory.keyToString(key);
        assertThat(stringKey,is(notNullValue()));

        tester.start("/delete/" + stringKey);
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/"));
        assertThat(tester.getController(),is(notNullValue()));

        samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(0));
    }
}