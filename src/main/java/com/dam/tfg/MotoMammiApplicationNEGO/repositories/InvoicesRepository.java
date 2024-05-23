package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.InvoicesDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InvoicesRepository implements ObjectRepository<InvoicesDTO> {
    @Override
    public void storeList(List<InvoicesDTO> t) {

    }

    @Override
    public void store(InvoicesDTO invoicesDTO) {

    }

    @Override
    public List<InvoicesDTO> retrieve(String pSource, String date) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4));

            List<Object[]> results = session.createQuery(
                            "SELECT c.dni, c.name, c.first_surname, c.last_surname, " +
                                    "v.model, v.plate, i.cost, i.invoiceNumber " +
                                    "FROM CustomerDTO c " +
                                    "JOIN VehiclesDTO v ON c.plate = v.plate " +
                                    "JOIN InvoicesDTO i ON v.plate = i.plateVehicle " +
                                    "WHERE YEAR(i.issue_date) = :year " +
                                    "AND MONTH(i.issue_date) = :month")
                    .setParameter("year", year)
                    .setParameter("month", month)
                    .list();

            List<InvoicesDTO> invoicesDTO = new ArrayList<>();
            for (Object[] result : results) {
                InvoicesDTO invoiceDTO = new InvoicesDTO();
                invoiceDTO.setDniCustomer((String) result[0]);
                invoiceDTO.setNameCustomer((String) result[1]);
                invoiceDTO.setFirstSurname((String) result[2]);
                invoiceDTO.setSecondSurname((String) result[3]);
                invoiceDTO.setModelVehicle((String) result[4]);
                invoiceDTO.setPlateVehicle((String) result[5]);
                invoiceDTO.setCost((Double) result[6]);
                invoiceDTO.setInvoiceNumber((String) result[7]);
                invoicesDTO.add(invoiceDTO);
            }

            session.getTransaction().commit();
            return invoicesDTO;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al buscar en la base de datos");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }



    @Override
    public InvoicesDTO search(String codExternal, String codProv, String pSource) {
        return null;
    }

    @Override
    public List<InvoicesDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public InvoicesDTO delete(int id) {
        return null;
    }

    @Override
    public InvoicesDTO update(InvoicesDTO invoicesDTO) {
        return null;
    }
}
