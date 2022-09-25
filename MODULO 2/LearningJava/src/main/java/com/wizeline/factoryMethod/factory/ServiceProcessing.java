package com.wizeline.factoryMethod.factory;

import com.sun.net.httpserver.HttpServer;

/**
 *
 * Patrón de dieño: Factory Method
 */


public interface ServiceProcessing {

    void procesaService(HttpServer server, String path);
}
