package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import webapp.model.BidAcceptResponseBody;
import webapp.model.Bid;
import webapp.services.AllocationService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
public class BidAcceptController {


    AllocationService allocationService;

    @Autowired
    public void setAllocationService(AllocationService allocationService) {
        this.allocationService = allocationService;
    }

    @PostMapping("/api/bids")
    public ResponseEntity<?> getSearchResultViaAjax(
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
            String gameId = bid.getGameId();
            String userId = bid.getUserId();
            //validate the 2 above and then figure out the allocation code
            //validate
            //all the magic has to happen here
            result.setNumAllocated(allocationService.allocate());
        }


        return ResponseEntity.ok(result);

    }

}
