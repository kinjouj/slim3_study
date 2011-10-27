package sample.controller;

import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;

import sample.service.SampleService;

public class IndexController extends Controller {

    private SampleService service = new SampleService();

    @Override
    public Navigation run() throws Exception {
        requestScope("samples",service.findAll());
        return forward("/WEB-INF/jsp/index.jsp");
    }
}
