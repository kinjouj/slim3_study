package sample.controller.uploads;

import javax.servlet.http.HttpServletResponse;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;

import sample.service.UploadService;

public class UploadController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() throws Exception {
        if(!isPost()) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            response.getWriter().println("HTTP Method Not Allowed");

            return null;
        }

        FileItem file = requestScope("file");

        if(file != null && file.getContentType().startsWith("image")) {
            service.upload(file);
        }

        return redirect("/uploads");
    }

    @Override
    protected Navigation handleError(Throwable t) throws Throwable {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print("ERROR: " + t.getMessage());

        return null;
    }
}
