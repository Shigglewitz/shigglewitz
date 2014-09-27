package org.shigglewitz.chess.controller;

import static org.junit.Assert.assertEquals;

import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shigglewitz.chess.maven.Properties;

@RunWith(Arquillian.class)
public class DeploymentsTest {
    @Test
    @OperateOnDeployment("controller")
    public void testController(@ArquillianResource URL deploymentUrl)
            throws URISyntaxException {
        assertEquals("Unable to deploy chess war", "http://localhost:8080/"
                + Properties.WAR_NAME + "/", deploymentUrl.toString());
    }
}
