package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.CustomerDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.InvoiceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.InvoicesDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.VehicleDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoiceRepository implements ObjectRepository<InvoiceDTO> {

    @Override
    public void storeList(List<InvoiceDTO> t) {

    }

    @Override
    public void store(InvoiceDTO invoiceDTO) {

    }

    @Override
    public List<InvoiceDTO> retrieve(String pSource, String date) {
        return null;
    }

    @Override
    public InvoiceDTO search(String codProv, String date, String pSource) {
        InvoicesRepository invoicesRepository = new InvoicesRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();

        List<InvoicesDTO> invoicesDTO = invoicesRepository.retrieve(codProv, date);

        for (InvoicesDTO invoices : invoicesDTO) {
            CustomerDTO customerDTO = customerRepository.search(invoices.getDni(), codProv, "");
            VehicleDTO vehicleDTO = vehicleRepository.search(invoices.getPlate(), codProv, "");

            InvoiceDTO invoiceDTO = new InvoiceDTO();

            invoiceDTO.setDniCustomer(customerDTO.getDni());
            invoiceDTO.setNameCustomer(customerDTO.getName());
            invoiceDTO.setFirstSurname(customerDTO.getFirstSurname());
            invoiceDTO.setSecondSurname(customerDTO.getLastSurname());
            invoiceDTO.setModelVehicle(vehicleDTO.getModel());
            invoiceDTO.setPlateVehicle(vehicleDTO.getPlate());
            invoiceDTO.setIssue_date(invoices.getIssueDate());
            invoiceDTO.setCost(invoices.getCost());
            invoiceDTO.setInvoiceNumber(invoices.getInvoiceNumber());

            return invoiceDTO;
        }
        return null;

    }

    @Override
    public List<InvoiceDTO> searchList(String codProv, String date) {
        InvoicesRepository invoicesRepository = new InvoicesRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        VehicleRepository vehicleRepository = new VehicleRepository();

        List<InvoicesDTO> invoicesDTOList = invoicesRepository.retrieve(codProv, date);
        List<InvoiceDTO> invoiceDTOList = new ArrayList<>();

        for (InvoicesDTO invoices : invoicesDTOList) {
            CustomerDTO customerDTO = customerRepository.search(invoices.getDni(), codProv, "");
            VehicleDTO vehicleDTO = vehicleRepository.search(invoices.getPlate(), codProv, "");

            InvoiceDTO invoiceDTO = new InvoiceDTO();

            invoiceDTO.setDniCustomer(customerDTO.getDni());
            invoiceDTO.setNameCustomer(customerDTO.getName());
            invoiceDTO.setFirstSurname(customerDTO.getFirstSurname());
            invoiceDTO.setSecondSurname(customerDTO.getLastSurname());
            invoiceDTO.setModelVehicle(vehicleDTO.getModel());
            invoiceDTO.setPlateVehicle(vehicleDTO.getPlate());
            invoiceDTO.setIssue_date(invoices.getIssueDate());
            invoiceDTO.setCost(invoices.getCost());
            invoiceDTO.setInvoiceNumber(invoices.getInvoiceNumber());

            invoiceDTOList.add(invoiceDTO);
        }
        return invoiceDTOList;
    }

    @Override
    public InvoiceDTO delete(int id) {
        return null;
    }

    @Override
    public InvoiceDTO update(InvoiceDTO invoiceDTO) {
        return null;
    }
}
