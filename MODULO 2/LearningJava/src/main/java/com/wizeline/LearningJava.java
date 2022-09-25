package com.wizeline;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.wizeline.factoryMethod.factory.ProcesaTipoServicio;
import com.wizeline.factoryMethod.factory.ServiceProcessing;
import com.wizeline.utils.UtileriasServicios;
import com.wizeline.utils.exceptions.ExcepcionGenerica;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.net.httpserver.HttpServer;
import com.wizeline.BO.BankAccountBO;
import com.wizeline.BO.BankAccountBOImpl;
import com.wizeline.DTO.BankAccountDTO;
import com.wizeline.DTO.ResponseDTO;


@SuppressWarnings({"deprecation", "rawtypes"})
public class LearningJava extends Thread {

    private static final Logger LOGGER = Logger.getLogger(LearningJava.class.getName());
    private static final int PORT = 8080;

    private static String responseTextThread = "";
    private ResponseDTO response;
    private static String textThread = "";

    public static String path1 = "/api/login";
    public static String path2 = "/api/createUser";
    public static String path3 = "/api/getUserAccount";
    public static String path4 = "/api/getAccounts";

    /**
     * Descripcion: Metodo principal del proyecto LearningJava
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {


        /**
         *
         * Implementando patron de diseño Factory method para sericios:
         * Login
         * CreateUser
         * GetAccounts
         * GetUserAccount
         */


        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);

        ProcesaTipoServicio procesaTipoServicio = new ProcesaTipoServicio();
        ServiceProcessing serviceProcessing = procesaTipoServicio.procesaTipoServicio(server, path1);
        serviceProcessing.procesaService(server, path1);

        ServiceProcessing serviceProcessing2 = procesaTipoServicio.procesaTipoServicio(server, path2);
        serviceProcessing2.procesaService(server, path2);

        ServiceProcessing serviceProcessing3 = procesaTipoServicio.procesaTipoServicio(server, path3);
        serviceProcessing3.procesaService(server, path3);

        ServiceProcessing serviceProcessing4 = procesaTipoServicio.procesaTipoServicio(server, path4);
        serviceProcessing4.procesaService(server, path4);


        //// ----- ENDPOINT PARA HILOS ----


        // Crear usuarios
        server.createContext("/api/createUsers", (exchange -> {
            LOGGER.info("Inicia proceso para hilos ...");
            ResponseDTO response = new ResponseDTO();
            /** Validates the type of http request  */
            exchange.getRequestBody();
            if ("POST".equals(exchange.getRequestMethod())) {
                LOGGER.info("Procesando peticion HTTP de tipo POST para Funcion de Hilos");

                // Obtenemos el request del body que mandamos
                StringBuilder text = new StringBuilder();
                try (Scanner scanner = new Scanner(exchange.getRequestBody())) {
                    while (scanner.hasNext()) {
                        text.append(scanner.next());
                    }
                } catch (Exception e) {
                    LOGGER.severe(e.getMessage());
                    throw new ExcepcionGenerica(e.getMessage());

                }

                textThread = text.toString();

                LOGGER.info("textThread: " + textThread);
                // Iniciamos thread
                LearningJava thread = new LearningJava();
                thread.start();

                // Esperamos a que termine el thread
                while (thread.isAlive()) ;

                exchange.getResponseHeaders().set("contentType", "application/json; charset=UTF-8");
                exchange.sendResponseHeaders(200, responseTextThread.getBytes().length);
            } else {
                /** 405 Method Not Allowed */
                exchange.sendResponseHeaders(405, -1);
            }
            OutputStream output = exchange.getResponseBody();

            LOGGER.info("LearningJava - Cerrando recursos para contexto /api/createUsers");
            output.write(responseTextThread.getBytes());
            output.flush();
            output.close();
            exchange.close();
        }));


        /// ENDPOINT PARA IMPLEMENTAR PROGRAMACION FUNCIONAL

        // Consultar todas las cuentas y agruparlas por su tipo utilizando Programación Funcional
        server.createContext("/api/getAccountsGroupByType", (exchange -> {
            LOGGER.info("Inicia servicio de con contexto : /api/getAccountsGroupByType");

            LOGGER.info("Peticion de entrada para contexto /api/getAccountsGroupByType " + exchange.getRequestBody().toString());
            Instant inicioDeEjecucion = Instant.now();
            BankAccountBO bankAccountBO = new BankAccountBOImpl();
            String responseText = "";
            /** Validates the type of http request  */
            if ("GET".equals(exchange.getRequestMethod())) {
                LOGGER.info("LearningJava - Procesando peticion HTTP de tipo GET");
                List<BankAccountDTO> accounts = bankAccountBO.getAccounts();


                // Aqui implementaremos la programación funcional
                Map<String, List<BankAccountDTO>> groupedAccounts;
                Function<BankAccountDTO, String> groupFunction = (account) -> account.getAccountType().toString();
                groupedAccounts = accounts.stream().collect(Collectors.groupingBy(groupFunction));

                JSONObject json = new JSONObject(groupedAccounts);
                responseText = json.toString();
                exchange.getResponseHeaders().add("Content-type", "application/json");
                exchange.sendResponseHeaders(200, responseText.getBytes().length);


            } else {
                /** 405 Method Not Allowed */
                exchange.sendResponseHeaders(405, -1);
            }
            OutputStream output = exchange.getResponseBody();
            Instant finalDeEjecucion = Instant.now();
            /**
             * Always remember to close the resources you open.
             * Avoid memory leaks
             */
            LOGGER.info("LearningJava - Cerrando recursos ...");
            String total = new String(String.valueOf(Duration.between(inicioDeEjecucion, finalDeEjecucion).toMillis()).concat(" segundos."));
            LOGGER.info("Tiempo de respuesta: ".concat(total));
            output.write(responseText.getBytes());
            output.flush();
            output.close();
            exchange.close();
        }));


    }

    @Override
    public void run() {
        try {
            crearUsuarios();
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new ExcepcionGenerica(e.getMessage());
        }
    }


    @Deprecated(since = "Anotaciones update")
    private void createUsers() {
        try {
            UtileriasServicios utileriasServicios = new UtileriasServicios();
            String user = "user";
            String pass = "password";
            JSONArray jsonArray = new JSONArray(textThread);
            JSONObject user1 = new JSONObject(jsonArray.get(0).toString());
            JSONObject user2 = new JSONObject(jsonArray.get(1).toString());
            JSONObject user3 = new JSONObject(jsonArray.get(2).toString());

            // Creamos usuario 1
            response = utileriasServicios.createUser(user1.getString(user), user1.getString(pass));
            responseTextThread = new JSONObject(response).toString();
            LOGGER.info("Usuario 1: " + responseTextThread);
            Thread.sleep(1000);

            // Creamos usuario 2
            response = utileriasServicios.createUser(user2.getString(user), user2.getString(pass));
            responseTextThread = new JSONObject(response).toString();
            LOGGER.info("Usuario 2: " + responseTextThread);
            Thread.sleep(1000);

            // Creamos usuario 3
            response = utileriasServicios.createUser(user3.getString(user), user3.getString(pass));
            responseTextThread = new JSONObject(response).toString();
            LOGGER.info("Usuario 3: " + responseTextThread);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new ExcepcionGenerica(e.getMessage());
        }
    }


    private void crearUsuarios() {
        try {
            UtileriasServicios utileriasServicios = new UtileriasServicios();
            String user = "user";
            String pass = "password";
            JSONArray jsonArray = new JSONArray(textThread);
            JSONObject userJson;

            ResponseDTO response = null;

            LOGGER.info("jsonArray.length(): " + jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++)    // Procesa numero de usuarios con el numero de hilos correspondiente
            {
                userJson = new JSONObject(jsonArray.get(i).toString());
                response = utileriasServicios.createUser(userJson.getString(user), userJson.getString(pass));
                responseTextThread = new JSONObject(response).toString();
                LOGGER.info("Usuario " + (i + 1) + ": " + responseTextThread);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
            throw new ExcepcionGenerica(e.getMessage());
        }
    }


}
