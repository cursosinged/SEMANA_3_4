package com.wizeline.factoryMethod.factory;

import com.sun.net.httpserver.HttpServer;
import com.wizeline.factoryMethod.services.CreateUser;
import com.wizeline.factoryMethod.services.GetAccounts;
import com.wizeline.factoryMethod.services.GetUserAccount;
import com.wizeline.factoryMethod.services.Login;
import com.wizeline.utils.exceptions.ExcepcionGenerica;

import java.util.logging.Logger;

/**
 * Patrón de diseño implementado: Factory Method
 */


public class ProcesaTipoServicio {

    private static final Logger LOG = Logger.getLogger(ProcesaTipoServicio.class.getName());

    public ServiceProcessing procesaTipoServicio(HttpServer server, String path) {

        if (server != null && path != null) {
            switch (path) {
                case "/api/login":
                    LOG.info("SE PROCESA SERVICIO login");
                    return new Login();

                case "/api/createUser":
                    LOG.info("SE PROCESA SERVICIO createUser");
                    return new CreateUser();

                case "/api/getUserAccount":
                    LOG.info("SE PROCESA SERVICIO getUserAccount");
                    return new GetUserAccount();

                case "/api/getAccounts":
                    LOG.info("SE PROCESA SERVICIO getAccounts");
                    return new GetAccounts();

                default:
                    throw new ExcepcionGenerica("Opciones no diponibles");

            }
        } else {
            throw new ExcepcionGenerica("Problema con procesamiento");
        }
    }


}
