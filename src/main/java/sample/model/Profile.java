package sample.model;

import java.io.Serializable;
import com.google.appengine.api.datastore.Key;

import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.Attribute;

import sample.meta.SampleMeta;

@Model
public class Profile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    private String screenName;

    @Attribute(persistent = false)
    private InverseModelListRef<Sample,Profile> samplesRef = new InverseModelListRef<Sample,Profile>(Sample.class,SampleMeta.get().profileRef,this);

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public InverseModelListRef<Sample,Profile> getSamplesRef() {
        return samplesRef;
    }
}
