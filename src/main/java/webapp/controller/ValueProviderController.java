package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import webapp.datastores.GameDatastore;
import webapp.model.Game;
import webapp.model.HistoryRequest;
import webapp.model.ValueProfile;
import webapp.util.Util;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Gautam on 2018-08-30.
 */
@RestController
public class ValueProviderController {

    @Autowired
    GameDatastore gameDatastore;

    @PostMapping("/api/value")
    public ResponseEntity<?> startGame(
            @Valid @RequestBody HistoryRequest request, Errors errors) {
        ValueProfile profile = new ValueProfile();

        if (errors.hasErrors()) {

            profile.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(profile);

        }
        else
        {
            String userId = request.getUserId();
            String gameId = request.getGameId();
            int round = request.getRound();
            Game game = gameDatastore.getGame(gameId);
            int allocation = game.getTotalAllocation(userId);
            List<Integer> values = game.getValue(userId);
            List<Integer> requiredValues = values.subList(allocation, values.size());
            profile.setValues(requiredValues);
            return ResponseEntity.ok(profile);
        }
    }
}
