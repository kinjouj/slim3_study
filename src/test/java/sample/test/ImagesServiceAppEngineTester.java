package sample.test;

import org.slim3.tester.AppEngineTester;
import com.google.appengine.api.images.ImagesServicePb.ImagesTransformRequest;
import com.google.appengine.api.images.ImagesServicePb.ImagesTransformResponse;
import com.google.appengine.api.images.dev.LocalImagesService;
import com.google.appengine.tools.development.LocalRpcService.Status;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Environment;

public class ImagesServiceAppEngineTester extends AppEngineTester {
    @Override
    public byte[] makeSyncCall(Environment env,String service,String method,byte[] requestBuffer) throws ApiProxyException {
        if("images".equals(service) && "Transform".equals(method)) {
            ImagesTransformRequest.Builder builder = ImagesTransformRequest.newBuilder();

            try {
                builder.mergeFrom(requestBuffer);
            } catch(Exception e) {
                e.printStackTrace();
            }

            LocalImagesService imagesService = new LocalImagesService();
            ImagesTransformResponse res = imagesService.transform(new Status(),builder.build());

            return res.toByteArray();
        }

        return super.makeSyncCall(env,service,method,requestBuffer);
    }
}