package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import webapp.datastores.GameDatastore;
import webapp.model.Bid;
import webapp.model.BidAcceptResponseBody;
import webapp.model.Game;
import webapp.model.GameRegistrationResponseBody;

import javax.validation.Valid;
import java.util.stream.Collectors;

import static webapp.datastores.GameDatastore.numPlayerLimit;

/**
 * Created by Gautam on 2018-08-05.
 */
@RestController
public class GameRegistrationController {

    @Autowired
    GameDatastore datastore;

    @Value("${waitTime:50000}")
    private int waitTime;



    @PostMapping("/api/game")
    public ResponseEntity<?> startGame(
            @Valid @RequestBody String gameId, Errors errors) {

        GameRegistrationResponseBody result = new GameRegistrationResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        else
        {
            gameId = gameId.replace("\"", "");
            //this is a hack , figure out why you're getting these extra quotes

                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (datastore.isGameFull(gameId)) {
                    datastore.startGame(gameId);
                } else {
                    int num = datastore.getPlayersInGame(gameId).size();
                    int players2add = numPlayerLimit - num;
                    while(players2add >0)
                    {
                        datastore.addRobotPlayer2Game(gameId);
                        players2add--;
                    }
                }


            //have to only start the game once
        }


        return ResponseEntity.ok(result);

    }
}
