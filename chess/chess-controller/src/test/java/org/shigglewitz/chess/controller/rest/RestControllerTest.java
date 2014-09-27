package org.shigglewitz.chess.controller.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.spring.integration.test.annotation.SpringClientConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.shigglewitz.chess.controller.ControllerTestHelper;
import org.shigglewitz.chess.maven.Properties;
import org.shigglewitz.chess.representation.GameRepresentation;
import org.shigglewitz.chess.representation.PlayerRepresentation;
import org.shigglewitz.chess.test.util.ArquillianTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@SpringClientConfiguration({ "applicationContext-Chess.xml" })
@RunWith(Arquillian.class)
public class RestControllerTest extends ArquillianTest {
	@Test
	@OperateOnDeployment("controller")
	public void testCreatePlayer() throws JsonParseException,
	JsonMappingException, IOException {
		this.createPlayer();
	}

	@Test
	@OperateOnDeployment("controller")
	public void testCreateGame() throws JsonParseException,
	JsonMappingException, IOException {
		PlayerRepresentation lightPlayer = this.createPlayer();
		PlayerRepresentation darkPlayer = this.createPlayer();

		this.createGame(lightPlayer, darkPlayer);
	}

	@Test
	@OperateOnDeployment("controller")
	public void testViewGame() throws JsonParseException, JsonMappingException,
	IOException {
		PlayerRepresentation lightPlayer = this.createPlayer();
		PlayerRepresentation darkPlayer = this.createPlayer();

		GameRepresentation game = this.createGame(lightPlayer, darkPlayer);

		ResponseEntity<String> response = null;
		response = ControllerTestHelper.accessUrl(this.deploymentUrl,
				Properties.GAME_REST_CONTROLLER_PATH + "/" + game.getId(),
				HttpMethod.GET, null);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		GameRepresentation gr = ControllerTestHelper.getEntity(
				response.getBody(), GameRepresentation.class);
		assertNotNull(gr);
		assertEquals(game.getId(), gr.getId());
		assertEquals(lightPlayer.getId(), gr.getLightPlayer());
		assertEquals(darkPlayer.getId(), gr.getDarkPlayer());
	}

	@Test
	@OperateOnDeployment("controller")
	public void testInvalidParametersViewGame() {
		ResponseEntity<String> response = null;
		response = ControllerTestHelper.accessUrl(
				this.deploymentUrl,
				Properties.GAME_REST_CONTROLLER_PATH + "/"
						+ RandomStringUtils.randomAlphanumeric(20),
						HttpMethod.GET, null);
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	protected PlayerRepresentation createPlayer() {
		ResponseEntity<String> response = null;
		MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();

		response = ControllerTestHelper.accessUrl(this.deploymentUrl,
				Properties.PLAYER_REST_CONTROLLER_PATH, HttpMethod.POST,
				postParams);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		PlayerRepresentation pr = ControllerTestHelper.getEntity(
				response.getBody(), PlayerRepresentation.class);
		assertNotNull(pr);
		assertNotNull(pr.getId());

		return pr;
	}

	protected GameRepresentation createGame(PlayerRepresentation lightPlayer,
			PlayerRepresentation darkPlayer) {
		MultiValueMap<String, String> postParams = new LinkedMultiValueMap<>();
		postParams.add("lightPlayer", lightPlayer.getId().toString());
		postParams.add("darkPlayer", darkPlayer.getId().toString());

		ResponseEntity<String> response = null;
		response = ControllerTestHelper.accessUrl(this.deploymentUrl,
				Properties.GAME_REST_CONTROLLER_PATH, HttpMethod.POST,
				postParams);
		assertEquals(HttpStatus.OK, response.getStatusCode());

		GameRepresentation gr = ControllerTestHelper.getEntity(
				response.getBody(), GameRepresentation.class);
		assertNotNull(gr);
		assertNotNull(gr.getId());
		assertEquals(lightPlayer.getId(), gr.getLightPlayer());
		assertEquals(darkPlayer.getId(), gr.getDarkPlayer());

		return gr;
	}
}
