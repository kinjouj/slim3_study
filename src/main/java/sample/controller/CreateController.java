package sample.controller;

import javax.servlet.http.HttpServletResponse;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.RequestMap;

import sample.service.SampleService;
import sample.meta.SampleMeta;

public class CreateController extends Controller {

    private SampleService service = new SampleService();

    @Override
    public Navigation run() throws Exception {
        if(!isPost()) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().print("HTTP Method Not Allowed");

            return null;
        }

        Validators validator = new Validators(request);
        validator.add(SampleMeta.get().name,validator.required());

        if(validator.validate()) {
            service.createSample(new RequestMap(request));
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
