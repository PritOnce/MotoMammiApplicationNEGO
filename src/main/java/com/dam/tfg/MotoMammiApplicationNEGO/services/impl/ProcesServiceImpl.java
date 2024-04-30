package com.dam.tfg.MotoMammiApplicationNEGO.services.impl;

import com.dam.tfg.MotoMammiApplicationNEGO.models.ProvidersDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.InterfaceRepository;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.ProviderRepository;
import com.dam.tfg.MotoMammiApplicationNEGO.services.ProcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ProcesServiceImpl implements ProcesService {

    @Value("${nameFile.customers}")
    private String customerFile;
    @Value("${nameFile.providers}")
    private String providersFile;
    @Value("${nameFile.parts}")
    private String insuranceFile;
    @Value("${nameFile.customers.ext}")
    private String extension;
    @Value("${relative.path}")
    private String relativePath;
    @Value("${path.in}")
    private String pathIn;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    InterfaceRepository interfaceRepository;

    public void readFileInfo(String pSource){
        try {
            System.out.println("HORA ACTUAL CADA 15 SEGUNDOS: " + dateFormat.format(new Date()) + " pSoruce: " + pSource);
            List<ProvidersDTO> proveedor = providerRepository.retrieve();
            for (ProvidersDTO prov: proveedor) {
                System.out.println(prov.getId() + " " + prov.getCodProv() + " " + prov.getName() + " " + prov.getDateIni() + " " +
                        prov.getDateEnd() + " " + prov.isSwiAct() + " " + java.time.LocalDate.now());

                try{
                    String path = getNameFile(pSource, prov.getCodProv(), String.valueOf(java.time.LocalDate.now()));
                    FileReader fr = new FileReader(path);
                    BufferedReader br = new BufferedReader(fr);
                    String linea;
                    linea = br.readLine();
                    while ( (linea = br.readLine()) != null ){
                        setDataOnInterface(linea);
                    }
                }catch (Exception e){
                    System.out.println("FICHERO NO ENCONTRADO");
                }
            }
        }catch (Exception e){
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    private String getNameFile(String pSource, String codProv, String date){
        String[] fecha = date.split("-");
        String path = relativePath+pathIn+customerFile+codProv+"_"+fecha[0]+fecha[1]+fecha[2]+extension;

        return path;
    }

    private void setDataOnInterface(String data){
        String[] lineaSplit = data.split(",");
    }
}
