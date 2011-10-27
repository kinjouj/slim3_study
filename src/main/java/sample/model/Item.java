package sample.model;

import java.io.Serializable;
import com.google.appengine.api.datastore.Key;
import org.slim3.datastore.Model;
import org.slim3.datastore.Attribute;
import org.slim3.datastore.ModelRef;

@Model
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    private String itemName;

    private ModelRef<Sample> sampleRef = new ModelRef<Sample>(Sample.class);

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public ModelRef<Sample> getSampleRef() {
        return sampleRef;
    }
}
