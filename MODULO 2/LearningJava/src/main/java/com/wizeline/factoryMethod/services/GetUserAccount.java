package com.wizeline.factoryMethod.services;

import com.sun.net.httpserver.HttpServer;
import com.wizeline.DTO.BankAccountDTO;
import com.wizeline.DTO.ResponseDTO;
import com.wizeline.DTO.UserDTO;
import com.wizeline.factoryMethod.factory.ServiceProcessing;
import com.wizeline.singleton.ProcesarInfo;
import com.wizeline.utils.UtileriasServicios;
import org.json.JSONObject;

import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.logging.Logger;

import static com.wizeline.utils.Utils.isPasswordValid;


/**
 *
 * Patrón de diseño implementado: Factory Method
 */




public class GetUserAccount implements ServiceProcessing {
    private static final Logger LOGGER = Logger.getLogger(GetUserAccount.class.getName());
    private static final String SUCCESS_CODE = "OK000";

    @Override
    public void procesaService(HttpServer server, String path) {

        // Consultar información de cuenta de un usuario
            server.createContext(path, (exchange -> {
            UtileriasServicios utileriasServicios=new UtileriasServicios();

            LOGGER.info("Inicia procesamiento de peticion para contexto /api/getUserAccount");
            Instant inicioEjecucion= Instant.now();
            ResponseDTO response = new ResponseDTO();

            String responseText = "";
            if ("GET".equals(exchange.getRequestMethod())) {
                LOGGER.info("PROCESANDO INFOMACION DE ENTRADA TIPO GET para contexto /api/getUserAccount");
                UserDTO user =  new UserDTO();
                Map<String, String> params = utileriasServicios.splitQuery(exchange.getRequestURI());
                user = user.getParameters(utileriasServicios.splitQuery(exchange.getRequestURI()));
                // Valida formato del parametro fecha (date) [dd-mm-yyyy]
                String lastUsage = params.get("date");//params.get("date");

                ProcesarInfo validaFecha=ProcesarInfo.getInstance(lastUsage);

                /**
                 *
                 * Implementando patroin de diseño Singleton
                 */

                 if(validaFecha.isDateFormatValid(lastUsage)){//if (isDateFormatValid(lastUsage)) {
                    LOGGER.info("PASSWORD CORRECTO, SE RPOCESA SIGUIENTE VALIDACION");

                    // Valida el password del usuario (password)
                    if (isPasswordValid(user.getPassword())) {
                        LOGGER.info("PASSWORD CORRECTO");
                        response = utileriasServicios.login(user.getUser(), user.getPassword());
                        if (response.getCode().equals(SUCCESS_CODE)) {
                            BankAccountDTO bankAccountDTO = utileriasServicios.getAccountDetails(user.getUser(), lastUsage);
                            JSONObject json = new JSONObject(bankAccountDTO);
                            responseText = json.toString();
                            exchange.getResponseHeaders().add("Content-type", "application/json");
                            exchange.sendResponseHeaders(200, responseText.getBytes().length);
                        }
                    }
                    else {
                        responseText = "Password Incorrecto";
                        exchange.getResponseHeaders().add("Content-type", "application/json");
                        exchange.sendResponseHeaders(401, responseText.getBytes().length);
                    }


                }

                else {
                    responseText = "Formato de Fecha Incorrecto";
                    exchange.getResponseHeaders().add("Content-type", "application/json");
                    exchange.sendResponseHeaders(400, responseText.getBytes().length);
                }


            } else {
                /** 405 Method Not Allowed */
                exchange.sendResponseHeaders(405, -1);
            }
            OutputStream output = exchange.getResponseBody();
            Instant finaljecucion= Instant.now();

            LOGGER.info("Cerrando recursos para contexto /api/getUserAccount");

            String total= new String(String.valueOf(Duration.between(inicioEjecucion, finaljecucion).toMillis()).concat(" milisegundos "));
            LOGGER.info("Tiempo de ejecucion final para contexto /api/getUserAccount: ".concat(total));

            output.write(responseText.getBytes());
            output.flush();
            output.close();
            exchange.close();
        }));

    }
}
