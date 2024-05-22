package com.dam.tfg.MotoMammiApplicationNEGO.API;

import com.dam.tfg.MotoMammiApplicationNEGO.models.CustomerDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.CustomerRepository;
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

    @RequestMapping(value =("/appInsurance/v1/readInfoFileNEGO/{resource}/{codprov}/{date}"),
            method = RequestMethod.GET,
            produces = "application/json")
    String callProcessReadInfo(@PathVariable String resource,
                               @PathVariable String codprov,
                               @PathVariable String date//"20240423"
    ){
        if (resource.isEmpty()){
            return "ERROR: El resource es obligatorio";
        }

        try{
            System.out.println("\nEsta tarea se lanza cada 15 segundos");
            pService.readFileInfo(resource,codprov,date);

        } catch (Exception e){
            System.err.println("heey pero me estoy poniendo peluche yo 😏😏");
        }
        System.out.println("El valor de resource es: "+resource);
        return "Buenos dias";
    }

    @RequestMapping(value =("/appInsurance/v1/integrateInfoNEGO/{resource}/{codprov}/{date}"),
            method = RequestMethod.GET,
            produces = "application/json")
    String callIntegrateInfo(@PathVariable String resource,
                               @PathVariable String codprov,
                               @PathVariable String date//"20240423"
    ){
        if (resource.isEmpty()){
            return "ERROR: El resource es obligatorio";
        }

        try{
            System.out.println("\nEsta tarea se lanza cada 15 segundos");
            pService.integrateInfo(resource,codprov);

        } catch (Exception e){
            System.err.println("heey pero me estoy poniendo peluche yo 😏😏");
        }
        System.out.println("El valor de resource es: "+resource);
        return "Buenos dias";
    }

    @RequestMapping(value =("/appInsurance/v1/genInvoiceFileNEGO/{codprov}/{date}"),
            method = RequestMethod.GET,
            produces = "application/json")
    String genInvoiceFile(@PathVariable String codprov,
                          @PathVariable String date//"20240423"
    ){

        try{
            System.out.println("\nEsta tarea se lanza cada 15 segundos");
            pService.genInvoiceFile(codprov, date);

        } catch (Exception e){
            System.err.println("heey pero me estoy poniendo peluche yo 😏😏");
        }
        return "Buenos dias";
    }
}
