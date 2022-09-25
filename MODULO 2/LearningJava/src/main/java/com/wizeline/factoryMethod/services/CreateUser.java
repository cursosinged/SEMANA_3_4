package com.wizeline.factoryMethod.services;

import com.sun.net.httpserver.HttpServer;
import com.wizeline.DTO.ResponseDTO;
import com.wizeline.DTO.UserDTO;
import com.wizeline.factoryMethod.factory.ServiceProcessing;
import com.wizeline.utils.UtileriasServicios;
import org.json.JSONObject;

import java.io.OutputStream;
import java.util.logging.Logger;


/**
 *
 * Patrón de diseño implementado: Factory Method
 */


public class CreateUser implements ServiceProcessing {



    private static final Logger LOGGER = Logger.getLogger(CreateUser.class.getName());



@Override
    public void procesaService(HttpServer server, String path) {
        server.createContext(path, (exchange -> {

            LOGGER.info("Se procesa servicio con path: /api/createUser ");
            UtileriasServicios utileriasServicios=new UtileriasServicios();

        //    LOGGER.info("Inicia procesamiento de peticion para contexto /api/createUser");
            ResponseDTO response = new ResponseDTO();
            String responseText = "";

            exchange.getRequestBody();
            if ("POST".equals(exchange.getRequestMethod())) {
                LOGGER.info("LearningJava - Procesando peticion HTTP de tipo POST");
                UserDTO user =  new UserDTO();
                user = user.getParameters(utileriasServicios.splitQuery(exchange.getRequestURI()));
                response = utileriasServicios.createUser(user.getUser(), user.getPassword());
                JSONObject json = new JSONObject(response);
                responseText = json.toString();
                exchange.getResponseHeaders().set("contentType", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
            } else {
                LOGGER.info("Metodo no disponible");
                /** 405 Method Not Allowed */
                exchange.sendResponseHeaders(405, -1);
            }
            OutputStream output = exchange.getResponseBody();

            LOGGER.info("Cerrando recursos para contexto /api/createUser");
            output.write(responseText.getBytes());
            output.flush();
            output.close();
            exchange.close();
        }));


    }




}
