package com.dam.tfg.MotoMammiApplicationNEGO.services.impl;

import com.dam.tfg.MotoMammiApplicationNEGO.models.InterfaceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.ProvidersDTO;
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

    public void readFileInfo(String pSource){
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

    private String getNameFile(String pSource, String codProv, String date){
        String[] fecha = date.split("-");
        String path = relativePath+pathIn+customerFile+codProv+"_"+fecha[0]+fecha[1]+fecha[2]+extension;

        return path;
    }

    private void setDataOnInterface(List<String> data, String codProv) {
        List<InterfaceDTO> interfaceDTOList = new ArrayList<InterfaceDTO>();
        Gson gson = new Gson();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        JsonObject jsonObject = new JsonObject();
        try {
            for (String d : data) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                String[] datos = d.split(",");

                jsonObject.addProperty("DNI", datos[0]);
                jsonObject.addProperty("Nombre", datos[1]);
                jsonObject.addProperty("Apellido1", datos[2]);
                jsonObject.addProperty("Apellido2", datos[3]);
                jsonObject.addProperty("CorreoElectronico", datos[4]);
                jsonObject.addProperty("FechaNacimiento", datos[5]);
                jsonObject.addProperty("TipoVia", datos[6]);
                jsonObject.addProperty("Ciudad", datos[7]);
                jsonObject.addProperty("Numero", datos[8]);
                jsonObject.addProperty("Telefono", datos[9]);
                jsonObject.addProperty("Sexo", datos[10]);

                String json = gson.toJson(jsonObject);
                System.out.println(json);

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
                interfaceDTO.setStatusProcess("P");
                interfaceDTO.setOperation("NEW");
                interfaceDTO.setResource("Customer");

                interfaceDTOList.add(interfaceDTO);

            }
            interfaceRepository.store(interfaceDTOList);
        } catch (NullPointerException nullPointerException) {
            if(!data.isEmpty()){
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("220");
                interfaceDTO.setErrorMessage(nullPointerException.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(Arrays.asList(interfaceDTO));
            }
        } catch (Exception e) {
            if(!data.isEmpty()) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("220");
                interfaceDTO.setErrorMessage(e.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(Arrays.asList(interfaceDTO));
            }
        }
    }

}
