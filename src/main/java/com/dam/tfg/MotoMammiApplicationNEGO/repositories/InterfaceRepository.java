package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.InterfaceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.ProvidersDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import jakarta.persistence.EntityNotFoundException;
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
        ConfigDB.buildSessionFactory();
        List<InterfaceDTO> interfaceDTOS = (List<InterfaceDTO>) ConfigDB.getCurrentSession()
                .createQuery("from InterfaceDTO where statusProccess = 'N'").list();
        System.out.println("Consulta en Interface");
        return interfaceDTOS;
    }

    @Override
    public InterfaceDTO search(String codExternal, String codProv) {
        ConfigDB.buildSessionFactory();
        InterfaceDTO interfaceDTO = (InterfaceDTO) ConfigDB.getCurrentSession()
                .createQuery("from InterfaceDTO where codExternal = :codigoExterno " +
                        "and codProv = :codigoProveedor " +
                        "order by creationDate desc") // Suponiendo que tienes un campo fechaCreacion para ordenar
                .setParameter("codigoExterno", codExternal)
                .setParameter("codigoProveedor", codProv)
                .setMaxResults(1)
                .uniqueResult();
        return interfaceDTO;
    }


    @Override
    public List<InterfaceDTO> searchList(String codExternal, String codProv) {
        ConfigDB.buildSessionFactory();
        List<InterfaceDTO> interfaces = (List<InterfaceDTO>) ConfigDB.getCurrentSession()
                .createQuery("from InterfaceDTO where codExternal = :codigoExterno " +
                        "and codProv = :codigoProveedor")
                .setParameter("codigoExterno", codExternal).setParameter("codigoProveedor", codProv).list();
        return interfaces;
    }

    @Override
    public InterfaceDTO  delete(int id) {
        return null;
    }

    @Override
    public InterfaceDTO update(InterfaceDTO interfaceDTO) {
        Session session = null;
        try {
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            // Obtener la entidad desde la base de datos usando el ID
            InterfaceDTO existingEntity = (InterfaceDTO) session.get(InterfaceDTO.class, interfaceDTO.getId());

            // Verificar si la entidad existe
            if (existingEntity != null) {
                // Actualizar los atributos de la entidad con los valores de interfaceDTO
                existingEntity.setStatusProcess(interfaceDTO.getStatusProcess());
                existingEntity.setUpdateBy(interfaceDTO.getUpdateBy());
                existingEntity.setLastUpdate(interfaceDTO.getLastUpdate());
                session.update(existingEntity);
                session.getTransaction().commit();
                return existingEntity;
            } else {
                // Si la entidad no existe, lanzar una excepción
                throw new EntityNotFoundException("La entidad con ID " + interfaceDTO.getId() + " no fue encontrada.");
            }
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
