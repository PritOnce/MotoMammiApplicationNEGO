package com.dam.tfg.MotoMammiApplicationNEGO.API;

import com.dam.tfg.MotoMammiApplicationNEGO.services.ProcesService;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppInsuranceController
{
    @Autowired
    ProcesService pService;

    @RequestMapping(value =("/readInfo/{resource}/{codprov}/{date}"),
            method = RequestMethod.GET,
            produces = "application/json")
    String callProcessReadInfo(@PathVariable String resource,
                               @PathVariable String codprov,
                               @PathVariable String date//"20240423"
    ){
        try{
            System.out.println("\nEsta tarea se lanza cada 15 segundos");
//            pService.readFileInfo(Constants.SOURCE_CUSTOMER,codprov,date);
            pService.integrateInfo(Constants.SOURCE_CUSTOMER,codprov,date);

        } catch (Exception e){
            System.err.println("heey pero me estoy poniendo peluche yo 😏😏");
        }
        System.out.println("El valor de resource es: "+resource);
        return "Buenos dias";
    }
}
