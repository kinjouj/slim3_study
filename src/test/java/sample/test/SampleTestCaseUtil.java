package sample.test;

import com.google.appengine.api.datastore.Key;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.GlobalTransaction;

import sample.model.Item;
import sample.model.Profile;
import sample.model.Sample;

public class SampleTestCaseUtil {
    public static Key createTestSample() {
        Key key = Datastore.allocateId(Sample.class);

        Sample sample = new Sample();
        sample.setKey(key);
        sample.setName("test");

        Profile profile = new Profile();
        profile.setKey(Datastore.allocateId(key,Profile.class));
        profile.setScreenName("test");

        sample.getProfileRef().setModel(profile);

        Item item = new Item();
        item.setKey(Datastore.allocateId(key,Item.class));
        item.setItemName("test");
        item.getSampleRef().setModel(sample);

        GlobalTransaction tx = null;

        try {
            tx = Datastore.beginGlobalTransaction();

            Datastore.put(item,profile,sample);

            tx.commit();

            return key;
        } catch(Exception e) {
            if(tx != null) {
                tx.rollback();
            }
        }

        return null;
    }
}
