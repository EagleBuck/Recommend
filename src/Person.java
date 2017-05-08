import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Justin DiPietro
 * COMP30490 - Recommendation Project
 * 2017-03-10
 *
 * Person.java
 * for all of the users
 */
public class Person {
    public Person(int initID){
        this.id = initID;
    }

    public void setResnick(){

        int aveRate = getAveRating();
        int movDist = 3 - aveRate;      // distance between average and 3
                                            // take that distance and apply it to everything
        List<Integer> keyList = new ArrayList<Integer>(pMovieMap.keySet());
        for (int i = 0; i < keyList.size(); i++) {

            int tempRate = pMovieMap.get(keyList.get(i)).rating;
            while(movDist != 0 && tempRate < 5 && tempRate > 1){
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

    public void addMovie(int movieID, DuoMovie duoMovie){
        pMovieMap.put(movieID, duoMovie);
    }

    public int getID() {return id;}
    public HashMap<Integer, DuoMovie> getpMovieMap() {return pMovieMap;}

    public int getAveRating(){
        int aveRate = 0;
        List<Integer> keyList = new ArrayList<Integer>(pMovieMap.keySet());
        for (Integer aKeyList : keyList) {
            aveRate = aveRate + pMovieMap.get(aKeyList).rating;
        }
        aveRate = aveRate / keyList.size();
        return aveRate;
    }

    private HashMap<Integer, CoRater> findCoRaters(){
        // get all xPerson's rating
        // Stick em in a list.
        HashMap<Integer, CoRater> coRaterMap = new HashMap<>();                          // an empty map of all the ppl that have intersected with us. Gets returned

        List<Integer> mKeyList = new ArrayList<>(pMovieMap.keySet());            // a list of all of the movieIDs we've rated

        for (Integer aMKeyList : mKeyList) {                                      // this iterates through the movies

            Movie tempMovie = pMovieMap.get(aMKeyList).movie;                     // this is the selected movie

            HashMap<Integer, DuoPerson> tempRaterMap = tempMovie.getmPersonMap();      // now we need all of the people that have rated the movie

            List<Integer> raterKeyList = new ArrayList<>(tempRaterMap.keySet());           // a list of all of the people who've rated

            // then we use the key list in the jLoop
            for (int i = 0; i < raterKeyList.size(); i++) {
                int tempRaterID = raterKeyList.get(i);
                // this is for the raters of the movie

                //&& tempMovie.getmPersonMap().containsKey(tempRaterID) && tempMovie.getmPersonMap().containsKey(tempRaterID)

                if (coRaterMap.containsKey(tempRaterID) && tempMovie.getmPersonMap().containsKey(tempRaterID)) {          // Is the coRaterPerson already recorded

                    //System.out.println("if");
                    CoRater tempCo = coRaterMap.get(tempRaterID);

                    int tempDif = tempMovie.getIndRating(tempCo.coPersonID) - tempMovie.getIndRating(this.id);

                    tempCo.coMovieMap.put(tempMovie.getId(), tempDif);            // if yes, record this instance of coRating

                } else if(tempMovie.getmPersonMap().containsKey(tempRaterID)){                                                      // otherwise
                    //System.out.println("else if");
                    CoRater newCo = new CoRater(tempRaterMap.get(tempRaterID).person, tempRaterID);  // make a new coRater UNLESS PERSON HASNT RATED

                    coRaterMap.put(tempRaterID, newCo);

                    int tempDif = tempMovie.getIndRating(newCo.coPersonID) - tempMovie.getIndRating(this.id);      // y - x

                    newCo.coMovieMap.put(tempMovie.getId(), tempDif);
                }else{
                    System.out.println("i dont know anymore");
                }
            }
        }
        return coRaterMap;
    }

    public double findAveCoPredRate(int movieID){                                          // find the average coRater predicted rate
        this.coRaterMap = findCoRaters();
        List<Integer> coKeyList = new ArrayList<>(coRaterMap.keySet());     // this is a list of all the coRater keys. For iterating
        double predTotal = 0;
        int predCount = 0;
        for (int i = 0; i < coKeyList.size(); i++) {

            CoRater inbet = coRaterMap.get(coKeyList.get(i));

            double moreBet = inbet.getCoPredRate(movieID);

            //System.out.println("Predicted Rate: " + moreBet);

            if(moreBet == 9.0){         // a hacky solution to the problem
                continue;
            }

            predTotal = predTotal + moreBet;

            predCount++;
        }
        double coPredAve = predTotal / (double)predCount;
        return coPredAve;
    }


    private int id;
    private HashMap<Integer, DuoMovie> pMovieMap = new HashMap<>(); // this checks out
    private HashMap<Integer, CoRater> coRaterMap = new HashMap<>();

    // it makes more sense to store this by movie instead of rating id
    // cuz only 1 rating per movie anyway
    // wedoboth
}


/*
Start
Read info from CSV
Now we have a bunch of ratings

Goal: to get a person's average rating
For that we need to be able to get all the data from a single person
Assume that we need to do this for every person

Movie is also functionally identical for now

We're only actually copying it twice because ppl only have all of the reviews that they did
PersonMap and MovieMap are just RatingMap with a different value as the key for the sakke of usablility

 */