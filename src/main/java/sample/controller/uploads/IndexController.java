package sample.controller.uploads;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import sample.service.UploadService;

public class IndexController extends Controller {

    private UploadService service = new UploadService();

    @Override
    public Navigation run() {
        requestScope("uploads",service.findAll());
        return forward("/WEB-INF/jsp/uploads.jsp");
    }
}
