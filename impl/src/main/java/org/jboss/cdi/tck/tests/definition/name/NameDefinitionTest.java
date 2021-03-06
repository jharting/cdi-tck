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
package org.jboss.cdi.tck.tests.definition.name;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import javax.enterprise.inject.spi.Bean;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.cdi.tck.AbstractTest;
import org.jboss.cdi.tck.literals.AnyLiteral;
import org.jboss.cdi.tck.literals.DefaultLiteral;
import org.jboss.cdi.tck.literals.NamedLiteral;
import org.jboss.cdi.tck.shrinkwrap.WebArchiveBuilder;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.test.audit.annotations.SpecAssertion;
import org.jboss.test.audit.annotations.SpecAssertions;
import org.jboss.test.audit.annotations.SpecVersion;
import org.testng.annotations.Test;

@SpecVersion(spec = "cdi", version = "20091101")
public class NameDefinitionTest extends AbstractTest {

    @Deployment
    public static WebArchive createTestArchive() {
        return new WebArchiveBuilder().withTestClassPackage(NameDefinitionTest.class).build();
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.5.1", id = "a"), @SpecAssertion(section = "2", id = "e"),
            @SpecAssertion(section = "3.1.3", id = "bb") })
    public void testNonDefaultNamed() {
        Bean<Moose> moose = getUniqueBean(Moose.class);
        assertEquals(moose.getName(), "aMoose");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.5.2", id = "a"), @SpecAssertion(section = "3.1.5", id = "a"),
            @SpecAssertion(section = "2.5.1", id = "d"), @SpecAssertion(section = "2.5.2", id = "fa") })
    public void testDefaultNamed() {
        String name = "haddock";
        Bean<Haddock> haddock = getUniqueBean(Haddock.class);
        assertEquals(haddock.getName(), name);
        assertTrue(annotationSetMatches(haddock.getQualifiers(), AnyLiteral.INSTANCE, DefaultLiteral.INSTANCE,
                new NamedLiteral(name)));
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.7", id = "a"), @SpecAssertion(section = "2.7.1.3", id = "aa") })
    public void testStereotypeDefaultsName() {
        Bean<RedSnapper> bean = getUniqueBean(RedSnapper.class);
        assertEquals(bean.getName(), "redSnapper");
    }

    @Test
    @SpecAssertions({ @SpecAssertion(section = "2.5.3", id = "a"), @SpecAssertion(section = "2", id = "e") })
    public void testNamedNotDeclaredByBean() {
        Bean<SeaBass> bean = getUniqueBean(SeaBass.class);
        assertNull(bean.getName());
    }

    @Test
    @SpecAssertion(section = "2.5.3", id = "a")
    public void testNamedNotDeclaredByStereotype() {
        Bean<Minnow> bean = getUniqueBean(Minnow.class);
        assertNull(bean.getName());
    }

}
