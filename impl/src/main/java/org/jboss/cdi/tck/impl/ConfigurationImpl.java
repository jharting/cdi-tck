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
package org.jboss.cdi.tck.impl;

import javax.enterprise.context.spi.Context;

import org.jboss.cdi.tck.api.Configuration;
import org.jboss.cdi.tck.spi.Beans;
import org.jboss.cdi.tck.spi.Contexts;
import org.jboss.cdi.tck.spi.EL;
import org.jboss.cdi.tck.spi.Managers;

/**
 * CDI TCK configuration implementation.
 * 
 * @author Pete Muir
 * @author Martin Kouba
 */
public class ConfigurationImpl implements Configuration {

    private Beans beans;
    private Contexts<? extends Context> contexts;
    private Managers managers;
    private EL el;
    private String libraryDirectory;
    private String testDataSource;

    protected ConfigurationImpl() {
    }

    public ConfigurationImpl(Configuration configuration) {
        this.beans = configuration.getBeans();
        this.contexts = configuration.getContexts();
        this.managers = configuration.getManagers();
        this.el = configuration.getEl();
    }

    public Beans getBeans() {
        return beans;
    }

    public void setBeans(Beans beans) {
        this.beans = beans;
    }

    @SuppressWarnings("unchecked")
    public <T extends Context> Contexts<T> getContexts() {
        return (Contexts<T>) contexts;
    }

    public <T extends Context> void setContexts(Contexts<T> contexts) {
        this.contexts = contexts;
    }

    public Managers getManagers() {
        return managers;
    }

    public void setManagers(Managers managers) {
        this.managers = managers;
    }

    public EL getEl() {
        return el;
    }

    public void setEl(EL el) {
        this.el = el;
    }

    public String getLibraryDirectory() {
        return libraryDirectory;
    }

    public void setLibraryDirectory(String libraryDirectory) {
        this.libraryDirectory = libraryDirectory;
    }

    public String getTestDataSource() {
        return testDataSource;
    }

    public void setTestDataSource(String testDataSource) {
        this.testDataSource = testDataSource;
    }

    @Override
    public String toString() {
        StringBuilder configuration = new StringBuilder();
        configuration.append("JSR 299 TCK Configuration\n");
        configuration.append("-----------------\n");
        configuration.append("\tBeans: ").append(getBeans()).append("\n");
        configuration.append("\tContexts: ").append(getContexts()).append("\n");
        configuration.append("\tEL: ").append(getEl()).append("\n");
        configuration.append("\tManagers: ").append(getManagers()).append("\n");
        configuration.append("\tLibrary dir: ").append(getLibraryDirectory()).append("\n");
        configuration.append("\tTest DS: ").append(getTestDataSource()).append("\n");
        configuration.append("\n");
        return configuration.toString();
    }

}
