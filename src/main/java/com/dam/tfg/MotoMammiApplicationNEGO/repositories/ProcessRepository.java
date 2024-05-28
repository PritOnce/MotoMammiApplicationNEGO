package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.PartsDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.ProcessDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProcessRepository implements ObjectRepository<ProcessDTO> {
    @Override
    public void storeList(List<ProcessDTO> t) {

    }

    @Override
    public void store(ProcessDTO processDTO) {

    }

    @Override
    public List<ProcessDTO> retrieve(String pSource, String date) {
        return null;
    }

    @Override
    public ProcessDTO search(String processName, String codProv, String pSource) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            List<ProcessDTO> processDTOS = session.createQuery("from ProcessDTO where process_name = :process_name and status = 1")
                    .setParameter("process_name", processName)
                    .list();

            session.getTransaction().commit();

            if (processDTOS.isEmpty()) {
                return null;
            } else {
                return processDTOS.get(0); // Return the first matching result
            }
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al buscar en la base de datos en la  tabla PROCESS");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    @Override
    public List<ProcessDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public ProcessDTO delete(int id) {
        return null;
    }

    @Override
    public ProcessDTO update(ProcessDTO processDTO) {
        return null;
    }
}
