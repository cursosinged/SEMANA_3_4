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




public class Login implements ServiceProcessing {


    private static final Logger LOG = Logger.getLogger(Login.class.getName());



    @Override
    public void procesaService(HttpServer server, String path) {
        server.createContext(path, (exchange ->
        {
            LOG.info("Se procesa servicio con path: /api/login ");
         UtileriasServicios utileriasServicios=new UtileriasServicios();

            LOG.info("Inicia procesamiento de peticion ...");
            ResponseDTO response = new ResponseDTO();
            String responseText = "";

            try {

                if ("GET".equals(exchange.getRequestMethod())) {
                    LOG.info("Procesando peticion HTTP de tipo GET");
                    UserDTO user = new UserDTO();
                    user = user.getParameters(utileriasServicios.splitQuery(exchange.getRequestURI()));
                    response = utileriasServicios.login(user.getUser(), user.getPassword());
                    JSONObject json = new JSONObject(response);
                    responseText = json.toString();
                    exchange.getResponseHeaders().set("contentType", "application/json; charset=UTF-8");
                    exchange.sendResponseHeaders(200, responseText.getBytes().length);
                } else {
                    /** 405 Method Not Allowed */
                    exchange.sendResponseHeaders(405, -1);
                }


                OutputStream output = exchange.getResponseBody();

                LOG.info("Cerrando recursos del contexto /api/login");
                output.write(responseText.getBytes());
                output.flush();
                output.close();
                exchange.close();


            }
            catch (Exception e)
            {
                LOG.info("PROBLEMA CON SERVICIO /api/login: "+ e.getLocalizedMessage());
            }

        }));

    }


   // @Override
    public void createUser(HttpServer server, String path) {
        server.createContext(path, (exchange -> {

            LOG.info("Se procesa servicio con path: /api/createUser ");
            UtileriasServicios utileriasServicios=new UtileriasServicios();

            //    LOGGER.info("Inicia procesamiento de peticion para contexto /api/createUser");
            ResponseDTO response = new ResponseDTO();
            String responseText = "";

            exchange.getRequestBody();
            if ("POST".equals(exchange.getRequestMethod())) {
                LOG.info("LearningJava - Procesando peticion HTTP de tipo POST");
                UserDTO user =  new UserDTO();
                user = user.getParameters(utileriasServicios.splitQuery(exchange.getRequestURI()));
                response = utileriasServicios.createUser(user.getUser(), user.getPassword());
                JSONObject json = new JSONObject(response);
                responseText = json.toString();
                exchange.getResponseHeaders().set("contentType", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseText.getBytes().length);
            } else {
                /** 405 Method Not Allowed */
                exchange.sendResponseHeaders(405, -1);
            }
            OutputStream output = exchange.getResponseBody();

            LOG.info("Cerrando recursos para contexto /api/createUser");
            output.write(responseText.getBytes());
            output.flush();
            output.close();
            exchange.close();
        }));


    }



}
