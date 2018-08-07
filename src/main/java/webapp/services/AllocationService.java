package webapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webapp.datastores.GameDatastore;
import webapp.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllocationService {

    @Autowired
    GameDatastore gameDatastore;

    public int allocate() {
        return 3;
    }

    //need to update the bids as they come in and then allocate to appropriate players and price them


}