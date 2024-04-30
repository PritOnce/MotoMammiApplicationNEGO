package com.dam.tfg.MotoMammiApplicationNEGO.models;

import javax.persistence.*;
import java.security.Timestamp;

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

    // Getters and setters
}
