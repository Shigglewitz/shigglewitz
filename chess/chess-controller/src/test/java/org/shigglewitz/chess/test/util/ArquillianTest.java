package org.shigglewitz.chess.test.util;

import java.net.URL;

import org.jboss.arquillian.test.api.ArquillianResource;

public class ArquillianTest {
	// http://localhost:8080/Chess/
	@ArquillianResource
	protected URL deploymentUrl;
}
