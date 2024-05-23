package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.CustomerDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.InterfaceDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.models.PartsDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomerRepository implements ObjectRepository<CustomerDTO> {
    @Override
    public void storeList(List<CustomerDTO> t) {

    }

    @Override
    public void store(CustomerDTO customerDTO) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();
            session.save(customerDTO);
            session.getTransaction().commit();
            System.out.println("GUARDADO EN LA TABLA CUSTOMER");
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
    public List<CustomerDTO> retrieve(String pSource, String date) {
        return null;
    }

    @Override
    public CustomerDTO search(String dni, String codProv, String pSource) {
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            List<CustomerDTO> customers = (List<CustomerDTO>) ConfigDB.getCurrentSession()
                    .createQuery("from CustomerDTO where dni = :dni")
                    .setParameter("dni", dni).list();

            session.getTransaction().commit();

            if (customers.isEmpty()) {
                return null;
            } else {
                return customers.get(0); // Return the first matching result
            }

        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al buscar en la base de datos en la  tabla CUSTOMER");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;

    }

    @Override
    public List<CustomerDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public CustomerDTO delete(int id) {
        return null;
    }

    @Override
    public CustomerDTO update(CustomerDTO customerDTO) {
        return null;
    }

    public boolean searchDNI(String dni){
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            List<CustomerDTO> customers = (List<CustomerDTO>) ConfigDB.getCurrentSession()
                    .createQuery("from CustomerDTO where dni = :dni")
                    .setParameter("dni", dni).list();

            session.getTransaction().commit();

            if(customers.isEmpty()){
                return true;
            }
            return false;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al buscar en la base de datos en la  tabla CUSTOMER");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }

    public boolean searchPlate(String plate){
        Session session = null;
        try {
            ConfigDB.buildSessionFactory();
            session = ConfigDB.getCurrentSession();
            session.beginTransaction();

            List<CustomerDTO> customers = (List<CustomerDTO>) ConfigDB.getCurrentSession()
                    .createQuery("from CustomerDTO where plate = :plate")
                    .setParameter("plate", plate).list();

            session.getTransaction().commit();

            if(customers.isEmpty()){
                return true;
            }
            return false;
        } catch (Exception e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.out.println("Error al buscar en la base de datos en la  tabla CUSTOMER");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return false;
    }
}
