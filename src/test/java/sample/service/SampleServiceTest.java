package sample.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.google.appengine.api.datastore.Key;
import org.slim3.tester.AppEngineTestCase;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;
import org.junit.Test;

import sample.model.Sample;
import sample.meta.SampleMeta;
import sample.test.SampleTestCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SampleServiceTest extends AppEngineTestCase {

    private SampleService service = new SampleService();
    private SampleMeta meta = SampleMeta.get();

    @Test
    public void createSampleTest1() throws Exception {
        Map<String,Object> input = new HashMap<String,Object>();
        input.put("name","hoge fuga foobar");

        service.createSample(input);

        List<Sample> samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(1));

        Sample sample = samples.get(0);
        assertThat(sample,is(notNullValue()));
    }

    @Test(expected=NullPointerException.class)
    public void createSampleTest2() throws Exception {
        service.createSample(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void createSampleTest3() throws Exception {
        Map<String,Object> input = new HashMap<String,Object>();
        service.createSample(input);
    }

    @Test(expected=IllegalArgumentException.class)
    public void createSampleTest4() throws Exception {
        Map<String,Object> input = new HashMap<String,Object>();
        input.put("name",null);

        service.createSample(input);
    }

    @Test(expected=IllegalArgumentException.class)
    public void createSampleTest5() throws Exception {
        Map<String,Object> input = new HashMap<String,Object>();
        input.put("name","");

        service.createSample(input);
    }

    @Test(expected=IllegalArgumentException.class)
    public void createSampleTest6() throws Exception {
        Map<String,Object> input = new HashMap<String,Object>();
        input.put("name"," ");

        service.createSample(input);
    }

    @Test
    public void deleteSampleTest1() throws Exception {
        SampleTestCaseUtil.createTestSample();

        List<Sample> samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(1));

        Sample sample2 = samples.get(0);
        assertThat(sample2,is(notNullValue()));
        assertThat(sample2.getName(),is("test"));

        Key key = sample2.getKey();
        assertThat(key,is(notNullValue()));

        service.deleteSample(key);

        samples = Datastore.query(meta).asList();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(0));
    }

    @Test(expected=NullPointerException.class)
    public void deleteSampleTest2() throws Exception {
        service.deleteSample(null);
    }

    @Test(expected=EntityNotFoundRuntimeException.class)
    public void deletSampleTest3() throws Exception {
        service.deleteSample(Datastore.allocateId(Sample.class));
    }

    @Test
    public void findAllTest() throws Exception {
        List<Sample> samples = service.findAll();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(0));

        SampleTestCaseUtil.createTestSample();

        samples = service.findAll();
        assertThat(samples,is(notNullValue()));
        assertThat(samples.size(),is(1));

        Sample sample = samples.get(0);
        assertThat(sample,is(notNullValue()));
        assertThat(sample.getName(),is("test"));
    }
}
