package sample.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Key;
import org.slim3.datastore.Model;
import org.slim3.datastore.Attribute;
import org.slim3.datastore.ModelRef;

@Model
public class Upload implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    private String fileName;
    private int size;
    private Date createdAt;

    private ModelRef<UploadData> uploadDataRef = new ModelRef<UploadData>(UploadData.class);

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public ModelRef<UploadData> getUploadDataRef() {
        return uploadDataRef;
    }
}
