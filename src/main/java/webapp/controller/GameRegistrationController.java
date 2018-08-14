package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

/**
 * Created by Gautam on 2018-08-05.
 */
@RestController
public class GameRegistrationController {

    @Autowired
    GameDatastore datastore;

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
            while(true) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (datastore.isGameFull(gameId)) {
                    break;
                } else {
                    datastore.addRobotPlayer2Game(gameId);
                    //wait x amount of time and then add in a new automatic player
                }
            }
            datastore.startGame(gameId);
            //have to only start the game once
        }


        return ResponseEntity.ok(result);

    }
}
