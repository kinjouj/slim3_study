package sample.controller.uploads;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import sample.service.UploadService;

public class DeleteController extends Controller {

    private UploadService service = new UploadService();

    @Override
    protected Navigation run() throws Exception {
        Key key = asKey("key");

        if(key != null) {
            service.deleteUpload(key);
        }

        return redirect("/uploads");
    }

    @Override
    public Navigation handleError(Throwable t) throws Throwable {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print("ERROR: " + t.getMessage());

        return null;
    }
}