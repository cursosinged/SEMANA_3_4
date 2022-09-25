package com.wizeline.factoryMethod.services;

import com.sun.net.httpserver.HttpServer;
import com.wizeline.BO.BankAccountBO;
import com.wizeline.BO.BankAccountBOImpl;
import com.wizeline.DTO.BankAccountDTO;
import com.wizeline.factoryMethod.factory.ServiceProcessing;
import org.json.JSONArray;

import java.io.OutputStream;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * Patrón de diseño implementado: Factory Method
 */



public class GetAccounts implements ServiceProcessing {
    private static final Logger LOGGER = Logger.getLogger(GetAccounts.class.getName());
    private static final int PORT=8080;


    @Override
    public  void procesaService(HttpServer server, String path) {

        server.createContext(path, (exchange -> {
            LOGGER.info("Procesando peticion para /getAccounts");
            BankAccountBO bankAccountBO = new BankAccountBOImpl();

            String responseText = "";
            if ("GET".equals(exchange.getRequestMethod())) {
                LOGGER.info("Procesando peticion HTTP de tipo GET para contexto /api/getUserAccount");
                List<BankAccountDTO> accounts = bankAccountBO.getAccounts();
                JSONArray json = new JSONArray(accounts);
                responseText = json.toString();
                exchange.getResponseHeaders().add("Content-type", "application/json");
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
            } else {
                /** 405 Method Not Allowed */
                exchange.sendResponseHeaders(405, -1);
            }
            OutputStream output = exchange.getResponseBody();

            LOGGER.info("Cerrando recursos para contexto /api/getUserAccount");
            output.write(responseText.getBytes());
            output.flush();
            output.close();
            exchange.close();
        }));

        /** creates a default executor */
        server.setExecutor(null);
        server.start();
        LOGGER.info("LearningJava - Server started on port "+ PORT);


    }



}
