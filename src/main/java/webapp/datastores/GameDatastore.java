package webapp.datastores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import webapp.model.Game;
import webapp.model.Player;
import webapp.model.PlayerType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Gautam on 2018-05-23.
 * Not actually a web controller, just a persistent bean that handles registration
 */
@Component
public class GameDatastore {

    public static final int numPlayerLimit = 2  ;

    public static final String gamePrefix = "game";

    public static final String robotPrefix = "robot";

    private int numGamesCounter = 1;

    private int robotIndex = 1;

    public synchronized void increment() {
        //need to figure out how this works exactly
        numGamesCounter++;
    }

    Map<Player, Game> player2Game = new HashMap<>();

    Map<String, Game> gameMap = new HashMap<>();

    public Set<Player> getPlayers() {
        return player2Game.keySet();
    }


    public boolean containsPlayer(String username)
    {
        return getPlayers().contains(new Player(username));
    }

    public boolean addRobotPlayer2Game(String gameId)
    {
        //add player to specified game
        //game must exist before
        if(!gameMap.containsKey(gameId))
        {
            return false;
        }
        else
        {
            Game game = gameMap.get(gameId);
            //game must also have empty slots
            if(game.isReady())
            {
                return false;
            }
            game.getPlayerList().add(new Player(generateRobotUsername(), PlayerType.MBS));
            return true;
        }
    }

    private String generateRobotUsername()
    {
        String out =  robotPrefix+Integer.toString(robotIndex);
        robotIndex++;
        return out;
    }

    public String addPlayer(String username, PlayerType playerType)
    {
        String gameId = createGameId(numGamesCounter);
        Player player = new Player(username, playerType);
        if(gameMap.containsKey(gameId))
        {
            Game game = gameMap.get(gameId);
            Set<Player> players = game.getPlayerList();
            if(players.size() < numPlayerLimit)
            {
                //add player

                players.add(player);
                player2Game.put(player, new Game(gameId));
            }
            else
            {
                //create a new game
                numGamesCounter++;
                gameId = createGameId(numGamesCounter);
                players = new HashSet<>();
                players.add(player);
                game = new Game(gameId);
                game.setPlayerList(players);
                player2Game.put(player, new Game(gameId));
                gameMap.put(gameId, game);
            }
        }
        else
        {
            //should only be hit the first time
            gameId = createGameId(numGamesCounter);
            Set<Player> players = new HashSet<>();
            players.add(player);
            Game game = new Game(gameId);
            game.setPlayerList(players);
            gameMap.put(gameId, game);
            player2Game.put(player, new Game(gameId));
            //add a game with current counter
        }
        //logic to actually add a player to a game
        return gameId;
    }

    private String createGameId(int num)
    {
        return gamePrefix+num;
    }

    public boolean isGameFull(String gameId)
    {
        return gameMap.get(gameId).isReady();
    }

}
