package com.ccsw.TutorialEntregable.prestamo.model;

import com.ccsw.TutorialEntregable.client.model.Client;
import com.ccsw.TutorialEntregable.game.model.Game;
import jakarta.persistence.*;

import java.util.Date;

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

    // Guardar las fechas como Date
    @Column(name = "ini_date", nullable = false)
    private Date iniDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

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

    public Date getIniDate() {
        return this.iniDate;
    }

    public void setIniDate(Date iniDate) {
        this.iniDate = iniDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
