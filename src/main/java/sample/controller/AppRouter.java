package sample.controller;

import org.slim3.controller.router.RouterImpl;

public class AppRouter extends RouterImpl {
    public AppRouter() {
        addRouting("/delete/{key}","/delete?key={key}");
        addRouting("/uploads","/uploads/index");
        addRouting("/uploads/thumb/{key}","/uploads/thumb?key={key}");
        addRouting("/uploads/show/{key}","/uploads/show?key={key}");
        addRouting("/uploads/delete/{key}","/uploads/delete?key={key}");
    }
}
