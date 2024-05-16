package com.dam.tfg.MotoMammiApplicationNEGO.services;

import org.springframework.stereotype.Service;

@Service
public interface ProcesService {

    public void readFileInfo(String pSource, String codProv, String date);//pSource = origen/fuente de los datos
    public void integrateInfo(String pSource, String codProv);
}
