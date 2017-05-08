import java.io.*;
import java.util.*;

import static java.lang.Math.abs;


/**
 * Justin DiPietro
 * COMP30490 - Recommendation Project
 * 2017-03-10
 *
 *
 * Start.java
 * Runs the program
 */
public class Start {

    public static void main(String[] args) throws IOException { // runs everything

        final long startTime = System.currentTimeMillis();

        System.out.println("Initiating...");
        String fileName = "100k.txt";                               // Pulls up the datafile

        Rating[] rateList = readFile(fileName);                     // sets the rateList
        Map<Integer, Person> personMap = makePersonMap(rateList);   // makes the PersonMap
        Map<Integer, Movie> movieMap = makeMovieMap(rateList);      // makes the movieMap
        movieMap = fillMovieMap(rateList, movieMap, personMap);     // fills the moviemap
        personMap = fillPersonMap(rateList, personMap, movieMap);   // fills the PersonMap

        getPersonData(personMap);

        getMovieData(movieMap);

        System.out.println("Init Complete");

        System.out.println("Running bigRateLoop");
        bigRateLoop(personMap,movieMap);                            // runs through all of the rating processes

        System.out.println("FINISHED");
        final long endTime = System.currentTimeMillis();

        System.out.println("Total execution time: " + (endTime - startTime) + " ms");
    }

    private static void getPersonData(Map<Integer, Person> personMap){      // gets all the statistics about people
        System.out.println("Running getPersonData");
        List<Integer> persKeyList = new ArrayList<Integer>(personMap.keySet());
        List<Double> rateCounts = new ArrayList<>();
        for (int i = 0; i < persKeyList.size(); i++) {
            int xID = persKeyList.get(i);
            Person xPerson = personMap.get(xID);
            double rateCount = xPerson.getpMovieMap().size();
            rateCounts.add(rateCount);
        }
        double personMax = Collections.max(rateCounts);
        double personMin = Collections.min(rateCounts);
        double personMedian = getMedian(rateCounts);
        double personMean = getMean(rateCounts);
        double personStDev = getStdDev(rateCounts);

        System.out.println("personMax: "+personMax);
        System.out.println("personMin: "+personMin);
        System.out.println("personMedian: "+personMedian);
        System.out.println("personMean: " + personMean);
        System.out.println("personStDev: "+personStDev);
    }

    private static void getMovieData(Map<Integer, Movie> movieMap){         // gets all the statistics about movies
        System.out.println("Running getMovieData");
        List<Integer> persKeyList = new ArrayList<Integer>(movieMap.keySet());
        List<Double> rateCounts = new ArrayList<>();
        for (int i = 0; i < persKeyList.size(); i++) {
            int xID = persKeyList.get(i);
            Movie xMovie = movieMap.get(xID);
            double rateCount = xMovie.getmPersonMap().size();
            rateCounts.add(rateCount);
        }
        double movieMax = Collections.max(rateCounts);
        double movieMin = Collections.min(rateCounts);
        double movieMedian = getMedian(rateCounts);
        double movieMean = getMean(rateCounts);
        double movieStDev = getStdDev(rateCounts);

        System.out.println("movieMax: "+movieMax);
        System.out.println("movieMin: "+movieMin);
        System.out.println("movieMedian: "+movieMedian);
        System.out.println("movieMean: " + movieMean);
        System.out.println("movieStDev: "+movieStDev);
    }

    private static double getMedian(List<Double> rateCounts){   // gets the median number of ratings
        double median = rateCounts.get(rateCounts.size()/2);
        return median;
    }

    private static double getStdDev(List<Double> rateCounts){   // gets the standard deviation

        double mean = getMean(rateCounts);
        double count = rateCounts.size();
        double dv = 0;
        for (double d : rateCounts) {
            double dm = d - mean;
            dv += dm * dm;
        }
        return Math.sqrt(dv / count);
    }

    private static double getMean(List<Double> rateCounts){        // gets the average number of ratings
        double total = 0;
        int count = 0;
        for (int i = 0; i < rateCounts.size(); i++) {
            total = total + rateCounts.get(i);
            count++;
        }
        return total / count;
    }

    private static void bigRateLoop(Map<Integer, Person> personMap, Map<Integer, Movie> movieMap){
        List<Integer> pKeyList = new ArrayList<Integer>(personMap.keySet());
        List<Integer> mKeyList = new ArrayList<Integer>(movieMap.keySet());

// UNCOMMENT below

/*
        System.out.println("Running looMeanLoop");
        try {
            looMeanLoop(personMap,movieMap,pKeyList,mKeyList);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println("Running dbusLoop");
        dbusLoop(personMap,movieMap,pKeyList,mKeyList);
        */
        resLoop(personMap,movieMap,pKeyList,mKeyList);          // currently set up only to run resLoop
    }

    private static void resLoop(Map<Integer, Person> personMap, Map<Integer, Movie> movieMap, List<Integer> pKeyList, List<Integer> mKeyList){
        for (int i = 0; i < pKeyList.size(); i++) {
            Person tempP = personMap.get(pKeyList.get(i));
            tempP.setResnick();
        }
        for (int i = 0; i < mKeyList.size(); i++) {
            Movie tempM = movieMap.get(mKeyList.get(i));
            tempM.setResnick();
        }
        dbusLoop(personMap, movieMap, pKeyList, mKeyList);
    }

    private static void dbusLoop(Map<Integer, Person> personMap, Map<Integer, Movie> movieMap, List<Integer> pKeyList, List<Integer> mKeyList){
        int skipped = 0;                                        // Distance-based system
        int unskip = 0;
        List<Double> percErrList = new ArrayList<>();
        List<TestResult> resultList = new ArrayList<>();
        for (int i = 0; i < pKeyList.size(); i++) {
            Person tPers = personMap.get(pKeyList.get(i));

            for (int j = 0; j < mKeyList.size(); j++) {
                Movie tMovi = movieMap.get(mKeyList.get(j));

                if(tPers.getpMovieMap().containsKey(tMovi.getId()) && tMovi.getRateCount() > 1){
                    // If the person rated the movie and there are enough ratings to leave one out, it continues
                    unskip++;
                    double guess = tPers.findAveCoPredRate(tMovi.getId());                    // So this is the first guess

                    double percentError = getPercentError(tMovi, tPers, guess);            // Gets the percent error from the method for that purpose
                    percErrList.add(percentError);
                    TestResult newResult = new TestResult(tPers.getID(),tMovi.getId(), tMovi.getIndRating(tPers.getID()), guess);
                    resultList.add(newResult);                          // later we put it in a csv

                }else{                                                  // If person didn't rate the movie, it skips to the next one and tallies skipped

                    skipped++;
                }
            }
        }

        double percCover = getCoverage(skipped, unskip);
        System.out.println("Skipped: " + skipped);
        System.out.println("Percent Coverage: " + percCover);
        System.out.println("Average Percent Error: ");
        System.out.println(getAveErr(percErrList));
        System.out.println("Result List Size: " + resultList.size());

        try {
            writeCSV(resultList, "dbusLoop");                // This is where CSVs are made
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // print-outs go here
    }

    private static void looMeanLoop(Map<Integer, Person> personMap, Map<Integer, Movie> movieMap, List<Integer> pKeyList, List<Integer> mKeyList) throws FileNotFoundException {
        int skipped = 0;
        int unskip = 0;
        List<Double> percErrList = new ArrayList();
        List<TestResult> resultList = new ArrayList();

        for (int i = 0; i < pKeyList.size(); i++) {
            Person tPers = personMap.get(pKeyList.get(i));

            for (int j = 0; j < mKeyList.size(); j++) {
                Movie tMovi = movieMap.get(mKeyList.get(j));

                if(tPers.getpMovieMap().containsKey(tMovi.getId()) && tMovi.getRateCount() > 1){
                    // If the person rated the movie and there are enough ratings to leave one out, it continues
                    unskip++;
                    double guess = looMean(tPers, tMovi);                    // So this is the first guess
                    double percentError = getPercentError(tMovi, tPers, guess);
                    percErrList.add(percentError);
                    TestResult newResult = new TestResult(tPers.getID(),tMovi.getId(), tMovi.getIndRating(tPers.getID()), guess);
                    resultList.add(newResult);                          // later we put it in a csv

                }else{                                                  // If person didn't rate the movie, it skips to the next one and tallies skipped
                    skipped++;
                }
            }
        }
        double percCover = getCoverage(skipped, unskip);
        System.out.println("Leave One Out Mean Data ---------------");
        System.out.println("Percent Coverage: \n" + percCover*100);
        System.out.println("Average Percent Error: ");
        System.out.println(getAveErr(percErrList)*100);
        System.out.println("END DATA -----------------");

        try {
            writeCSV(resultList, "looMeanLoop");                // This is where CSVs are made
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void writeCSV(List<TestResult> tResultList, String fileName) throws FileNotFoundException{

        System.out.println("Writing CSV...");                                   // writes the test info to a csv
        System.out.println("CSV size: " + tResultList.size() + " lines");

        PrintWriter pw = new PrintWriter(new File(fileName + ".csv"));
        StringBuilder sb = new StringBuilder();
        sb.append("UserID");
        sb.append(',');
        sb.append("ItemID");
        sb.append(',');
        sb.append("realRate");
        sb.append(',');
        sb.append("guessRate");
        sb.append(',');
        sb.append("RMSE");
        sb.append('\n');

        for (int i = 0; i < tResultList.size(); i++) {
            TestResult tempResult = tResultList.get(i);
            sb.append(tempResult.getUserID());
            sb.append(',');
            sb.append(tempResult.getItemID());
            sb.append(',');
            sb.append(tempResult.getRealRating());
            sb.append(',');
            sb.append(tempResult.getGuessRating());
            sb.append(',');
            sb.append(tempResult.getRMSE());
            sb.append('\n');
        }

        pw.write(sb.toString());
        pw.close();
        System.out.println("CSV done!");
    }

    private static double getAveErr(List<Double> percErrList){          // finds average error by checking guesses against reality
        double total = 0;                                               // does not account for a near miss
        for (int i = 0; i < percErrList.size(); i++) {
            total = total + percErrList.get(i);
        }
        return total / percErrList.size();
    }

    private static double getCoverage(int skipped, int unskip) {        // finds coverage as a proportion of skipping vs not skipping
        int total = skipped + unskip;
        double percCover = (double)skipped / (double)total;
        return percCover;
    }

    private static double looMean(Person person, Movie movie){
        double indRating = (double)movie.getIndRating(person.getID());  // get the rating that this person gave
        double mAveRate = (double)movie.getAveRating();                 // get the average
        int oRateCount = movie.getRateCount();                          // The original number of ratings for the movie
        double oTotalRate = mAveRate * oRateCount;                      // modify the average accordingly
        double nTotalRate = oTotalRate - indRating;                     // The total rating with the one left out
        int nRateCount = oRateCount - 1;                                // The number of ratings for the movie with one left out
        double newAverage = nTotalRate / nRateCount;                    // The new values divided

        return newAverage;                                              // so this is the guess for average L1O
    }           // aka mean item rating

    private static double getPercentError(Movie movie, Person person, double guess){
        double realRating = (double)movie.getIndRating(person.getID());
        double guessRating = guess;
        double percentError = abs(realRating - guessRating) / realRating;

        return percentError;
    }



    private static Rating[] readFile(String fileName){          // reads the file. Buffer for errors
        try {
            Rating[] rateList = readCSV(fileName);              // gets the rateList from readCSV()
            return rateList;
        } catch (IOException e) {
            System.out.println("FAIILED");
            e.printStackTrace();
        }
        System.out.println("read failed");
        System.exit(4);
        return null;
    }                                           // Creates the rateList

    private static Map<Integer, Person> makePersonMap(Rating[] rateList){   // makes all of the empty people :(
        Map<Integer, Person> personMap = new HashMap<>();                   // creates the map for them
        for (int i = 0; i < rateList.length; i++) {                         // iterates thru the rate list
            int tempID = rateList[i].getPersonID();
            if(personMap.containsKey(tempID)){                              // if
            }else{
                //System.out.println("makePersonMap: else");
                Person newP = new Person(tempID);
                personMap.put(tempID, newP);
            }
        }
        return personMap;
    }

    private static Map<Integer, Person> fillPersonMap(Rating[] rateList, Map<Integer, Person> personMap, Map<Integer, Movie> movieMap){
        System.out.println("in fillPersonMap. RateList.length: " + rateList.length);
        for (int i = 0; i < rateList.length; i++) {
            Rating tRate = rateList[i];
        //    System.out.println("tRate.getMovieID(): " + tRate.getMovieID());
        //    System.out.println("tRate.getPersonID(): " + tRate.getPersonID());

            Person tempPerson = personMap.get(tRate.getPersonID());
            Movie xMov = movieMap.get(tRate.getMovieID());
            DuoMovie tempDuoM = new DuoMovie(xMov,tRate.getRating());       // DuoMovie(the movie, the person's rating of the movie)

            tempPerson.addMovie(tRate.getMovieID(), tempDuoM);
        }
        System.out.println("returning filled Person Map");
        return personMap;
    }

    private static Map<Integer, Movie> makeMovieMap(Rating[] rateList){     // This just makes all of the empty movies
        System.out.println("Start makeMovieMap");
        Map<Integer, Movie> movieMap = new HashMap<>();
        for (int i = 0; i < rateList.length; i++) {                         // run through the list of ratings
            int tempID = rateList[i].getMovieID();
            if(movieMap.containsKey(tempID)){                               // if the movie already exists, do nothing
            }else{
                Movie newM = new Movie(tempID);                             // otherwise, make it exist
                movieMap.put(tempID, newM);
            }
        }
        return movieMap;
    }

    private static Map<Integer, Movie> fillMovieMap(Rating[] rateList, Map<Integer, Movie> movieMap, Map<Integer, Person> personMap){   // Puts all of the info into the movie map
        for (int i = 0; i < rateList.length; i++) {                     // loops through the ratelist
            Rating tRate = rateList[i];                                 // on every rating, it finds the movie, and gives it the new rating
            Movie tempMovie = movieMap.get(tRate.getMovieID());         //
            DuoPerson tempDuoP = new DuoPerson(personMap.get(tRate.getPersonID()),tRate.getRating());       // Its adds people as duoPerson(Person, rating)
            tempMovie.addPerson(tRate.getPersonID(), tempDuoP);
        }
        System.out.println("returning filled Movie Map");
        return movieMap;
    }

    private static Rating[] readCSV(String csv) throws IOException {        // Reads the data from the original CSV
        Rating[] rateList = new Rating[100000];                             // loads all of the ratings into this list
        FileInputStream fis = new FileInputStream(csv);

        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        String line = null;                                                 // setting the empty line
        int i = 0;
        while ((line = br.readLine()) != null) {                            // while loop that goes until the end of the file
            String[] parts = line.split(",");
            Rating newRate = new Rating(Integer.parseInt(parts[0]),Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            rateList[i] = newRate;
            i++;
        }
        br.close();
        return rateList;
    }


    }

