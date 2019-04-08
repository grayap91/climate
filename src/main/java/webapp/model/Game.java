package webapp.model;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.commons.lang3.tuple.Pair;
import webapp.controller.PlayerPresentException;
import webapp.util.Util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

/**
 * Created by Gautam on 2018-05-23.
 */
public class Game implements Runnable {

    public static final int numRounds = 10;

    public static final int numGoodsPerPeriod = 3;

    public static final int numPlayersMax = 5;

    private int round = 1;

    private boolean[] allocated = new boolean[numRounds];

    private List<OutputRow> outputRows = new ArrayList<>();

    public void setWriteDir(String writeDir) {
        this.writeDir = writeDir;
    }

    String writeDir;

    public Set<Player> getPlayerList() {
        return playerList;
    }

    public boolean addPlayer(Player player) throws PlayerPresentException
    {
        if(getPlayerList().contains(player))
        {
            throw new PlayerPresentException();
        }
        if(playerList.size() < numPlayersMax) {
            playerList.add(player);
            player2Value.put(player, Util.generateRandomValueProfile());
            return true;
        }
        return false;

    }

    public List<Integer> getValue(String userId)
    {
        Player player = getPlayerById(userId);
        if(player != null) {
            return player2Value.get(player);
        }
        else {
            return new ArrayList<Integer>();
        }
    }

    Map<Player, List<Integer>> player2Value = new HashMap<>();

    public int getTotalAllocation(String userId)
    {
        if(getPlayerById(userId)!= null)
        {
            return getTotalAllocation(getPlayerById(userId));
        }
        return 0;
    }

    private int getTotalAllocation(Player player)
    {
        return allocationHistory.getTotalAllocation(player);
    }

    public void setPlayerList(Set<Player> playerList) {
        this.playerList = playerList;
    }

    Set<Player> playerList = new HashSet<>();

    BiddingHistory biddingHistory = new BiddingHistory();

    AllocationHistory allocationHistory = new AllocationHistory();

    public Game(String gameId)
    {
        this.gameId = gameId;
        for(boolean b : allocated)
        {
            b = false;
        }
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    String gameId;

    public boolean isReady()
    {
        return playerList.size() == numPlayersMax;
    }

    public Player getPlayerById(String userId)
    {
        for(Player player : playerList)
        {
            if(player.getUserId().equals(userId))
            {
                return player;
            }
        }
        return null;
    }

    public boolean allBidsIn(int round)
    {
        Map map = biddingHistory.getBidMap(round);
        if(map == null)
        {
            return false;
        }
        boolean value = true;
        for(Player player : getPlayerList())
        {
            value = value && map.containsKey(player);
        }
        return value;
    }

    public boolean allHumanBidsIn(int round)
    {
        Map map = biddingHistory.getBidMap(round);
        if(map == null)
        {
            return false;
        }
        boolean value = true;
        for(Player player : getPlayerList())
        {
            if(player.playerType == PlayerType.HUMAN) {
                value = value && map.containsKey(player);
            }
        }
        return value;
    }

    public void processBid(String userId, Bid bid)
    {
        Player player = getPlayerById(userId);
        processBid(player, bid);

    }

    public void processBid(Player player, Bid bid)
    {
        if(playerList.contains(player))
        {
            biddingHistory.addBid(player, bid);
        }

    }

    public int getPrices(int round)
    {
        return biddingHistory.getPrice(round);

    }

    private void submitRobotBids(int round)
    {
        for(Player player : playerList)
        {
            if(player.playerType == PlayerType.MBS)
            {
                //
                Bid bid = new Bid();
                bid.setGameId(gameId);
                bid.setRound(round);
                bid.setUserId(player.getUserId());
                bid.setBid1(Integer.toString(20));
                bid.setBid2(Integer.toString(20));
                bid.setBid3(Integer.toString(20));
                processBid(player, bid);
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        return gameId != null ? gameId.equals(game.gameId) : game.gameId == null;
    }

    @Override
    public int hashCode() {
        return gameId != null ? gameId.hashCode() : 0;
    }

    @Override
    public void run() {
        //error handling to check that the game is in fact ready
        while(round <= numRounds)
        {
            //check if all bids are here
            if(allHumanBidsIn(round) && !allocated[round-1])
            {
                submitRobotBids(round);
                //if bids are here then allocate
                //only do this once
                allocate();
                allocated[round-1] = true;
                updateOutput(round);

            }
            else {
                continue;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            round++;

        }
        long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
        writeRowLevelData(number);
        writeValueData(number);
        //what do here?
    }

    private void writeValueData(long number)
    {
        List<String> out = new ArrayList<>();
        String filename = writeDir+number+"_values.csv";
        for (Player player : playerList)
        {
            StringBuilder sb  = new StringBuilder();
            sb.append(player.getUserId());
            List<Integer> values = player2Value.get(player);
            for( int val : values)
            {
                sb.append(","+Integer.toString(val));
            }
            sb.append('\n');
            out.add(sb.toString());
        }
        try {
            Writer writer = new FileWriter(filename);
            for(String s :out) {
                writer.write(s);
            }
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    private void writeRowLevelData(long number)
    {
        String filename = writeDir+number+"_rows.csv";

        try {
            Writer writer = new FileWriter(filename);
            StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).build();
            beanToCsv.write(outputRows);
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void updateOutput(int round)
    {
        for(Player player : playerList)
        {
            OutputRow row = new OutputRow();
            row.setUserId(player.username);
            row.setRound(round);
            int allocation = allocationHistory.getAllocationForPlayer(player, round);
            row.setAllocation(allocation);
            int totalAllocationPrior = allocationHistory.getTotalAllocation(player) - allocation;
            int[] values = player2Value.get(player).subList(totalAllocationPrior,totalAllocationPrior+3).stream().mapToInt(i->i).toArray();
            row.setValue1(Integer.toString(values[0]));
            row.setValue1(Integer.toString(values[0]));
            row.setValue1(Integer.toString(values[0]));
            Bid bid = biddingHistory.getBid(player, round);
            row.setBid1(bid.getBid1());
            row.setBid2(bid.getBid2());
            row.setBid3(bid.getBid3());
            row.setPrice(biddingHistory.getPrice(round));
            outputRows.add(row);
        }

    }

    private void allocate()
    {
        for(Player player : playerList)
        {
            allocationHistory.initializeAllocationForPlayer(player, round);
        }
        //this assumes the bidding history for every player is in
        Map<Player, Bid> map = biddingHistory.getBidMap(round);
        List<Pair<Player, Integer>> list = transformMap(map);
        biddingHistory.addPrice(round, getPrice(list));
        for(int i = 0; i<= numGoodsPerPeriod-1; i++)
        {
            Player player = list.get(i).getLeft();
            allocationHistory.incrementAllocationForPlayer(player, round);
        }
    }

    private int getPrice(List<Pair<Player, Integer>> list)
    {
        //no need to use the index in this implementation
        return list.get(numGoodsPerPeriod).getRight();
    }

    private List<Pair<Player, Integer>> transformMap(Map<Player, Bid> map)
    {
        List<Pair<Player, Integer>> list = new ArrayList<>();
        for(Map.Entry<Player, Bid> entry : map.entrySet())
        {
            Bid bid = entry.getValue();
            Pair<Player, Integer> p1 = Pair.of(entry.getKey(), bid.getBid1Num());
            Pair<Player, Integer> p2 = Pair.of(entry.getKey(), bid.getBid2Num());
            Pair<Player, Integer> p3 = Pair.of(entry.getKey(), bid.getBid3Num());
            list.add(p1);
            list.add(p2);
            list.add(p3);
        }
        Collections.sort(list, new Comparator<Pair<Player, Integer>>() {
            @Override
            public int compare(Pair<Player, Integer> o1, Pair<Player, Integer> o2) {
                if(o1.getRight() > o2.getRight())
                {
                    return -1;

                }
                else if(o1.getRight() == o2.getRight())
                {
                    return 0;
                }
                else {
                    return 1;
                }
            }
        });

        System.out.println(list);
        //this sorting part might still be inaccurate
        return list;
    }

    public int getAllocation(String userId, int round)
    {
        return allocationHistory.getAllocationForPlayer(getPlayerById(userId), round);
    }

    public Bid getBid(String userId, int round)
    {
        return biddingHistory.getBid(getPlayerById(userId), round);
    }
}
