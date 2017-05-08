import static java.lang.Math.sqrt;

/**
 * Justin DiPietro
 * COMP30490 - Recommendation Project
 * 2017-03-10
 *
 * TestResult.java
 * For keeping track of test results
 *
 */
public class TestResult {
    public TestResult(int uID, int iID, int rRating, double gRating){
        userID = uID;
        itemID = iID;
        realRating = rRating;
        guessRating = gRating;
        rmse = getRMSE(realRating, guessRating);
    }
    private double getRMSE(int realRating, double guessRating){                // This generates individual rmse
        double error = guessRating - (double)realRating;
        error = error * error;
        double rmse =  sqrt(error);
        return rmse;
    }

    public int getUserID() {return userID;}

    public int getItemID() {return itemID;}

    public int getRealRating() {return realRating;}

    public double getGuessRating() {return guessRating;}

    public double getRMSE() {return rmse;}

    private int userID;
    private int itemID;
    private int realRating;
    private double guessRating;
    private double rmse;
}
