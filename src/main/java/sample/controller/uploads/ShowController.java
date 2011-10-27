package sample.controller.uploads;

import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Key;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import sample.service.UploadService;
import sample.model.Upload;
import sample.model.UploadData;

public class ShowController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() throws Exception {
        Key key = asKey("key");

        if(key != null) {
            Upload upload = service.findByKey(key);

            if(upload != null) {
                UploadData uploadData = upload.getUploadDataRef().getModel();

                if(uploadData != null) {
                    show(upload.getFileName(),uploadData.getData());

                    return null;
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        return null;
    }

    @Override
    protected Navigation handleError(Throwable t) throws Throwable {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print("ERROR: " + t.getMessage());

        return null;
    }
}
