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

    private static List<Integer> uniformRandom()
    {
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for(int i=1; i<=10; i++)
        {
            int num = random.nextInt(100)+1;
            list.add(num);
        }

        for(int i=1; i<=20; i++)
        {
            list.add(0);
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
        //generate a random number

    }

    private static List<Integer> uniformRandom2()
    {
        //uniform between 60 and 80
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for(int i=1; i<=10; i++)
        {
            int num = random.nextInt(20)+60;
            list.add(num);
        }

        for(int i=1; i<=20; i++)
        {
            list.add(0);
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
        //generate a random number
    }

    private static List<Integer> uniformRandom3()
    {
        //2 zones, uniform between 70,80 and then between 20,30
        List<Integer> list = new ArrayList<>();
        Random random = new Random();
        for(int i=1; i<=5; i++)
        {
            int num = random.nextInt(10)+70;
            list.add(num);
        }

        for(int i=1; i<=5; i++)
        {
            int num = random.nextInt(10)+20;
            list.add(num);
        }

        for(int i=1; i<=20; i++)
        {
            list.add(0);
        }
        Collections.sort(list, Collections.reverseOrder());
        return list;
        //generate a random number
    }


}
