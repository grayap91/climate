package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import webapp.datastores.GameDatastore;
import webapp.model.BidAcceptResponseBody;
import webapp.model.Bid;
import webapp.model.Game;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class BidAcceptController {

    @Autowired
    GameDatastore datastore;


    @PostMapping("/api/bids")
    public ResponseEntity<?> acceptBid(
            @Valid @RequestBody Bid bid, Errors errors) {

        BidAcceptResponseBody result = new BidAcceptResponseBody();

        //If error, just return a 400 bad request, along with the error message
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        else
        {
            result.setMsg("okay");
            Game game = datastore.getGame(bid.getGameId());
            if(game != null)
            {
                game.processBid(bid.getUserId(), bid);
                while(true)
                {
                    if(game.allBidsIn(bid.getRound()))
                    {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int allocation = game.getAllocation(bid.getUserId(), bid.getRound());
                        result.setNumAllocated(allocation);
                        break;
                    }
                }
            }
            //stores it
            //check if all human bids are complete then load robot bids?

        }


        return ResponseEntity.ok(result);

    }

}
