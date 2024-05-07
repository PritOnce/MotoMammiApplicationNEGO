package com.dam.tfg.MotoMammiApplicationNEGO.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mm_customer")
public class CustomerDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "dni", nullable = false, unique = true)
    private String dni;

    @Column(name = "name")
    private String name;

    @Column(name = "first_surname")
    private String first_surname;

    @Column(name = "last_surname")
    private String last_surname;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private Date birth_date;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "street_type")
    private String street_type;

    @Column(name = "city")
    private String city;

    @Column(name = "number")
    private int number;

    @Column(name = "phone")
    private String phone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "license_type")
    private String license_type;

    @Column(name = "plate", unique = true)
    private String plate;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstSurname() {
        return first_surname;
    }

    public void setFirstSurname(String firstSurname) {
        this.first_surname = firstSurname;
    }

    public String getLastSurname() {
        return last_surname;
    }

    public void setLastSurname(String lastSurname) {
        this.last_surname = lastSurname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birth_date;
    }

    public void setBirthDate(Date birthDate) {
        this.birth_date = birthDate;
    }

    public String getPostalCode() {
        return postal_code;
    }

    public void setPostalCode(String postalCode) {
        this.postal_code = postalCode;
    }

    public String getStreetType() {
        return street_type;
    }

    public void setStreetType(String streetType) {
        this.street_type = streetType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLicenseType() {
        return license_type;
    }

    public void setLicenseType(String licenseType) {
        this.license_type = licenseType;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
