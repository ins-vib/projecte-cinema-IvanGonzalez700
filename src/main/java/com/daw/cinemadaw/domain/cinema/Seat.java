package com.daw.cinemadaw.domain.cinema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


@Entity
public class Seat {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int number;

    @Column
    private String seatRow;

    @Column
    private int x;

    @Column
    private int y;

    @Column
    @Enumerated(EnumType.STRING)
    private SeatType type = SeatType.STANDARD;

    
    @Column
    private boolean active;

    @ManyToOne
    private Room room;

    public Seat() {
    }

    public Seat(int number, String row, int x, int y, SeatType type) {
        this.number = number;
        this.seatRow = row;
        this.x = x;
        this.y = y;
        this.type = type;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRow() {
        return seatRow;
    }

    public void setRow(String seatRow) {
    this.seatRow = seatRow;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public SeatType getType() {
        return type;
    }

    public void setType(SeatType type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return "Seat [number=" + number + ", row=" + seatRow + ", x=" + x + ", y=" + y + ", type=" + type + ", active="
                + active + "]";
    }

}
