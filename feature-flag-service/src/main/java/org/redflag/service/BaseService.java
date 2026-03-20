package org.redflag.service;


public abstract class BaseService<Request, Response> {
    protected void validateRequest(Request request) {

    }

    protected void validateState(Request request) {

    }

    protected abstract Response execute(Request request);

    public Response service(Request request) {
        validateRequest(request);
        validateState(request);
        return execute(request);
    }
}
