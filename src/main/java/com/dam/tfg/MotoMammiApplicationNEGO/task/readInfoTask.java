package com.dam.tfg.MotoMammiApplicationNEGO.task;
import com.dam.tfg.MotoMammiApplicationNEGO.services.ProcesService;
import com.dam.tfg.MotoMammiApplicationNEGO.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

@Component
public class readInfoTask {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private ProcesService procesService;
    @Scheduled(cron = "${cron.task.getCustomer}")
    public void taskCustomer() {
       try {
//           procesService.readFileInfo(Constants.SOURCE_CUSTOMER, null, null);
       }catch (Exception e){
           System.out.println(e.getMessage());
       }
    }
}
