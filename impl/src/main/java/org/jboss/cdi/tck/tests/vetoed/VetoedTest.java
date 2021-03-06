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
package org.jboss.cdi.tck.tests.vetoed;

import static org.jboss.cdi.tck.TestGroups.INTEGRATION;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedTypeWrapper;
import org.jboss.cdi.tck.tests.extensions.alternative.metadata.AnnotatedWrapper;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Fish;
import org.jboss.cdi.tck.tests.vetoed.aquarium.Piranha;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

/**
 * <p>
 * This test was originally part of the Weld test suite.
 * <p>
 * 
 * Temporarily marked as integration tests - see SHRINKWRAP-369.
 * 
 * @author Jozef Hartinger
 * @author Martin Kouba
 */
@Test(groups = INTEGRATION)
@SpecVersion(spec = "cdi", version = "20091101")
public class VetoedTest extends AbstractTest {

    @SuppressWarnings("unchecked")
    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder()
                .withTestClass(VetoedTest.class)
                .withClasses(AnnotatedTypeWrapper.class, AnnotatedWrapper.class, Animal.class, Elephant.class,
                        ModifyingExtension.class, VerifyingExtension.class).withPackage(Piranha.class.getPackage())
                .withExtensions(ModifyingExtension.class, VerifyingExtension.class).withLibrary(Gecko.class, Reptile.class)
                .build();
    }

    @Inject
    VerifyingExtension verifyingExtension;

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.12", id = "a"), @SpecAssertion(section = "3.1.1", id = "h"), @SpecAssertion(section = "11.5.6", id = "ac") })
    public void testClassLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Elephant.class));
        assertEquals(getCurrentManager().getBeans(Elephant.class, AnyLiteral.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Animal.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.12", id = "a"), @SpecAssertion(section = "3.1.1", id = "h"), @SpecAssertion(section = "11.5.6", id = "ac") })
    public void testPackageLevelVeto() {
        assertFalse(verifyingExtension.getClasses().contains(Piranha.class));
        assertEquals(getCurrentManager().getBeans(Piranha.class, AnyLiteral.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Fish.class));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "3.12", id = "a"), @SpecAssertion(section = "11.5.6", id = "ad") })
    public void testAnnotatedTypeAddedByExtension() {
        assertFalse(verifyingExtension.getClasses().contains(Gecko.class));
        assertEquals(getCurrentManager().getBeans(Gecko.class, AnyLiteral.INSTANCE).size(), 0);
        assertFalse(verifyingExtension.getClasses().contains(Reptile.class));
    }

}
