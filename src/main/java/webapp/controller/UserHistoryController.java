package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import webapp.datastores.GameDatastore;
import webapp.model.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gautam on 2018-08-14.
 */

@RestController
public class UserHistoryController {

    @Autowired
    GameDatastore datastore;

    @PostMapping("/api/history")
    public ResponseEntity<?> getAllocation(
            @Valid @RequestBody HistoryRequest request, Errors errors) {
        //If error, just return a 400 bad request, along with the error message

        HistoryResponseBody result = new HistoryResponseBody();
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        else
        {
            String gameId = request.getGameId();
            gameId = gameId.replace("\"", "");
            String userId = request.getUserId();
            userId = userId.replace("\"", "");
            int round = request.getRound();

            Game game = datastore.getGame(gameId);
            if(game == null) {
                result.setMsg("Game doesn't exist");
                //figure out what to do with all these later
            }
            List<UserRoundHistory> list = new ArrayList<>();
            for(int i = 1; i< round; i++) {
                UserRoundHistory hist = new UserRoundHistory();
                int allocation = game.getAllocation(userId, i);
                Bid bid = game.getBid(userId, i);
                List<Integer> bids = new ArrayList<>();
                bids.add(Integer.parseInt(bid.getBid1()));
                bids.add(Integer.parseInt(bid.getBid2()));
                bids.add(Integer.parseInt(bid.getBid3()));
                hist.setAllocation(allocation);
                hist.setPrice(0);
                hist.setBids(bids);
                list.add(hist);
            }
            result.setList(list);
        }


        return ResponseEntity.ok(result);

    }


}
