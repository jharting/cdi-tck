/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.cdi.tck.tests.context.request.event.async;

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AsyncService {

    @Inject
    private RequestScopedObserver requestScopedObserver;

    @Inject
    private ApplicationScopedObserver observer;

    @Asynchronous
    public Future<Boolean> compute() {
        /*
         * This verifies that:
         * 1) Request context is active
         * 2) @Initialized(RequestScoped.class) was received
         * TODO: how to properly verify @Destroyed(RequestScoped)??
         */
        observer.reset();
        return new AsyncResult<Boolean>(requestScopedObserver.isInitializedObserved());
    }
}
