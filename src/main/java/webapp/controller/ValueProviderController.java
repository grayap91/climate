package webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    @PostMapping("/api/value")
    public ResponseEntity<?> startGame(
            @Valid @RequestBody String userId, Errors errors) {
        ValueProfile profile = new ValueProfile();

        if (errors.hasErrors()) {

            profile.setMsg(errors.getAllErrors()
                    .stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.joining(",")));

            return ResponseEntity.badRequest().body(profile);

        }
        else
        {
            List<Integer> list = Util.generateRandomValueProfile();
            profile.setValues(list);
            return ResponseEntity.ok(profile);
        }


    }
}
