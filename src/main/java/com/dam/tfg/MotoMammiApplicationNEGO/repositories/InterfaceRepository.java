package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.InterfaceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public class InterfaceRepository implements ObjectRepository<InterfaceDTO> {

    @Transactional
    public void storeList(List<InterfaceDTO> interfaceDTOList) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();
            for (InterfaceDTO dataInterfaceDTO : interfaceDTOList) {
                session.save(dataInterfaceDTO);
            }
            session.getTransaction().commit();
            System.out.println("GUARDADO EN LA TABLA INTERFACE");
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al guardar en la base de datos");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
                ConfigDB.closeSessionFactory();
            }
        }
    }

    @Override
    public void store(InterfaceDTO interfaceDTO) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();
            session.save(interfaceDTO);
            session.getTransaction().commit();
            System.out.println("GUARDADO");
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
    public List<InterfaceDTO> retrieve() {
        return null;
    }

    @Override
    public InterfaceDTO  search(String name) {
        return null;
    }

    @Override
    public InterfaceDTO  delete(int id) {
        return null;
    }
}
