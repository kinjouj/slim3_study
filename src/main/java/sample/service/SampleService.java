package sample.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;
import org.slim3.util.BeanUtil;

import sample.model.Sample;
import sample.model.Profile;
import sample.model.Item;
import sample.meta.SampleMeta;
import sample.meta.ProfileMeta;

public class SampleService {

    private SampleMeta meta = SampleMeta.get();

    public List<Sample> findAll() {
        return Datastore.query(meta).sortInMemory(meta.createdAt.desc).asList();
    }

    public void createSample(Map<String,Object> input) throws Exception {
        if(input == null) {
            throw new NullPointerException("input is null");
        }

        if(!input.containsKey("name")) {
            throw new IllegalArgumentException("not found key 'name'");
        }

        String name = (String)input.get("name");

        if(name == null || name.isEmpty() || name.matches("^\\s+$")) {
            throw new IllegalArgumentException("name is empty");
        }

        List<Object> models = new ArrayList<Object>(4);
        Key key = Datastore.allocateId(Sample.class);

        Sample sample = new Sample();
        BeanUtil.copy(input,sample);
        sample.setKey(key);
        sample.setCreatedAt(new Date());

        ProfileMeta profileMeta = ProfileMeta.get();
        Profile profile = Datastore.query(profileMeta).filter(profileMeta.screenName.equal("foobar fuga hoge")).asSingle();

        if(profile == null) {
            profile = new Profile();
            profile.setKey(Datastore.allocateId(key,Profile.class));
            profile.setScreenName("foobar fuga hoge");

            models.add(profile);
        }

        sample.getProfileRef().setModel(profile);

        Item item1 = new Item();
        item1.setKey(Datastore.allocateId(key,Item.class));
        item1.setItemName("Mac Book AIR");

        Item item2 = new Item();
        item2.setKey(Datastore.allocateId(key,Item.class));
        item2.setItemName("Android");

        item1.getSampleRef().setModel(sample);
        item2.getSampleRef().setModel(sample);

        models.add(item1);
        models.add(item2);
        models.add(sample);

        GlobalTransaction tx = null;

        try {
            tx = Datastore.beginGlobalTransaction();

            Datastore.put(models);

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }

            throw e;
        }
    }

    public void deleteSample(Key key) throws Exception {
        if(key == null) {
            throw new NullPointerException("key is null");
        }

        Sample sample = Datastore.get(Sample.class,key);

        Transaction tx = null;

        try {
            tx = Datastore.beginTransaction();

            Datastore.delete(sample.getKey());

            tx.commit();
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }

            throw e;
        }
    }
}
