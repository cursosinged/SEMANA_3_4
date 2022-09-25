package com.wizeline.singleton;


import java.util.logging.Logger;
import java.util.regex.Pattern;


/**
 *
 * Patrón de dieño: Singleton
 */


@SuppressWarnings({ "rawtypes"})
public class ProcesarInfo {
    private static final Logger LOG = Logger.getLogger(ProcesarInfo.class.getName());


    public String fecha;
    private static ProcesarInfo SINGLETON;

    private static Pattern DATE_PATTERN = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");

    public ProcesarInfo(String fecha) {
    }

    private boolean ProcesarInfo(String fecha)
    {
    this.fecha=fecha;

    if(fecha!=null || !fecha.isEmpty())
    {
       return isDateFormatValid(fecha);
    }

        return false;
    }

    public static ProcesarInfo getInstance(String fecha)
    {
        SINGLETON=new ProcesarInfo(fecha);
             return SINGLETON;
    }


    public boolean isDateFormatValid(String fecha) {
        LOG.info("Procesando que la fecha ingresada sea la correcta bajo el patron de diseño SINGLETON .... : "+ fecha );
        return DATE_PATTERN.matcher(fecha).matches();
    }


}
