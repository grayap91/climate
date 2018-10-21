package webapp.datastores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import webapp.model.Game;
import webapp.model.Player;
import webapp.model.PlayerType;
import webapp.model.ValueProfile;
import webapp.util.Util;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * Created by Gautam on 2018-05-23.
 * Not actually a web controller, just a persistent bean that handles registration
 */
@Component
public class GameDatastore {

    public static final int numPlayerLimit = 5  ;

    public static final String gamePrefix = "game";

    public static final String robotPrefix = "robot";

    private int numGamesCounter = 1;

    private int robotIndex = 1;

    public synchronized void increment() {
        //need to figure out how this works exactly
        numGamesCounter++;
    }

    Map<String, Boolean> gameStartMap = new ConcurrentHashMap<>();

    Map<Player, Game> player2Game = new HashMap<>();

    Map<String, Game> gameMap = new HashMap<>();
    //everytime a new game is launched we need an object that can watch this game
    // and trigger allocations after rounds are complete


    public Set<Player> getPlayers() {
        return player2Game.keySet();
    }

    public boolean containsPlayer(String username, PlayerType playerType)
    {
        return getPlayers().contains(new Player(username, playerType));
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
            game.addPlayer(new Player(generateRobotUsername(), PlayerType.MBS));
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
            boolean added = game.addPlayer(player);
            if(!added)
            {
                //create a new game
                numGamesCounter++;
                gameId = createGameId(numGamesCounter);
                game = new Game(gameId);
                added = game.addPlayer(player);
                player2Game.put(player, new Game(gameId));
                gameMap.put(gameId, game);
            }
        }
        else
        {
            //should only be hit the first time
            gameId = createGameId(numGamesCounter);
            Game game = new Game(gameId);
            boolean added = game.addPlayer(player);
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

    public Set<Player> getPlayersInGame(String gameId)
    {
        return gameMap.get(gameId).getPlayerList();
    }

    public Game getGame(String gameId)
    {
        return gameMap.get(gameId);
    }


    public void startGame(String gameId)
    {
        if(gameStartMap.containsKey(gameId))
        {
            return;
        }
        else
        {
            gameStartMap.put(gameId, true);
            //maybe clean up somehow?
            Thread thread = new Thread(gameMap.get(gameId));
            thread.start();
        }


    }


}
