package sample.controller;

import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Key;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import sample.service.SampleService;

public class DeleteController extends Controller {

    private SampleService service = new SampleService();

    @Override
    public Navigation run() throws Exception {
        Key key = asKey("key");

        if(key != null) {
            service.deleteSample(key);
        }

        return redirect("/");
    }

    @Override
    protected Navigation handleError(Throwable t) throws Throwable {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print("ERROR: " + t.getMessage());

        return null;
    }
}