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
package org.jboss.cdi.tck.interceptors.tests.method;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "int", version = "3.1.PFD")
public class MethodLevelInterceptorTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(MethodLevelInterceptorTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3", id = "ca"), @SpecAssertion(section = "8", id = "a"),
            @SpecAssertion(section = "8", id = "d"), @SpecAssertion(section = "8", id = "g"),
            @SpecAssertion(section = "8", id = "h") })
    public void testInterceptorCanBeAppliedToMoreThanOneMethod() {
        Fish fish = getContextualReference(Fish.class);
        assert fish.foo().equals("Intercepted bar");
        assert fish.ping().equals("Intercepted pong");
        assert fish.getName().equals("Salmon");
        assert FishInterceptor.getInstanceCount() == 1;
    }

    @Test
    @SpecAssertion(section = "8", id = "j")
    public void testExcludeClassInterceptors() {
        assert getContextualReference(Dog.class).foo().equals("Intercepted bar");
        assert getContextualReference(Dog.class).ping().equals("pong");
    }
}
