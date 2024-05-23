package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.VehicleDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleRepository implements ObjectRepository<VehicleDTO> {
    @Override
    public void storeList(List<VehicleDTO> t) {

    }

    @Override
    public void store(VehicleDTO vehicleDTO) {

        CustomerRepository customerRepository = new CustomerRepository();

        if (customerRepository.searchPlate(vehicleDTO.getPlate())) {
            throw new IllegalArgumentException("La placa no existe");
        }

        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();
            session.save(vehicleDTO);
            session.getTransaction().commit();
            System.out.println("GUARDADO EN LA TABLA VEHICLE");
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
    public List<VehicleDTO> retrieve(String pSource, String date) {
        return null;
    }

    @Override
    public VehicleDTO search(String plate, String codProv, String pSource) {
        ConfigDB.buildSessionFactory();
        List<VehicleDTO> vehicle = (List<VehicleDTO>) ConfigDB.getCurrentSession()
                .createQuery("from VehicleDTO where plate = :plate")
                .setParameter("plate", plate).list();
        if (vehicle.isEmpty()) {
            return null;
        } else {
            return vehicle.get(0); // Return the first matching result
        }
    }

    @Override
    public List<VehicleDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public VehicleDTO delete(int id) {
        return null;
    }

    @Override
    public VehicleDTO update(VehicleDTO vehicleDTO) {
        return null;
    }
}
