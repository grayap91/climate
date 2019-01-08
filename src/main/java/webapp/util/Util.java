package webapp.util;

import webapp.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/**
 * Created by Gautam on 2018-05-28.
 */
public class Util {

    public static final String getId4Name(String username)
    {
        //encrypt decrypt here
        return username;
    }

    public static List<Integer> generateRandomValueProfile_2()
    {
        //decreasing marginal value
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        int num = random.nextInt(1000)+1;
        list.add(num);
        for(int i=1; i<=30; i++)
        {
            num = random.nextInt(num)+1;
            list.add(num);
        }
        return list;
        //generate a random number

    }

    public static List<Integer> generateRandomValueProfile()
    {
        //decreasing marginal value
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        int num = random.nextInt(1000)+1;
        list.add(num);
        for(int i=1; i<=30; i++)
        {
            num = random.nextInt(1000)+1;
            list.add(num);
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
        //generate a random number

    }


}
