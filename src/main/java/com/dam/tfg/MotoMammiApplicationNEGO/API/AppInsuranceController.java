package com.dam.tfg.MotoMammiApplicationNEGO.API;

import com.dam.tfg.MotoMammiApplicationNEGO.models.ProcessDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.ProcessRepository;
import com.dam.tfg.MotoMammiApplicationNEGO.services.ProcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppInsuranceController
{
    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ProcesService pService;

    @GetMapping(value =("/appInsurance/v1/readInfoFileNEGO/{resource}"),
            produces = "application/json")
    String callProcessReadInfo(@PathVariable String resource,
                               @RequestParam(value = "codprov", required = false)  String codprov,
                               @RequestParam(value = "date", required = false) String date//"20240423"
                               ){
        if (resource.isEmpty()){
            return "ERROR: El resource es obligatorio";
        }

        try{
            ProcessDTO processDTO = processRepository.search("callProcessReadInfo", "","");

            if (!processDTO.isStatus()){
                throw new Exception("callProcessReadInfo no esta disponible");
            }

            System.out.println("\nEsta tarea se lanza cada 15 segundos");
            pService.readFileInfo(resource,codprov,date);

        } catch (Exception e){
            System.err.println("heey pero me estoy poniendo peluche yo 😏😏");
        }
        System.out.println("El valor de resource es: "+resource);
        return "Buenos dias";
    }

    @GetMapping(value =("/appInsurance/v1/processInfoFileNEGO/{resource}"),
            produces = "application/json")
    String callIntegrateInfo(@PathVariable String resource,
                               @RequestParam(value = "codprov", required = false) String codprov,
                               @RequestParam(value = "date", required = false) String date//"20240423"
    ) throws Exception {
        ProcessDTO processDTO = processRepository.search("callIntegrateInfo", "","");

        if (!processDTO.isStatus()){
            throw new Exception("callProcessReadInfo no esta disponible");
        }

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

    @GetMapping(value = "/appInsurance/v1/genInvoiceFileNEGO", produces = "application/json")
    String genInvoiceFile(@RequestParam(value = "codprov", required = false) String codprov,
                                 @RequestParam(value = "date", required = false) String date) throws Exception {

        ProcessDTO processDTO = processRepository.search("genInvoiceFile", "","");

        if (!processDTO.isStatus()){
            throw new Exception("callProcessReadInfo no esta disponible");
        }

        try {
            System.out.println("\nEsta tarea se lanza cada 15 segundos");
            pService.genInvoiceFile(codprov, date);
        } catch (Exception e) {
            System.err.println("heey pero me estoy poniendo peluche yo 😏😏");
            e.printStackTrace();
        }
        return "Buenos dias";
    }
}
