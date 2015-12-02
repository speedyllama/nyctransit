package com.speedyllama.mtastatus;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class MTAStatusQueryHandler implements RequestHandler<Object, Object> {

    @Override
    public Object handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

        // TODO: implement your handler
        return null;
    }

}
