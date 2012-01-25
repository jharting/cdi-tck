/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
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
package org.jboss.cdi.tck.tests.interceptors.invocation;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.beans10.BeansDescriptor;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * 
 * @author Martin Kouba
 */
@SpecVersion(spec = "cdi", version = "20091101")
public class InterceptorInvocationTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClassPackage(InterceptorInvocationTest.class)
                .withBeansXml(
                        Descriptors.create(BeansDescriptor.class).createInterceptors()
                                .clazz(MissileInterceptor.class.getName()).up()).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "7.2", id = "a"), @SpecAssertion(section = "9.2", id = "a"),
            @SpecAssertion(section = "12.4", id = "kb") })
    public void testManagedBeanIsIntercepted() {

        MissileInterceptor.reset();
        Missile missile = getInstanceByType(Missile.class);
        missile.fire();

        assertTrue(MissileInterceptor.methodIntercepted);
        assertNotNull(missile.getWarhead()); // test that injection works
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "7.2", id = "a1"), @SpecAssertion(section = "3.10", id = "f") })
    public void testInitializerMethodsNotIntercepted() {

        MissileInterceptor.reset();
        Missile missile = getInstanceByType(Missile.class);

        assertFalse(MissileInterceptor.methodIntercepted);
        assertTrue(missile.initCalled()); // this call is intercepted
        assertTrue(MissileInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = "7.2", id = "ia")
    public void testProducerMethodsAreIntercepted() {

        MissileInterceptor.reset();
        getInstanceByType(Wheat.class);

        assertTrue(MissileInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = "7.2", id = "ic")
    public void testDisposerMethodsAreIntercepted() {

        MissileInterceptor.reset();

        Bean<Wheat> bean = getBeans(Wheat.class).iterator().next();
        CreationalContext<Wheat> creationalContext = getCurrentManager().createCreationalContext(bean);
        Wheat instance = getInstanceByType(Wheat.class);

        MissileInterceptor.methodIntercepted = false;
        bean.destroy(instance, creationalContext);

        assertTrue(WheatProducer.destroyed);
        assertTrue(MissileInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = "7.2", id = "ie")
    public void testObserverMethodsAreIntercepted() {

        MissileInterceptor.reset();
        getCurrentManager().fireEvent(new Missile());

        assertTrue(MissileObserver.observed);
        assertTrue(MissileInterceptor.methodIntercepted);
    }

    @Test
    @SpecAssertion(section = "7.2", id = "j")
    public void testLifecycleCallbacksAreIntercepted() {

        MissileInterceptor.reset();
        getInstanceByType(Rye.class);

        assertFalse(MissileInterceptor.methodIntercepted);
        assertTrue(MissileInterceptor.lifecycleCallbackIntercepted);
    }

    @Test
    @SpecAssertion(section = "7.2", id = "m")
    public void testObjectMethodsAreNotIntercepted() {

        MissileInterceptor.reset();
        getInstanceByType(Missile.class).toString();

        assertFalse(MissileInterceptor.methodIntercepted);
        assertTrue(MissileInterceptor.lifecycleCallbackIntercepted);
    }

}