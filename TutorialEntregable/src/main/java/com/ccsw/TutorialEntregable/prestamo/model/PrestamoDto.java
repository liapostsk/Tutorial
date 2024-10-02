package com.ccsw.TutorialEntregable.prestamo.model;

import com.ccsw.TutorialEntregable.client.model.ClientDto;
import com.ccsw.TutorialEntregable.game.model.GameDto;

import java.time.LocalDate;

/**
 * @author ccsw
 *
 */
public class PrestamoDto {

    private Long id;

    private GameDto game;

    private ClientDto client;

    private LocalDate iniDate;

    private LocalDate endDate;

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
    public LocalDate getIniDate() {

        return this.iniDate;
    }

    /**
     * @param iniDate new value of {@link #getIniDate}.
     */
    public void setIniDate(LocalDate iniDate) {

        this.iniDate = iniDate;
    }

    /**
     * @return endDate
     */
    public LocalDate getEndDate() {

        return this.endDate;
    }

    /**
     * @param endDate new value of {@link #getEndDate}.
     */
    public void setEndDate(LocalDate endDate) {

        this.endDate = endDate;
    }

}
