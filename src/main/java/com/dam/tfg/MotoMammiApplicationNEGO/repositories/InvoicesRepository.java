package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.InterfaceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.InvoicesDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

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
    public List<InvoicesDTO> retrieve(String codProv, String date) {
        Session session = null;
        try {

            int year = Integer.parseInt(date.substring(0, 4));
            int month = Integer.parseInt(date.substring(4));

            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            List<InvoicesDTO> invoices = (List<InvoicesDTO>) ConfigDB.getCurrentSession()
                    .createQuery("from InvoicesDTO where codProv = :codProv and YEAR(issue_date) = :year and MONTH(issue_date) = :month and send = false")
                    .setParameter("codProv", codProv)
                    .setParameter("year", year)
                    .setParameter("month", month)
                    .list();

            session.getTransaction().commit();
            return invoices;

        }catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al buscar en la base de datos en la  tabla INVOICES");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;

    }

    @Override
    public InvoicesDTO search(String date, String codProv, String pSource) {
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
        Session session = null;
        try {
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            // Obtener la entidad desde la base de datos usando el ID
            List<InvoicesDTO> results = session.createQuery("FROM InvoicesDTO WHERE invoice_number = :invoice_number")
                    .setParameter("invoice_number", invoicesDTO.getInvoiceNumber())
                    .list();

            if (results.isEmpty()) {
                // Si no se encuentra la entidad, lanzar una excepción
                throw new EntityNotFoundException("La entidad con ID " + invoicesDTO.getInvoiceNumber() + " no fue encontrada.");
            }

            // Obtener la entidad existente
            InvoicesDTO existingEntity = results.get(0);

            // Actualizar los atributos de la entidad con los valores de interfaceDTO
            existingEntity.setSend(true);

            session.update(existingEntity);
            session.getTransaction().commit();
            return existingEntity;
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al actualizar en la base de datos");
            e.printStackTrace();
            // Manejar o relanzar la excepción según corresponda en tu aplicación
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
