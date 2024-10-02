package com.ccsw.TutorialEntregable.prestamo.model;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.game.model.Game;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // Relación con la entidad Game
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    // Relación con la entidad Client
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    // Guardar las fechas como LocalDate
    @Column(name = "ini_date", nullable = false)
    private LocalDate iniDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    // Getters y setters

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDate getIniDate() {
        return this.iniDate;
    }

    public void setIniDate(LocalDate iniDate) {
        this.iniDate = iniDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
