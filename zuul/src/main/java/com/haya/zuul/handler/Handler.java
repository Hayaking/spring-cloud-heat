package com.haya.zuul.handler;

import com.netflix.zuul.context.RequestContext;

/**
 * @author haya
 */
public interface Handler {

    void doIt(String name, RequestContext context);

}
