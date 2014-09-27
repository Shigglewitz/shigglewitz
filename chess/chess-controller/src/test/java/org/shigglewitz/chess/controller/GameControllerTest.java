package org.shigglewitz.chess.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shigglewitz.chess.entity.Game;
import org.shigglewitz.chess.entity.random.RandomUtil;
import org.shigglewitz.chess.maven.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringClientConfiguration({ "applicationContext-Chess.xml",
		"applicationContext-test.xml" })
@RunWith(Arquillian.class)
public class GameControllerTest {
	// http://localhost:8080/Chess/
	@ArquillianResource
	private URL deploymentUrl;

	@Autowired
	private RandomUtil randomUtil;

	@Test
	@OperateOnDeployment("controller")
	public void testViewGame() {
		Game game = this.randomUtil.createRandomDefaultGame();
		ResponseEntity<String> response = null;
		response = ControllerTestHelper.accessUrl(this.deploymentUrl,
				Properties.GAME_CONTROLLER_PATH + "/" + game.getId(),
				HttpMethod.GET);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		String strResponse = response.getBody();
		assertNotNull(strResponse);
		assertTrue("Does not contain <html>: " + strResponse,
				strResponse.contains("<html>"));
		assertTrue("Does not end with </html>: " + strResponse,
				strResponse.endsWith("</html>"));
	}
}
