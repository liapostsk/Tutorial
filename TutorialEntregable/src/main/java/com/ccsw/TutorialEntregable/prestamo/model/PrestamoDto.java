package com.ccsw.TutorialEntregable.prestamo.model;

import com.ccsw.TutorialEntregable.client.model.ClientDto;
import com.ccsw.TutorialEntregable.game.model.GameDto;

import java.util.Date;

public class PrestamoDto {

    private Long id;

    private GameDto game;

    private ClientDto client;

    private Date iniDate;

    private Date endDate;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return game
     */
    public GameDto getGame() {

        return this.game;
    }

    /**
     * @param game new value of {@link #getGame()}.
     */
    public void setGame(GameDto game) {

        this.game = game;
    }

    /**
     * @return client
     */
    public ClientDto getClient() {

        return this.client;
    }

    /**
     * @param client new value of {@link #getClient()}.
     */
    public void setClient(ClientDto client) {

        this.client = client;
    }

    /**
     * @return iniDate
     */
    public Date getIniDate() {

        return this.iniDate;
    }

    /**
     * @param iniDate new value of {@link #getIniDate}.
     */
    public void setIniDate(Date iniDate) {

        this.iniDate = iniDate;
    }

    /**
     * @return endDate
     */
    public Date getEndDate() {

        return this.endDate;
    }

    /**
     * @param endDate new value of {@link #getEndDate}.
     */
    public void setEndDate(Date endDate) {

        this.endDate = endDate;
    }

}
