/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.transport.socket;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.opengamma.util.test.TestGroup;

/**
 * Test.
 */
@Test(groups = TestGroup.INTEGRATION)
public class EndPointDescriptionTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(EndPointDescriptionTest.class);

  private static void testEndPoints(final boolean bind) throws IOException {
    final AbstractServerSocketProcess server = new AbstractServerSocketProcess() {

      @Override
      protected void socketOpened(final Socket socket) {
      }

    };
    if (bind) {
      server.setBindAddress(InetAddress.getLocalHost());
    }
    server.start();
    final FudgeMsg serverEndPoint = server.getEndPointDescription(FudgeContext.GLOBAL_DEFAULT);
    assertNotNull(serverEndPoint);
    LOGGER.info("Server end point {}", serverEndPoint);
    final AbstractSocketProcess client = new AbstractSocketProcess() {

      @Override
      protected void socketOpened(final Socket socket, final BufferedOutputStream os, final BufferedInputStream is) {
      }

    };
    client.setInetAddress(InetAddress.getLocalHost());
    client.setPortNumber(server.getPortNumber());
    client.start();
    final FudgeMsg clientEndPoint = client.getEndPointDescription(FudgeContext.GLOBAL_DEFAULT);
    assertNotNull(clientEndPoint);
    LOGGER.info("Client end point {} ", clientEndPoint);
    assertEquals(serverEndPoint.getString(SocketEndPointDescriptionProvider.TYPE_KEY), clientEndPoint.getString(SocketEndPointDescriptionProvider.TYPE_KEY));
    assertEquals(serverEndPoint.getInt(SocketEndPointDescriptionProvider.PORT_KEY), clientEndPoint.getInt(SocketEndPointDescriptionProvider.PORT_KEY));
    client.stop();
    server.stop();
  }

  /**
   * @throws IOException
   *           if there is a problem
   */
  public void testEndPointsBound() throws IOException {
    testEndPoints(true);
  }

  /**
   * @throws IOException
   *           if there is a problem
   */
  public void testEndPointsUnbound() throws IOException {
    testEndPoints(false);
  }

  /**
   *
   */
  public void testConnectToEndPoint() {
    final AbstractServerSocketProcess server = new AbstractServerSocketProcess() {

      @Override
      protected void socketOpened(final Socket socket) {
      }

    };
    server.start();
    final FudgeMsg serverEndPoint = server.getEndPointDescription(FudgeContext.GLOBAL_DEFAULT);
    assertNotNull(serverEndPoint);
    LOGGER.info("Server end point {}", serverEndPoint);
    final AbstractSocketProcess client = new AbstractSocketProcess() {

      @Override
      protected void socketOpened(final Socket socket, final BufferedOutputStream os, final BufferedInputStream is) {
      }

    };
    client.setServer(serverEndPoint);
    client.start();
    final FudgeMsg clientEndPoint = client.getEndPointDescription(FudgeContext.GLOBAL_DEFAULT);
    assertNotNull(clientEndPoint);
    LOGGER.info("Client end point {} ", clientEndPoint);
    client.stop();
    server.stop();
  }

  /**
   *
   */
  public void testConnectToStaticEndPoint() {
    final AbstractServerSocketProcess server = new AbstractServerSocketProcess() {

      @Override
      protected void socketOpened(final Socket socket) {
      }

    };
    server.start();
    final SocketEndPointDescriptionProvider serverEndPointDescriptor = new SocketEndPointDescriptionProvider();
    serverEndPointDescriptor.setAddress("localhost");
    serverEndPointDescriptor.setPort(server.getPortNumber());
    final FudgeMsg serverEndPoint = serverEndPointDescriptor.getEndPointDescription(FudgeContext.GLOBAL_DEFAULT);
    assertNotNull(serverEndPoint);
    LOGGER.info("Server end point {}", serverEndPoint);
    final AbstractSocketProcess client = new AbstractSocketProcess() {

      @Override
      protected void socketOpened(final Socket socket, final BufferedOutputStream os, final BufferedInputStream is) {
      }

    };
    client.setServer(serverEndPoint);
    client.start();
    final FudgeMsg clientEndPoint = client.getEndPointDescription(FudgeContext.GLOBAL_DEFAULT);
    assertNotNull(clientEndPoint);
    LOGGER.info("Client end point {} ", clientEndPoint);
    client.stop();
    server.stop();
  }

}
