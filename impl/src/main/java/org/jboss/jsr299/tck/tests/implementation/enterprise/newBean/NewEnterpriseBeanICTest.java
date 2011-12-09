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
package org.jboss.jsr299.tck.tests.implementation.enterprise.newBean;

import static org.jboss.jsr299.tck.TestGroups.DISPOSAL;
import static org.jboss.jsr299.tck.TestGroups.INTEGRATION;
import static org.jboss.jsr299.tck.TestGroups.NEW;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.jsr299.tck.AbstractJSR299Test;
import org.jboss.jsr299.tck.literals.NewLiteral;
import org.jboss.jsr299.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class NewEnterpriseBeanICTest extends AbstractJSR299Test {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NewEnterpriseBeanICTest.class)
                .withExcludedClass(NewEnterpriseBeanTest.class).build();
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "l")
    public void testNewBeanHasSameConstructor() {
        ExplicitConstructor newBean = getInstanceByType(ExplicitConstructor.class, new NewLiteral(
                ExplicitConstructorSessionBean.class));
        assert newBean.getConstructorCalls() == 1;
        assert newBean.getInjectedSimpleBean() != null;
    }

    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "m")
    public void testNewBeanHasSameInitializers() {
        InitializerSimpleBeanLocal bean = getInstanceByType(InitializerSimpleBeanLocal.class);
        InitializerSimpleBeanLocal newBean = getInstanceByType(InitializerSimpleBeanLocal.class, new NewLiteral(
                InitializerSimpleBean.class));
        assert bean != newBean;
        assert bean.getInitializerCalls() == 2;
    }

    /**
     * Sets up both the bean and the @New bean with different configurations so that the correct producer method used can be
     * determined.
     * 
     * @throws Exception
     */
    @Test(groups = { INTEGRATION, NEW })
    @SpecAssertion(section = "3.14", id = "v")
    public void testNewBeanHasNoProducerMethods() throws Exception {
        FoxLocal fox = getInstanceByType(FoxLocal.class);
        FoxLocal newFox = getInstanceByType(FoxLocal.class, new NewLiteral(Fox.class));
        fox.setNextLitterSize(3);
        newFox.setNextLitterSize(5);
        Litter theOnlyLitter = getInstanceByType(Litter.class, new AnnotationLiteral<Tame>() {
        });
        assert theOnlyLitter.getQuantity() == fox.getNextLitterSize();
    }

    @Test(groups = { INTEGRATION, NEW, DISPOSAL })
    @SpecAssertion(section = "3.14", id = "x")
    public void testNewBeanHasNoDisposalMethods() throws Exception {
        FoxLocal fox = getInstanceByType(FoxLocal.class);
        FoxLocal newFox = getInstanceByType(FoxLocal.class, new NewLiteral(Fox.class));
        Set<Bean<Litter>> beans = getBeans(Litter.class, new AnnotationLiteral<Tame>() {
        });
        assert beans.size() == 1;
        Bean<Litter> litterBean = beans.iterator().next();
        CreationalContext<Litter> creationalContext = getCurrentManager().createCreationalContext(litterBean);
        Litter litter = litterBean.create(creationalContext);
        litterBean.destroy(litter, creationalContext);
        assert fox.isLitterDisposed();
        assert !newFox.isLitterDisposed();
    }

}
