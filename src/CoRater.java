import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Justin DiPietro
 * COMP30490 - Recommendation Project
 * 2017-03-10
 *
 * CoRater.java
 * For keeping track of all of the neighbors
 *
 */
public class CoRater {                              // a CoRater is a neighbor
    public CoRater(Person inPerson, int inPersonID){
        coPerson = inPerson;                        // this points to the neighbor
        coPersonID = inPersonID;                    // this is the neighbor's id for mapping purposes
    }

    public double getAveRateDif() {                 // returns the average of all of the rating differences

        List<Integer> coKeyList = new ArrayList<>(coMovieMap.keySet()); // turn difference map into a list that can be iterated through
        int total = 0;                                                  // starting point for calculating the average
        for (int i = 0; i < coKeyList.size(); i++) {                    // runs through and calculates total value
            total = total + coMovieMap.get(coKeyList.get(i));
        }
        double aveRateDif = (double)total / (double)coKeyList.size();   // total / count = average
        return aveRateDif;
    }

    public double getCoPredRate(int movieID){
        //System.out.println("aveRateDiff: " + getAveRateDif());
        double tempDub = getAveRateDif() * 200;                         // agreescale puts each neighbor on a -1 to 1 scale of agreement
        double tempX = tempDub / 4;                                     // its based on the average difference
        double tempY = tempX - 100;
        double tempZ = tempY * - 1;
        double agreeScale = tempZ / 100;

        DuoMovie tempDuoMovie = coPerson.getpMovieMap().get(movieID);   // THis pulls out the duo from the neighbor's movieMap

        try{
            boolean x = tempDuoMovie.movie != null;                     // this triggers the null pointer
        }
        catch (NullPointerException e){
            return 9.0;                                                 // the average maker that this function returns to interprets 9.0 as a skip
        }
        if(agreeScale< 0.8){
            return 9.0;                                                 // returning 9.0 is the signal to skip
        }else{
            Movie tempMovie = tempDuoMovie.movie;          // pulls the movie out of the dup

            int coRating = tempMovie.getIndRating(coPersonID);

            // coRater Person, pull up the Personal movieMap, pull out this movie. get the rating for the person
            // now we take the coRating and multiply by the agree scale. That should give us the coPredicted rating
            double coPredRate = agreeScale * (double)coRating;

            return coPredRate;
        }
    }

    public Person coPerson;
    public int coPersonID;
    public HashMap<Integer, Integer> coMovieMap = new HashMap<>(); // Movies that overlaps with us: movieID, rateDifference

}
