package com.daw.cinemadaw.domain.cinema;

import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


@Entity
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nom del cinema és obligatori")
    @Size(min = 2, max = 100, message = "El nom del cinema ha de tenir entre 2 i 100 caràcters")
    @Column
    private String name;

    @NotBlank(message = "L'adreça del cinema és obligatòria")
    @Size(min = 2, max = 200, message = "L'adreça ha de tenir entre 2 i 200 caràcters")
    @Column
    private String address;

    @NotBlank(message = "La ciutat és obligatòria")
    @Size(min = 2, max = 100, message = "La ciutat ha de tenir entre 2 i 100 caràcters")
    @Column
    private String city;

    @NotBlank(message = "El codi postal és obligatori")
    @Pattern(regexp = "\\d{5}", message = "El codi postal ha de tenir exactament 5 dígits")
    @Column
    private String postalCode;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true )
    private List<Room> rooms = new ArrayList<>();

    public Cinema() {
    }

    
    public Cinema(String name, String address, String city, String postalCode) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    @Override
    public String toString() {
        return "Cinema [id=" + id + ", name=" + name + ", address=" + address + ", city=" + city + ", postalCode="
                + postalCode + "]";
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
    
}
