package org.redflag.service;

public abstract class AbstractService<Request, Response> {
    protected void validateRequest(Request request) {

    }

    protected void validateState(Request request) {

    }

    protected abstract Response logic(Request request);

    public Response service(Request request) {
        validateRequest(request);
        validateState(request);
        return logic(request);
    }
}
