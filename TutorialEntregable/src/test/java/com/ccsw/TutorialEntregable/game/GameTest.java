package com.ccsw.TutorialEntregable.game;

import com.ccsw.TutorialEntregable.game.model.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GameTest {

    public static final Long EXISTS_GAME_ID = 1L;
    public static final Long NOT_EXISTS_GAME_ID = 0L;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    @Test
    public void getExistsAuthorIdShouldReturnAuthor() {

        Game game = mock(Game.class);
        when(game.getId()).thenReturn(EXISTS_GAME_ID);
        when(gameRepository.findById(EXISTS_GAME_ID)).thenReturn(Optional.of(game));

        Game gameResponse = gameService.get(EXISTS_GAME_ID);

        assertNotNull(gameResponse);

        assertEquals(EXISTS_GAME_ID, gameResponse.getId());
    }

    @Test
    public void getNotExistsAuthorIdShouldReturnNull() {

        when(gameRepository.findById(NOT_EXISTS_GAME_ID)).thenReturn(Optional.empty());

        Game game = gameService.get(NOT_EXISTS_GAME_ID);

        assertNull(game);
    }

}