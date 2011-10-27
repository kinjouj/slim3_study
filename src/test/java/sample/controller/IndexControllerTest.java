package sample.controller;

import java.util.List;
import org.slim3.tester.ControllerTestCase;
import org.junit.Test;

import sample.model.Sample;
import sample.model.Profile;
import sample.model.Item;
import sample.test.SampleTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static javax.servlet.http.HttpServletResponse.*;

public class IndexControllerTest extends ControllerTestCase {

    @Test
    public void test1() throws Exception {
        tester.start("/");
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.getDestinationPath(),is("/WEB-INF/jsp/index.jsp"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Sample> samples = tester.requestScope("samples");
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(0));
    }

    @Test
    public void test2() throws Exception {
        SampleTestCaseUtil.createTestSample();

        tester.start("/");
        assertThat(tester.response.getStatus(),is(SC_OK));
        assertThat(tester.getDestinationPath(),is("/WEB-INF/jsp/index.jsp"));
        assertThat(tester.getController(),is(notNullValue()));

        List<Sample> samples = tester.requestScope("samples");
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(1));

        Sample sample = samples.get(0);
        assertThat(sample,is(notNullValue()));
        assertThat(sample.getName(),is("test"));

        Profile profile2 = sample.getProfileRef().getModel();
        assertThat(profile2,is(notNullValue()));
        assertThat(profile2.getScreenName(),is("test"));
        assertThat(profile2.getKey().getParent(),is(sample.getKey()));

        List<Item> items = sample.getItemRef().getModelList();
        assertThat(items,is(notNullValue()));
        assertThat(items.size(),is(1));

        Item item2 = items.get(0);
        assertThat(item2,is(notNullValue()));
        assertThat(item2.getItemName(),is("test"));
        assertThat(item2.getKey().getParent(),is(sample.getKey()));
    }
}