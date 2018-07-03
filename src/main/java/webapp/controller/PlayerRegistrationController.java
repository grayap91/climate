package webapp.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import webapp.datastores.GameRegisterActor;
import webapp.model.PlayerRegistrationResponseBody;

import javax.validation.Valid;
import java.util.stream.Collectors;

/**
 * Created by Gautam on 2018-05-23.
 */
@RestController
public class PlayerRegistrationController {

    @Autowired
    GameRegisterActor players;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUserInGame(@Valid @RequestBody String username, Errors errors)
    {
        PlayerRegistrationResponseBody result = new PlayerRegistrationResponseBody();
        if (errors.hasErrors()) {

            result.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(result);

        }
        if(StringUtils.isBlank(username))
        {
            // or appropriately long
            result.setRegistered(false);
            result.setMsg("Please select an appropriate username");
            return ResponseEntity.ok().body(result);
            //return bad
        }
        if (players.containsPlayer(username))
        {
            result.setRegistered(false);
            result.setMsg("This user is already registered");
            return ResponseEntity.ok().body(result);
            //no need to do anything further
        }
        else
        {
            String gameId = players.addPlayer(username);
            result.setGameId(gameId);
            result.setRegistered(true);
            result.setMsg(username+" registered");
            return ResponseEntity.ok().body(result);
        }
    }

}