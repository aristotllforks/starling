/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.util.rest;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.authz.AuthorizationException;
import org.mockito.Mockito;
import org.testng.annotations.Test;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.util.test.TestGroup;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

/**
 * Test {@link AuthorizationExceptionMapper}.
 */
@Test(groups = TestGroup.UNIT)
public class AuthorizationExceptionMapperTest extends AbstractExceptionMapperTestHelper {

  /**
   * Tests the mapping for the data types.
   *
   * @param mediaType  the media type
   * @throws Exception  if there is a problem
   */
  @Test(dataProvider = "mediaTypes")
  public void testMapping(final MediaType mediaType) throws Exception {
    final AuthorizationException ex = new AuthorizationException("Test message");
    final AuthorizationExceptionMapper mapper = new AuthorizationExceptionMapper();
    init(mapper, mediaType);

    final Response test = mapper.toResponse(ex);
    testResult(test, Status.FORBIDDEN, ex);
  }

  /**
   * Tests the output message when the exception is null.
   *
   * @param mediaType  the media type
   * @throws Exception  if there is a problem
   */
  @Test(dataProvider = "mediaTypes")
  public void testNoErrorMessage(final MediaType mediaType) throws Exception {
    final AuthorizationException ex = new AuthorizationException("Test message");
    final AuthorizationExceptionMapper mapper = new AuthorizationExceptionMapper();
    init(mapper, mediaType);

    final Map<String, String> data = mapper.getMessage(ex);
    mapper.buildOutputMessage(null, data);
    assertEquals(data.size(), 3);
    assertEquals(data.get("message"), "");
    assertEquals(data.get("user"), "permissive");
    assertEquals(data.get("locator"), "");
  }

  /**
   * Tests the output message when the exception has no message.
   *
   * @param mediaType  the media type
   * @throws Exception  if there is a problem
   */
  @Test(dataProvider = "mediaTypes")
  public void testNoOutputMessage(final MediaType mediaType) throws Exception {
    final AuthorizationException ex = new AuthorizationException("Test message");
    final AuthorizationExceptionMapper mapper = new AuthorizationExceptionMapper();
    init(mapper, mediaType);

    final Map<String, String> data = mapper.getMessage(ex);
    mapper.buildOutputMessage(new IllegalArgumentException(), data);
    assertEquals(data.size(), 3);
    assertEquals(data.get("message"), "");
    assertEquals(data.get("user"), "permissive");
    assertTrue(data.get("locator").startsWith(
        "<p>IllegalArgumentException<br>&nbsp;&nbsp;at com.opengamma.util.rest.AuthorizationExceptionMapperTest.testNoOutputMessage()"));
    assertTrue(data.get("locator").endsWith("</p>"));
  }

  /**
   * Tests the output message when the exception has no message.
   *
   * @param mediaType  the media type
   * @throws Exception  if there is a problem
   */
  @Test(dataProvider = "mediaTypes")
  public void testOutputMessage(final MediaType mediaType) throws Exception {
    final AuthorizationException ex = new AuthorizationException("Test message");
    final AuthorizationExceptionMapper mapper = new AuthorizationExceptionMapper();
    init(mapper, mediaType);

    final Map<String, String> data = mapper.getMessage(ex);
    final String message = "Reason for exception";
    mapper.buildOutputMessage(new OpenGammaRuntimeException(message), data);
    assertEquals(data.size(), 3);
    assertEquals(data.get("message"), message);
    assertEquals(data.get("user"), "permissive");
    assertTrue(data.get("locator").startsWith(
        "<p>OpenGammaRuntimeException<br>&nbsp;&nbsp;at com.opengamma.util.rest.AuthorizationExceptionMapperTest.testOutputMessage()"));
    assertTrue(data.get("locator").endsWith("</p>"));
  }

  /**
   * Tests the output message when the exception is a UniformInterfaceException.
   *
   * @param mediaType  the media type
   * @throws Exception  if there is a problem
   */
  @Test(dataProvider = "mediaTypes")
  public void testUieOutputMessage(final MediaType mediaType) throws Exception {
    final AuthorizationException ex = new AuthorizationException("Test message");
    final AuthorizationExceptionMapper mapper = new AuthorizationExceptionMapper();
    init(mapper, mediaType);

    final Map<String, String> data = mapper.getMessage(ex);
    final ClientResponse cr = Mockito.mock(ClientResponse.class);
    final UniformInterfaceException204NoContent uie = new UniformInterfaceException204NoContent(cr);
    mapper.buildOutputMessage(uie, data);
    assertEquals(data.size(), 3);
    assertTrue(data.get("message").startsWith("Mock for ClientResponse,"));
    assertEquals(data.get("user"), "permissive");
    assertEquals(data.get("locator"), "");
  }

}
