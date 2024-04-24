package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.ProvidersDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class ProviderRepository implements ObjectRepository{

    @Override
    public void store(Object o) {

    }

    @Override
    public List<ProvidersDTO> retrieve() {
        ConfigDB.buildSessionFactory();
        List<ProvidersDTO> proveedores = (List<ProvidersDTO>) ConfigDB.getCurrentSession()
                .createQuery("from ProvidersDTO where SwiAct = true " +
                        "and ifnull(:pDate,current_date()) between dateIni and dateEnd")
                .setParameter("pDate", null).list();
        System.out.println("CONSULTA POR NOMBRE");
        return proveedores;
    }

    @Override
    public Object search(String name) {
        return null;
    }

    @Override
    public Object delete(int id) {
        return null;
    }
}
