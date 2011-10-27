package sample.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import org.slim3.datastore.Model;
import org.slim3.datastore.Attribute;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.InverseModelListRef;

import sample.meta.ItemMeta;

@Model
public class Sample implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    private String name;
    private Date createdAt;

    private ModelRef<Profile> profileRef = new ModelRef<Profile>(Profile.class);

    @Attribute(persistent = false)
    private InverseModelListRef<Item,Sample> itemRef = new InverseModelListRef<Item,Sample>(Item.class,ItemMeta.get().sampleRef.getName(),this);

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ModelRef<Profile> getProfileRef() {
        return profileRef;
    }

    public InverseModelListRef<Item,Sample> getItemRef() {
        return itemRef;
    }
}
