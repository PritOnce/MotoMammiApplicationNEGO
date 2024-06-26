package com.dam.tfg.MotoMammiApplicationNEGO.services.impl;

import com.dam.tfg.MotoMammiApplicationNEGO.models.*;
import com.dam.tfg.MotoMammiApplicationNEGO.repositories.*;
import com.dam.tfg.MotoMammiApplicationNEGO.services.ProcesService;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProcesServiceImpl implements ProcesService {

    @Value("${nameFile.customers}")
    private String customerFile;
    @Value("${nameFile.vehicles}")
    private String vehicleFile;
    @Value("${nameFile.parts}")
    private String partsFile;
    @Value("${nameFile.invoice}")
    private String invoiceFile;
    @Value("${nameFile.customers.ext}")
    private String extension;
    @Value("${nameFile.invoices.ext}")
    private String invoiceExtension;
    @Value("${relative.path}")
    private String relativePath;
    @Value("${path.in}")
    private String pathIn;
    @Value("${path.out}")
    private String pathOut;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    ProviderRepository providerRepository;
    @Autowired
    InterfaceRepository interfaceRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    PartRepository partRepository;
    @Autowired
    VehicleRepository vehicleRepository;
    @Autowired
    TranslationRepository translationRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoicesRepository invoicesRepository;

    CustomerDTO customerDTO = new CustomerDTO();
    VehicleDTO vehicleDTO = new VehicleDTO();
    PartsDTO partsDTO = new PartsDTO();

    public void readFileInfo(String pSource, String codProv, String date) {
        try {
            System.out.println("HORA ACTUAL CADA 15 SEGUNDOS: " + dateFormat.format(new Date()) + " pSoruce: " + pSource);
            List<ProvidersDTO> proveedor = providerRepository.retrieve("", "");
            for (ProvidersDTO prov: proveedor) {
                System.out.println(prov.getId() + " " + prov.getCodProv() + " " + prov.getName() + " " + prov.getDateIni() + " " +
                        prov.getDateEnd() + " " + prov.isSwiAct() + " " + LocalDate.now());

                try{
                    String codigoProveedor;

                    if (codProv == null){
                        codigoProveedor = prov.getCodProv();
                    } else {
                        codigoProveedor = codProv;
                    }
                    String path = getNameFile(pSource, codProv, date, prov.getCodProv());

                    File file = new File(path);
                    if (!file.exists()) {
                        System.out.println("FICHERO NO ENCONTRADO: " + path);
                        continue; // Salta al siguiente proveedor si el fichero no existe
                    }

                    FileReader fr = new FileReader(path);
                    BufferedReader br = new BufferedReader(fr);
                    String linea;
                    br.readLine();
                    List<String> list = new ArrayList<>();
                    while ( (linea = br.readLine()) != null ){
                        System.out.println(linea);
                        list.add(linea);
                    }

                    br.close();
                    switch (pSource){
                        case Constants.SOURCE_CUSTOMER:
                            serDataInterfaceCustomer(list, codigoProveedor, pSource);
                            break;
                        case Constants.SOURCE_VEHICLES:
                            serDataInterfaceVehicles(list, codigoProveedor, pSource);
                            break;
                        case Constants.SOURCE_PARTS:
                            serDataInterfaceParts(list, codigoProveedor, pSource);
                            break;
                    }

                }catch (Exception e){
                    System.out.println("FICHERO NO ENCONTRADO");
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            System.out.println("ERROR");
            e.printStackTrace();
        }
    }

    private void serDataInterfaceCustomer(List<String> data, String codProv, String pSource) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS'Z'").create();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            for (String d : data) {

                if (d.isEmpty()){
                    continue;
                }

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
                interfaceDTO.setResource(pSource);

                int valid = validateInfo(interfaceDTO, json, Constants.SOURCE_CUSTOMER);
                System.out.println("VALOR DE VALID: " + valid);
                if(valid == 0){
                    interfaceRepository.store(interfaceDTO);
                }else if (valid == 1){
                    interfaceDTO.setLastUpdate(currentTimestamp);
                    interfaceDTO.setUpdateBy("sistema de comparacion");
                    interfaceDTO.setOperation("UPDATE");
                    interfaceRepository.store(interfaceDTO);
                }else if (valid == 2){
                    System.out.println("COINCIDENCIA EN LA ENTRADA DE LOS DATOS");
                }
            }

        } catch (NullPointerException nullPointerException) {
            if(!data.isEmpty()){
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("230-IntCust");
                interfaceDTO.setErrorMessage(nullPointerException.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
            }
        } catch (Exception e) {
            if(!data.isEmpty()) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("220-IntCust");
                interfaceDTO.setErrorMessage(e.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
                e.printStackTrace();
            }
        }
    }

    private void serDataInterfaceVehicles(List<String> data, String codProv, String pSource) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        try {
            for (String d : data) {

                if (d.isEmpty()){
                    continue;
                }

                InterfaceDTO interfaceDTO = new InterfaceDTO();
                String[] datos = d.split(",");

                vehicleDTO.setPlate(datos[0]);
                vehicleDTO.setVehicleType(datos[1]);
                vehicleDTO.setBrand(datos[2]);
                vehicleDTO.setModel(datos[3]);
                vehicleDTO.setDni_customer(datos[4]);

                String json = gson.toJson(vehicleDTO);


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
                interfaceDTO.setResource(pSource);

                int valid = validateInfo(interfaceDTO, json, Constants.SOURCE_VEHICLES);
                System.out.println("VALOR DE VALID: " + valid);
                if(valid == 0){
                    interfaceRepository.store(interfaceDTO);
                }else if (valid == 1){
                    interfaceDTO.setLastUpdate(currentTimestamp);
                    interfaceDTO.setUpdateBy("sistema de comparacion");
                    interfaceDTO.setOperation("UPDATE");
                    interfaceRepository.store(interfaceDTO);
                }else if (valid == 2){
                    System.out.println("COINCIDENCIA EN LA ENTRADA DE LOS DATOS");
                }
            }

        } catch (NullPointerException nullPointerException) {
            if(!data.isEmpty()){
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("230-IntVehcle");
                interfaceDTO.setErrorMessage(nullPointerException.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
            }
        } catch (Exception e) {
            if(!data.isEmpty()) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("220-IntVehcle");
                interfaceDTO.setErrorMessage(e.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
                e.printStackTrace();
            }
        }
    }

    private void serDataInterfaceParts(List<String> data, String codProv, String pSource) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");


        try {
            for (String d : data) {

                if (d.isEmpty()){
                    continue;
                }

                InterfaceDTO interfaceDTO = new InterfaceDTO();
                String[] datos = d.split(",");
                Date dateClaim = dateFormat.parse(datos[3]);

                partsDTO.setDni(datos[0]);
                partsDTO.setClaimNumber(datos[1]);
                partsDTO.setPolicyNumber(datos[2]);
                partsDTO.setClaimDate(dateClaim);
                partsDTO.setDescription(datos[4]);
                partsDTO.setStatus((datos[5]));
                partsDTO.setAmount(Double.parseDouble(datos[6]));

                String json = gson.toJson(partsDTO);


                // Establecer los valores de InterfaceDTO según sea necesario
                interfaceDTO.setCodExternal(datos[1]);
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
                interfaceDTO.setResource(pSource);

                int valid = validateInfo(interfaceDTO, json, Constants.SOURCE_PARTS);
                System.out.println("VALOR DE VALID: " + valid);
                if(valid == 0){
                    interfaceRepository.store(interfaceDTO);
                }else if (valid == 1){
                    interfaceDTO.setLastUpdate(currentTimestamp);
                    interfaceDTO.setUpdateBy("sistema de comparacion");
                    interfaceDTO.setOperation("UPDATE");
                    interfaceRepository.store(interfaceDTO);
                }else if (valid == 2){
                    System.out.println("COINCIDENCIA EN LA ENTRADA DE LOS DATOS");
                }
            }

        } catch (NullPointerException nullPointerException) {
            if(!data.isEmpty()){
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("230-IntParts");
                interfaceDTO.setErrorMessage(nullPointerException.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
            }
        } catch (Exception e) {
            if(!data.isEmpty()) {
                InterfaceDTO interfaceDTO = new InterfaceDTO();
                interfaceDTO.setCodError("220-IntParts");
                interfaceDTO.setErrorMessage(e.getMessage());
                interfaceDTO.setStatusProcess("E");
                interfaceRepository.store(interfaceDTO);
                e.printStackTrace();
            }
        }
    }


    @Override
    public void integrateInfo(String pSource, String codProv) {

        List<InterfaceDTO> interfaceDTOS = interfaceRepository.retrieve(pSource, "");
        switch (pSource) {
            case Constants.SOURCE_CUSTOMER:
                integrateInfoCustomer(codProv, interfaceDTOS);
                break;
            case Constants.SOURCE_PARTS:
                integrateInfoParts(codProv, interfaceDTOS);
                break;
            case Constants.SOURCE_VEHICLES:
                integrateInfoVehicles(codProv, interfaceDTOS);
                break;
        }

    }

    private void integrateInfoCustomer(String codProv, List<InterfaceDTO> interfaceDTOS) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        for (InterfaceDTO interfaceDTO : interfaceDTOS) {
            try {
                CustomerDTO c = gson.fromJson(interfaceDTO.getContJson(), CustomerDTO.class);
                int validateCustomerExist = validateExistCustomer(c.getDni());
                System.out.println("VALOR DE CUSTOMER EXISTE: " + validateCustomerExist);

                if(codProv.isEmpty() || codProv == null){
                    codProv = interfaceDTO.getCodProv();
                }

                if (validateCustomerExist == 0) {
                    String traducido = fncTranslate(c.getStreetType(),codProv);
                    c.setStreetType(traducido);
                    customerRepository.store(c);
                    interfaceDTO.setUpdateBy("system actualizador nuevo");
                } else {
                    interfaceDTO.setUpdateBy("system actualizador existente");
                }
                interfaceDTO.setLastUpdate(currentTimestamp);
                interfaceDTO.setCodError(null);
                interfaceDTO.setErrorMessage(null);
                interfaceDTO.setStatusProcess("P");
                interfaceRepository.update(interfaceDTO);
            } catch (Exception e) {
                InterfaceDTO errorInterfaceDTO = new InterfaceDTO();
                errorInterfaceDTO.setCodExternal(interfaceDTO.getCodExternal());
                errorInterfaceDTO.setUpdateBy("system");
                errorInterfaceDTO.setLastUpdate(currentTimestamp);
                errorInterfaceDTO.setCodError("240-Customer");
                errorInterfaceDTO.setErrorMessage(e.getMessage());
                errorInterfaceDTO.setStatusProcess("E");
                errorInterfaceDTO.setOperation("UPDATE");
                interfaceRepository.update(errorInterfaceDTO);
            }
        }

    }

    private void integrateInfoParts(String codProv, List<InterfaceDTO> interfaceDTOS) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        for (InterfaceDTO interfaceDTO : interfaceDTOS) {
            try {
                PartsDTO p = gson.fromJson(interfaceDTO.getContJson(), PartsDTO.class);
                int validatePartExist = validateExistPart(p.getDni(), p.getClaimNumber());
                System.out.println("VALOR DE PART EXISTE: " + validatePartExist);

                if (validatePartExist == 0) {
                    partRepository.store(p);
                    interfaceDTO.setUpdateBy("system actualizador nuevo");
                } else {
                    interfaceDTO.setUpdateBy("system actualizador existente");
                }
                interfaceDTO.setLastUpdate(currentTimestamp);
                interfaceDTO.setCodError(null);
                interfaceDTO.setErrorMessage(null);
                interfaceDTO.setStatusProcess("P");
                interfaceRepository.update(interfaceDTO);
            } catch (Exception e) {
                InterfaceDTO errorInterfaceDTO = new InterfaceDTO();
                errorInterfaceDTO.setCodExternal(interfaceDTO.getCodExternal());
                errorInterfaceDTO.setUpdateBy("system");
                errorInterfaceDTO.setLastUpdate(currentTimestamp);
                errorInterfaceDTO.setCodError("240-Parts");
                errorInterfaceDTO.setErrorMessage(e.getMessage());
                errorInterfaceDTO.setStatusProcess("E");
                errorInterfaceDTO.setOperation("UPDATE");
                interfaceRepository.update(errorInterfaceDTO);
            }
        }
    }

    private void integrateInfoVehicles(String codProv, List<InterfaceDTO> interfaceDTOS) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy/MM/dd").create();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

        for (InterfaceDTO interfaceDTO : interfaceDTOS) {
            try {
                VehicleDTO v = gson.fromJson(interfaceDTO.getContJson(), VehicleDTO.class);
                int validateVehicleExist = validateExistVehicle(v.getPlate());
                System.out.println("VALOR DE VEHICLE EXISTE: " + validateVehicleExist);

                if (validateVehicleExist == 0) {
                    vehicleRepository.store(v);
                    interfaceDTO.setUpdateBy("system actualizador nuevo");
                } else {
                    interfaceDTO.setUpdateBy("system actualizador existente");
                }
                interfaceDTO.setLastUpdate(currentTimestamp);
                interfaceDTO.setCodError(null);
                interfaceDTO.setErrorMessage(null);
                interfaceDTO.setStatusProcess("P");
                interfaceRepository.update(interfaceDTO);
            } catch (Exception e) {
                InterfaceDTO errorInterfaceDTO = new InterfaceDTO();
                errorInterfaceDTO.setCodExternal(interfaceDTO.getCodExternal());
                errorInterfaceDTO.setUpdateBy("system");
                errorInterfaceDTO.setLastUpdate(currentTimestamp);
                errorInterfaceDTO.setCodError("240-Vehicle");
                errorInterfaceDTO.setErrorMessage(e.getMessage());
                errorInterfaceDTO.setStatusProcess("E");
                errorInterfaceDTO.setOperation("UPDATE");
                interfaceRepository.update(errorInterfaceDTO);
            }
        }
    }


    @Override
    public void genInvoiceFile(String codProv, String date) {
        List<ProvidersDTO> proveedor = providerRepository.retrieve("", "");

        for (ProvidersDTO provider: proveedor) {

            if (codProv.isEmpty() || codProv == null) {
                codProv = provider.getCodProv();
            }

            if (date.isEmpty() || date == null) {
                date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
            }

            String fileName = relativePath+pathOut+invoiceFile+codProv+"_"+date+invoiceExtension;
            File file = new File(fileName);

            List<InvoiceDTO> invoiceDTOS = invoiceRepository.searchList(codProv, date);
            
            try (FileWriter writer = new FileWriter(file, true)) {

                if (!invoiceDTOS.isEmpty()) {
                    writer.append("dni,nombre,ape1,ape2,model,plate,cost,invoice_number\n");

                }
                    for (InvoiceDTO invoice: invoiceDTOS) {
                        List<InvoicesDTO> invoices = invoicesRepository.retrieve(codProv, date);

                        writer.append(invoice.getDniCustomer());
                        writer.append(",");
                        writer.append(invoice.getNameCustomer());
                        writer.append(",");
                        writer.append(invoice.getFirstSurname());
                        writer.append(",");
                        writer.append(invoice.getSecondSurname());
                        writer.append(",");
                        writer.append(invoice.getModelVehicle());
                        writer.append(",");
                        writer.append(invoice.getPlateVehicle());
                        writer.append(",");
                        writer.append(String.valueOf(invoice.getCost()));
                        writer.append(",");
                        writer.append(invoice.getInvoiceNumber());
                        writer.append("\n");
                        writer.flush();

                        for (InvoicesDTO invoicesDTO: invoices) {
                            if (invoicesDTO.getInvoiceNumber().equals(invoice.getInvoiceNumber())){
                                invoicesRepository.update(invoicesDTO);
                            }
                        }
                    }

                    System.out.println("Archivo " + fileName + " creado.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int validateExistCustomer(String dni) {
        CustomerDTO customerDTO = customerRepository.search(dni, "", "");
        if(customerDTO == null){
            return 0;
        }else {
            return 1;
        }
    }

    private int validateExistPart(String dni, String claimNumber) {
        PartsDTO partsDTO = partRepository.search(dni, claimNumber, "");
        if(partsDTO == null){
            return 0;
        }else {
            return 1;
        }
    }

    private int validateExistVehicle(String plate) {
        VehicleDTO vehicleDTO = vehicleRepository.search(plate, "", "");
        if (vehicleDTO == null){
            return 0;
        }else {
            return 1;
        }
    }

    private int validateInfo(InterfaceDTO interfaceDTO, String json, String pSource) {

        InterfaceDTO interfaceData = interfaceRepository.search(interfaceDTO.getCodExternal(), interfaceDTO.getCodProv(), pSource);

        if(interfaceData == null){
            return 0;
        }else if (!interfaceData.getContJson().equals(json)){
            return 1;
        }else {
            return 2;
        }
    }

    private String fncTranslate(String streetType, String codProv) {

        TranslationDTO translationDTO = translationRepository.search(codProv, streetType, "");
        streetType = translationDTO.getInternalCode();

        return streetType;

    }

    private String getNameFile(String pSource, String codProv, String date, String codProvConsulta) {

        String[] fechaActual = LocalDate.now().toString().split("-");
        String typeOfFile;

        if (pSource.equals("CUSTOMERS")){
            typeOfFile = customerFile;
        }else if (pSource.equals("PARTS")){
            typeOfFile = partsFile;
        }else {
            typeOfFile = vehicleFile;
        }

        if ( date != null ) {
            if (codProv.isEmpty() || codProv == null) {
                return relativePath+pathIn+typeOfFile+codProvConsulta+"_"+date+extension;
            }else{
                return relativePath+pathIn+typeOfFile+codProv+"_"+date+extension;
            }
        }

        if (codProv == null){
            return relativePath+pathIn+typeOfFile+codProvConsulta+"_"+fechaActual[0]+fechaActual[1]+fechaActual[2]+extension;
        }else{
            return relativePath+pathIn+typeOfFile+codProv+"_"+fechaActual[0]+fechaActual[1]+fechaActual[2]+extension;
        }

    }

}
