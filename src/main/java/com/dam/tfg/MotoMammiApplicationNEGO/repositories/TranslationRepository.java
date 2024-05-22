package com.dam.tfg.MotoMammiApplicationNEGO.repositories;

import com.dam.tfg.MotoMammiApplicationNEGO.models.TranslationDTO;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.ConfigDB;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TranslationRepository implements ObjectRepository<TranslationDTO> {

    @Override
    public void storeList(List<TranslationDTO> t) {

    }

    @Override
    public void store(TranslationDTO translationDTO) {

    }

    @Override
    public List<TranslationDTO> retrieve() {
        return null;
    }

    @Override
    public TranslationDTO search(String codProv, String externalCode, String pSource) {
        ConfigDB.buildSessionFactory();
        List<TranslationDTO> customers = (List<TranslationDTO>) ConfigDB.getCurrentSession()
                .createQuery("from TranslationDTO where codProv = :codProv and externalCode = :externalCode " +
                        "and current_date() between dateIni and dateEnd")
                .setParameter("codProv", codProv)
                .setParameter("externalCode", externalCode)
                .list();
        if (customers.isEmpty()) {
            return null;
        } else {
            return customers.get(0); // Return the first matching result
        }
    }

    @Override
    public List<TranslationDTO> searchList(String codExternal, String codProv) {
        return null;
    }

    @Override
    public TranslationDTO delete(int id) {
        return null;
    }

    @Override
    public TranslationDTO update(TranslationDTO translationDTO) {
        return null;
    }
}
