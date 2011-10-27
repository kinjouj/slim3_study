package sample.controller.uploads;

import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.Image;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import sample.service.UploadService;

public class ThumbController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() throws Exception {
        Key key = asKey("key");
        Image image = service.thumbnail(key);

        if(image != null) {
            response.setContentType("image/png");
            show(asString("key"),image.getImageData());

            return null;
        }

        throw new IllegalStateException(KeyFactory.keyToString(key));
    }

    @Override
    protected Navigation handleError(Throwable t) throws Throwable {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print("ERROR: " + t.getMessage());

        return null;
    }
}
