package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.CustomerDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.PartsDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PartRepository implements ObjectRepository<PartsDTO> {
    @Override
    public void storeList(List<PartsDTO> t) {

    }

    @Override
    public void store(PartsDTO partsDTO) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();
            session.save(partsDTO);
            session.getTransaction().commit();
            System.out.println("GUARDADO EN LA TABLA PART");
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al guardar en la base de datos");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<PartsDTO> retrieve() {
        return null;
    }

    @Override
    public PartsDTO search(String dni, String claimNumber, String pSource) {
        ConfigDB.buildSessionFactory();
        List<PartsDTO> partsDTOS = (List<PartsDTO>) ConfigDB.getCurrentSession()
                .createQuery("from PartsDTO where dni = :dni and claim_number = :claim_number")
                .setParameter("dni", dni)
                .setParameter("claim_number", claimNumber).list();
        if (partsDTOS.isEmpty()) {
            return null;
        } else {
            return partsDTOS.get(0); // Return the first matching result
        }
    }

    @Override
    public List<PartsDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public PartsDTO delete(int id) {
        return null;
    }

    @Override
    public PartsDTO update(PartsDTO partsDTO) {
        return null;
    }
}
