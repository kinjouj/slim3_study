package sample.test;

import org.slim3.tester.ControllerTestCase;

public class AppRouterTestCase extends ControllerTestCase {

    protected static interface Callback {
        public void test(String path,String query) throws Exception;
    }

    public void routingTest(String start,Callback callback) throws Exception {
        tester.start(start);

        if(callback != null) {
            String path = tester.request.getRequestURI();
            String query = tester.request.getQueryString();

            callback.test(path,query);
        }
    }
}