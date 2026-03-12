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

    @NotBlank(message="el nom del cinema")
    @Size(min = 2, max = 100, message = "la ciutat")
    @Column
    private String name;

    @NotBlank(message="el nom del la adreca")
    @Size(min = 5, max = 150, message = "la ciutat")
    @Column
    private String address;

    @NotBlank(message="el nom del la ciutat")
    @Size(min = 2, max = 80, message = "la ciutat")
    @Column
    private String city;

    
    @Pattern(regexp = "\\d{5}", message = "el codi postal ha de tenir 5 digits")
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
