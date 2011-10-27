package sample.controller;

import java.util.List;
import org.slim3.tester.ControllerTestCase;
import org.slim3.datastore.Datastore;
import org.junit.Test;

import sample.model.Sample;
import sample.model.Profile;
import sample.model.Item;
import sample.meta.SampleMeta;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.*;

public class CreateControllerTest extends ControllerTestCase {

    private SampleMeta meta = SampleMeta.get();

    @Test
    public void test1() throws Exception {
        tester.start("/create");
        assertThat(tester.response.getStatus(),is(SC_METHOD_NOT_ALLOWED));
        assertThat(tester.getController(),is(notNullValue()));
    }

    @Test
    public void test2() throws Exception {
        tester.request.setMethod("POST");
        tester.start("/create");
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Sample> samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(0));
    }

    @Test
    public void test3() throws Exception {
        tester.request.setMethod("POST");
        tester.param("name","");
        tester.start("/create");
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Sample> samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(0));
    }

    @Test
    public void test4() throws Exception {
        tester.request.setMethod("POST");
        tester.requestScope("name","hoge fuga foobar");
        tester.start("/create");
        assertThat(tester.isRedirect(),is(true));
        assertThat(tester.getDestinationPath(),is("/"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Sample> samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(1));

        Sample sample1 = samples.get(0);
        assertThat(sample1,is(notNullValue()));
        assertThat(sample1.getName(),is("hoge fuga foobar"));

        Profile profile = sample1.getProfileRef().getModel();
        assertThat(profile,is(notNullValue()));
        assertThat(profile.getKey().getParent(),is(sample1.getKey()));
        assertThat(profile.getScreenName(),is("foobar fuga hoge"));

        List<Item> items = sample1.getItemRef().getModelList();
        assertThat(items,is(notNullValue()));
        assertThat(items.size(),is(2));

        Item item1 = items.get(0);
        assertThat(item1,is(notNullValue()));
        assertThat(item1.getKey().getParent(),is(sample1.getKey()));
        assertThat(item1.getItemName(),is("Mac Book AIR"));

        Sample sample2 = item1.getSampleRef().getModel();
        assertThat(sample2,is(notNullValue()));
        assertThat(sample2.getKey(),is(sample1.getKey()));
        assertThat(sample2.getName(),is("hoge fuga foobar"));

        Item item2 = items.get(1);
        assertThat(item2,is(notNullValue()));
        assertThat(item2.getKey().getParent(),is(sample1.getKey()));
        assertThat(item2.getItemName(),is("Android"));

        Sample sample3 = item2.getSampleRef().getModel();
        assertThat(sample3,is(notNullValue()));
        assertThat(sample3.getKey(),is(sample1.getKey()));
        assertThat(sample3.getName(),is("hoge fuga foobar"));
    }
}