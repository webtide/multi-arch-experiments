//
//  ========================================================================
//  Copyright (c) 1995-2016 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.quickstart;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.resource.Resource;
import org.junit.Test;

public class AttributeNormalizerTest
{
    private void assertNormalize(AttributeNormalizer normalizer, Object o, String expected)
    {
        String result = normalizer.normalize(o);
        assertThat("normalize((" + o.getClass().getSimpleName() + ") " + o.toString() + ")",
                result, is(expected));
    }
    
    private void assertExpand(AttributeNormalizer normalizer, String line, String expected)
    {
        String result = normalizer.expand(line);
        assertThat("expand('" + line + "')", result, is(expected));
    }
    
    private void assertEnv(Map<String, String> env) throws IOException
    {
        Map<String, String> oldValues = new HashMap<>();
        env.keySet().stream().forEach((key) ->
        {
            String old = System.getProperty(key);
            oldValues.put(key, old);
        });
        
        try
        {
            String testJettyHome = env.get("jetty.home");
            String testJettyBase = env.get("jetty.base");
            String testWar = env.get("WAR");
            
            oldValues.entrySet().stream()
                    .filter((e) -> e.getValue() != null && !e.getKey().equalsIgnoreCase("WAR"))
                    .forEach((entry) -> System.setProperty(entry.getKey(), entry.getValue()));
            
            Resource webresource = Resource.newResource(testWar);
            AttributeNormalizer normalizer = new AttributeNormalizer(webresource);
            
            // Normalize WAR as String path
            assertNormalize(normalizer, testWar, testWar); // only URL, File, URI are supported
            
            // Normalize jetty.base as File path
            assertNormalize(normalizer, new File(testJettyBase), "${jetty.base}");
            
            // Normalize jetty.home as File path
            assertNormalize(normalizer, new File(testJettyHome), "${jetty.home}");
    
            // Normalize jetty.base as URI path
            assertNormalize(normalizer, new File(testJettyBase).toURI(), "${jetty.base}");
    
            // Normalize jetty.home as URI path
            assertNormalize(normalizer, new File(testJettyHome).toURI(), "${jetty.home}");
    
            // Expand jetty.base
            assertExpand(normalizer, "${jetty.base}", testJettyBase);
    
            // Expand jetty.home
            assertExpand(normalizer, "${jetty.home}", testJettyHome);
            
            // Normalize WAR as URI
            URI testWarURI = new File(testWar).toURI();
            assertNormalize(normalizer, testWarURI, "${WAR}");
            
            // Normalize WAR deep path as File
            File testWarDeep = new File(new File(testWar), "deep/ref").getAbsoluteFile();
            assertNormalize(normalizer, testWarDeep, "${WAR}/deep/ref");
            
            // Normalize WAR deep path as String
            assertNormalize(normalizer, testWarDeep.toString(), testWarDeep.toString());
            
            // Normalize WAR deep path as URI
            assertNormalize(normalizer, testWarDeep.toURI(), "${WAR}/deep/ref");
            
            // Expand WAR deep path
            URI uri = URI.create("jar:" + testWarDeep.toURI().toASCIIString() + "!/other/file");
            assertExpand(normalizer, "jar:${WAR}!/other/file", uri.toASCIIString());
        }
        finally
        {
            oldValues.entrySet().stream().forEach((entry) ->
            {
                EnvUtils.restoreSystemProperty(entry.getKey(), entry.getValue());
            });
        }
    }
    
    @Test
    public void testNormalizeTypical() throws IOException
    {
        Map<String, String> env = new HashMap<>();
        env.put("jetty.home", EnvUtils.toSystemPath("/opt/jetty-distro"));
        env.put("jetty.base", EnvUtils.toSystemPath("/opt/jetty-distro/demo.base"));
        env.put("WAR", EnvUtils.toSystemPath("/opt/jetty-distro/demo.base/webapps/FOO"));
        
        assertEnv(env);
    }
    
    @Test
    public void testNormalizeOverlap() throws IOException
    {
        Map<String, String> env = new HashMap<>();
        env.put("jetty.home", EnvUtils.toSystemPath("/opt/app/dist"));
        env.put("jetty.base", EnvUtils.toSystemPath("/opt/app"));
        env.put("WAR", EnvUtils.toSystemPath("/opt/app/webapps/FOO"));
        
        assertEnv(env);
    }
    
    /**
     * This tests a path scenario often seen on various automatic deployments tooling
     * such as Kubernetes, CircleCI, TravisCI, and Jenkins.
     * @throws IOException
     */
    @Test
    public void testNormalizeNastyPaths() throws IOException
    {
        Map<String, String> env = new HashMap<>();
        env.put("jetty.home", EnvUtils.toSystemPath("/opt/app%2Fnasty/dist"));
        env.put("jetty.base", EnvUtils.toSystemPath("/opt/app%2Fnasty/base"));
        env.put("WAR", EnvUtils.toSystemPath("/opt/app%2Fnasty/base/webapps/FOO"));
        
        assertEnv(env);
    }
    
    /**
     * This tests scenarios on machines where the path has case differences
     * such as MS Windows and OSX
     *
     * @throws IOException
     */
    @Test
    public void testNormalizeCaseDifferences() throws IOException
    {
        Map<String, String> env = new HashMap<>();
        env.put("jetty.home", EnvUtils.toSystemPath("D:\\apps\\jetty-dist"));
        env.put("jetty.base", EnvUtils.toSystemPath("D:\\apps\\our-base"));
        env.put("WAR", EnvUtils.toSystemPath("D:\\apps\\our-base\\webapps\\FOO"));
        
        assertEnv(env);
    }
}
