import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Justin DiPietro
 * COMP30490 - Recommendation Project
 * 2017-03-10
 *
 * Movie.java
 * For keeping track of the items
 */
public class Movie {

    public Movie(int initID){
        this.id = initID;
    }

    public void addPerson(int personID, DuoPerson duoPerson){           // adds a person to movie map
        mPersonMap.put(personID, duoPerson);
    }

    public int getId() {return id;}

    public void setResnick(){                                           // this adjusts all of the values so that they line up better

        int aveRate = getAveRating();
        int movDist = 3 - aveRate;      // distance between average and 3
        // take that distance and apply it to everything
        List<Integer> keyList = new ArrayList<Integer>(mPersonMap.keySet());    // a list to iterate through
        for (int i = 0; i < keyList.size(); i++) {                              // iterating through the list / map
            int tempRate = mPersonMap.get(keyList.get(i)).rating;               // heres the map part
            while(movDist != 0 && tempRate < 5 && tempRate > 1){                // this moves the average over to 3
                if(movDist > 0){
                tempRate--;
                movDist--;}
                else {
                    tempRate++;
                    movDist++;
                }
            }
        }
    }

    public HashMap<Integer, DuoPerson> getmPersonMap() {return mPersonMap;}

    public int getAveRating(){                                  // return the average of the movie's ratings
        int aveRate = 0;
        List<Integer> keyList = new ArrayList<Integer>(mPersonMap.keySet());
        for (int i = 0; i < keyList.size(); i++){
            aveRate = aveRate + getIndRating(keyList.get(i));
        }
        aveRate = aveRate / keyList.size();                     // total/count = average
        return aveRate;
    }

    public int getIndRating(int personID){                  // get individual rating
        if(mPersonMap.containsKey(personID)){
            //System.out.println("--Person: "+personID+". Item: "+id+". Rating: "+mPersonMap.get(personID).rating);
            return mPersonMap.get(personID).rating;  // pull the rating from the duo
        }else{
            System.out.println("Person " + personID + " has not rated item " + id);
            return Integer.parseInt(null);                  // if it isn't there, return null
        }
    }

    public int getRateCount(){                                  // this is the rate count because each person can only vote once
        return mPersonMap.size();
    }

    private int id;
    private HashMap<Integer, DuoPerson> mPersonMap = new HashMap<Integer, DuoPerson>();

}
