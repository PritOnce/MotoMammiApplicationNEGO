package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.ProvidersDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class ProviderRepository implements ObjectRepository<ProvidersDTO> {

    @Override
    public void storeList(List<ProvidersDTO> providersDTOList) {

    }

    public void store(ProvidersDTO providersDTO) {

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
    public ProvidersDTO search(String codExternal, String codProvs) {
        return null;
    }

    @Override
    public List<ProvidersDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public ProvidersDTO delete(int id) {
        return null;
    }

    @Override
    public ProvidersDTO update(ProvidersDTO providersDTO) {
        return null;
    }
}
