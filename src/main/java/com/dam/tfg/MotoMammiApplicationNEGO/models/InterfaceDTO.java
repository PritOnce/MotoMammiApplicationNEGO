package com.dam.tfg.MotoMammiApplicationNEGO.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "mm_interface")
public class InterfaceDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "codExternal")
    private String codExternal;

    @Column(name = "codProv")
    private String codProv;

    @Lob
    @Column(name = "contJson")
    private String contJson;

    @Column(name = "creationDate")
    private Timestamp creationDate;

    @Column(name = "lastUpdate")
    private Timestamp lastUpdate;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "updateBy")
    private String updateBy;

    @Column(name = "codError")
    private String codError;

    @Lob
    @Column(name = "errorMessage")
    private String errorMessage;

    @Column(name = "statusProccess", columnDefinition = "varchar(10) default 'N'")
    private String statusProcess;

    @Column(name = "operation")
    private String operation;

    @Column(name = "resource")
    private String resource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodExternal() {
        return codExternal;
    }

    public void setCodExternal(String codExternal) {
        this.codExternal = codExternal;
    }

    public String getCodProv() {
        return codProv;
    }

    public void setCodProv(String codProv) {
        this.codProv = codProv;
    }

    public String getContJson() {
        return contJson;
    }

    public void setContJson(String contJson) {
        this.contJson = contJson;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getCodError() {
        return codError;
    }

    public void setCodError(String codError) {
        this.codError = codError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusProcess() {
        return statusProcess;
    }

    public void setStatusProcess(String statusProcess) {
        this.statusProcess = statusProcess;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
