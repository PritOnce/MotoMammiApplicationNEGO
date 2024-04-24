package com.dam.tfg.MotoMammiApplicationNEGO.models;

import jakarta.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mm_providers")
public class ProvidersDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "codProv")
    private String codProv;
    @Column(name = "name")
    private String name;
    @Column(name = "dateIni")
    private Date dateIni;
    @Column(name = "dateEnd")
    private Date dateEnd;
    @Column(name = "SwiAct")
    private boolean SwiAct;

    // Constructor vacío requerido por JPA
    public ProvidersDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodProv() {
        return codProv;
    }

    public void setCodProv(String codProv) {
        this.codProv = codProv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateIni() {
        return dateIni;
    }

    public void setDateIni(Date dateIni) {
        this.dateIni = dateIni;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public boolean isSwiAct() {
        return SwiAct;
    }

    public void setSwiAct(boolean swiAct) {
        SwiAct = swiAct;
    }
}