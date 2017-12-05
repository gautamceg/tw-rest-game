package com.example.demo.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gargg on 25/10/17.
 */
@RestController
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping("/game")
    public String palyGame(){
        return gameService.playGame();
    }
}
