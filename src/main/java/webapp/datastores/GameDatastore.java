package webapp.datastores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import webapp.controller.PlayerPresentException;
import webapp.model.*;
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

    @Value("${write.dir}")
    private String writeLocation;

    @Value("${robot.ratio:0.8}")
    private double ratio;

    @Value("${robot.rate:0}")
    private double eps;

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
            try {
                ValueType valueType;
                if(robotIndex%3==0)
                {
                    valueType = ValueType.FLAT;
                }
                else if(robotIndex%3==1)
                {
                    valueType = ValueType.UNIF;
                }
                else
                {
                    valueType = ValueType.STEP;
                }
                Player player = new Player(generateRobotUsername(), PlayerType.MBS);
                player.setValueType(valueType);
                Random random = new Random();
                ratio = ratio + (random.nextDouble())*(1-ratio);
                player.setRatio(ratio);
                player.setRatioDeriv(eps);
                game.addPlayer(player);
            }
            catch (PlayerPresentException e)
            {
                e.printStackTrace();
                //should never happen
            }
            return true;
        }
    }

    private String generateRobotUsername()
    {
        String out =  robotPrefix+Long.toString(robotIndex);
        robotIndex++;
        return out;
    }

    public String addPlayer(String username, PlayerType playerType) throws PlayerPresentException
    {
        String gameId = createGameId(numGamesCounter);
        Player player = new Player(username, playerType);
        ValueType valueType;
        if(username.hashCode()%3==0)
        {
            valueType = ValueType.FLAT;
        }
        else if(username.hashCode()%3==1)
        {
            valueType = ValueType.UNIF;
        }
        else
        {
            valueType = ValueType.STEP;
        }
        player.setValueType(valueType);
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
                game.setWriteDir(writeLocation);
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
            game.setWriteDir(writeLocation);
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

    public boolean isGameFullofHumans(String gameId)
    {
        return gameMap.get(gameId).isFullofHumans();
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
