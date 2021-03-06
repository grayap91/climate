package webapp.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Gautam on 2018-08-14.
 */

@RestController
public class UserHistoryController {

    @Autowired
    GameDatastore datastore;

    @PostMapping("/api/users")
    public ResponseEntity<?> getUserList(
            @Valid @RequestBody String gameId, Errors errors) {
        if (errors.hasErrors()) {

            String msg = errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(","));

            return ResponseEntity.badRequest().body(msg);

        } else {
            gameId = gameId.replace("\"", "");
            List<Player> list = new ArrayList<>();
            for (Player player : datastore.getPlayersInGame(gameId))
            {
                list.add(player);
            }
            return ResponseEntity.ok(list);
        }
    }

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
            List<Integer> values = game.getValue(userId);
            int cumProfit = 0;
            int counter = 0;
            for(int i = 1; i< round; i++) {
                int cumVal = 0;
                UserRoundHistory hist = new UserRoundHistory();
                int allocation = game.getAllocation(userId, i);
                for(int k=counter;k<(counter+allocation);k++)
                {
                    cumVal+=values.get(k);
                }
                counter=counter+allocation;
                Bid bid = game.getBid(userId, i);
                List<Integer> bids = new ArrayList<>();
                bids.add(bid.getBid1Num());
                bids.add(bid.getBid2Num());
                bids.add(bid.getBid3Num());
                hist.setAllocation(allocation);
                int price = game.getPrices(i);
                hist.setPrice(price);
                hist.setBids(bids);
                list.add(hist);
                if(allocation >0) {
                    cumProfit += (cumVal - (allocation * price));
                }
                hist.setProfit(cumProfit);
                //profit up until here
            }
            result.setList(list);
        }


        return ResponseEntity.ok(result);

    }


}
