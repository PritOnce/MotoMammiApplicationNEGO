package com.dam.tfg.MotoMammiApplicationNEGO.services.impl;

import com.dam.tfg.MotoMammiApplicationNEGO.models.CustomerDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.InterfaceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.ProvidersDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.CustomerRepository;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.InterfaceRepository;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.ProviderRepository;
import com.dam.tfg.MotoMammiApplicationNEGO.services.ProcesService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    CustomerRepository customerRepository;

    CustomerDTO customerDTO = new CustomerDTO();
    public void readFileInfo(String pSource, String codProv, String date) {
        try {
            System.out.println("HORA ACTUAL CADA 15 SEGUNDOS: " + dateFormat.format(new Date()) + " pSoruce: " + pSource);
            List<ProvidersDTO> proveedor = providerRepository.retrieve();
            for (ProvidersDTO prov: proveedor) {
                System.out.println(prov.getId() + " " + prov.getCodProv() + " " + prov.getName() + " " + prov.getDateIni() + " " +
                        prov.getDateEnd() + " " + prov.isSwiAct() + " " + LocalDate.now());

                try{
                    String path = getNameFile(pSource, prov.getCodProv(), String.valueOf(LocalDate.now()));

                    if(!new File(path).exists()) {
                        continue;
                    }
                    FileReader fr = new FileReader(path);
                    BufferedReader br = new BufferedReader(fr);
                    String linea;
                    String codigoProveedor = prov.getCodProv();
                    br.readLine();
                    List<String> list = new ArrayList<>();
                    while ( (linea = br.readLine()) != null ){
                        System.out.println(linea);
                        list.add(linea);
                    }
                    setDataOnInterface(list, codigoProveedor);
                }catch (Exception e){
                    System.out.println("FICHERO NO ENCONTRADO");
                }
            }
        }catch (Exception e){
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    @Override
    public void integrateInfo(String pSource, String codProv, String date) {
        Gson gson = new Gson();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        try{
            List<InterfaceDTO> interfaceDTOS = interfaceRepository.retrieve();
            for (InterfaceDTO interfaceDTO: interfaceDTOS) {
                CustomerDTO c= gson.fromJson(interfaceDTO.getContJson(), CustomerDTO.class);
                String traducido = fncTranslate(c.getStreetType(),codProv);
                c.setStreetType(traducido);
                customerRepository.store(c);
                interfaceDTO.setUpdateBy("admin");
                interfaceDTO.setLastUpdate(currentTimestamp);
                interfaceDTO.setStatusProcess("P");
                interfaceRepository.update(interfaceDTO);
            }
        }catch (Exception e){
            InterfaceDTO interfaceDTO = new InterfaceDTO();
            interfaceDTO.setUpdateBy("admin");
            interfaceDTO.setLastUpdate(currentTimestamp);
            interfaceDTO.setCodError("240");
            interfaceDTO.setErrorMessage(e.getMessage());
            interfaceDTO.setStatusProcess("E");
            interfaceRepository.update(interfaceDTO);
        }
    }

    private void setDataOnInterface(List<String> data, String codProv) {
        Gson gson = new Gson();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

        try {
            for (String d : data) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                String[] datos = d.split(",");
                Date dateBirth = dateFormat.parse(datos[5]);

                customerDTO.setDni(datos[0]);
                customerDTO.setName(datos[1]);
                customerDTO.setFirstSurname(datos[2]);
                customerDTO.setLastSurname(datos[3]);
                customerDTO.setEmail(datos[4]);
                customerDTO.setBirthDate(dateBirth);
                customerDTO.setPostalCode(datos[6]);
                customerDTO.setStreetType(datos[7]);
                customerDTO.setCity(datos[8]);
                customerDTO.setNumber(Integer.parseInt(datos[9]));
                customerDTO.setPhone(datos[10]);
                customerDTO.setGender(datos[11]);
                customerDTO.setLicenseType(datos[12]);
                customerDTO.setPlate(datos[13]);

                String json = gson.toJson(customerDTO);

                // Establecer los valores de InterfaceDTO según sea necesario
                interfaceDTO.setCodExternal(datos[0]);
                interfaceDTO.setCodProv(codProv);
                interfaceDTO.setContJson(json);
                interfaceDTO.setCreationDate(currentTimestamp);
                interfaceDTO.setLastUpdate(currentTimestamp);
                interfaceDTO.setCreatedBy("admin");
                interfaceDTO.setUpdateBy("admin");
                interfaceDTO.setCodError(null);
                interfaceDTO.setErrorMessage(null);
                interfaceDTO.setStatusProcess("N");
                interfaceDTO.setOperation("NEW");
                interfaceDTO.setResource("Customer");

                interfaceRepository.store(interfaceDTO);
            }

        } catch (NullPointerException nullPointerException) {
            if(!data.isEmpty()){
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("230");
                interfaceDTO.setErrorMessage(nullPointerException.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
            }
        } catch (Exception e) {
            if(!data.isEmpty()) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("220");
                interfaceDTO.setErrorMessage(e.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
                e.printStackTrace();
            }
        }
    }

    private String fncTranslate(String streetType, String codProv) {

        switch (codProv) {
            case "CAX" -> {
                switch (streetType) {
                    case "Car":
                        streetType = "C/";
                        break;
                    case "Aven":
                        streetType = "AV";
                        break;
                    case "Plz":
                        streetType = "PL";
                        break;
                }
            }
            case "ING" -> {
                switch (streetType) {
                    case "Calle":
                        streetType = "C/";
                        break;
                    case "Avenida":
                        streetType = "AV";
                        break;
                    case "P.":
                        streetType = "PL";
                        break;
                }
            }
            case "BBVA" -> {
                switch (streetType) {
                    case "Camino":
                        streetType = "C/";
                        break;
                    case "Avend":
                        streetType = "AV";
                        break;
                    case "P/":
                        streetType = "PL";
                        break;
                }
            }
            case "COL" -> {
                switch (streetType) {
                    case "Carrer":
                        streetType = "C/";
                        break;
                    case "Avenguda":
                        streetType = "AV";
                        break;
                    case "Plaça":
                        streetType = "PL";
                        break;
                }
            }
        }

        return streetType;
    }

    private String getNameFile(String pSource, String codProv, String date){
        String[] fecha = date.split("-");
        String path = relativePath+pathIn+customerFile+codProv+"_"+fecha[0]+fecha[1]+fecha[2]+extension;

        return path;
    }

}
