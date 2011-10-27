package sample.test;

import org.slim3.tester.ControllerTester;
import org.slim3.tester.ControllerTestCase;
import com.google.appengine.api.images.ImagesServicePb.ImagesTransformRequest;
import com.google.appengine.api.images.ImagesServicePb.ImagesTransformResponse;
import com.google.appengine.api.images.dev.LocalImagesService;
import com.google.appengine.tools.development.LocalRpcService.Status;
import com.google.apphosting.api.ApiProxy.Environment;

public class ImagesServiceControllerTester extends ControllerTester {

    public ImagesServiceControllerTester(Class<? extends ControllerTestCase> testCaseClass) {
        super(testCaseClass);
    }

    @Override
    public byte[] makeSyncCall(Environment env,String service,String method,byte[] requestBuffer) {
        if("images".equals(service) && "Transform".equals(method)) {
            ImagesTransformRequest.Builder builder = ImagesTransformRequest.newBuilder();

            try {
                builder.mergeFrom(requestBuffer);
            } catch(Exception e) {
                e.printStackTrace();
            }

            LocalImagesService imageService = new LocalImagesService();
            ImagesTransformResponse res = imageService.transform(new Status(),builder.build());

            return res.toByteArray();
        }

        return super.makeSyncCall(env,service,method,requestBuffer);
    }
}
