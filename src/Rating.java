/**
 * Justin DiPietro
 * COMP30490 - Recommendation Project
 * 2017-03-10
 *
 * Rating.java
 * For keeping track of ratings
 *
 */
public class Rating {
    public Rating(int initPersonID, int initMovieID, int initRating){
        this.personID = initPersonID;
        this.movieID = initMovieID;
        this.rating = initRating;
    }

    public int getPersonID() {return personID;}
    public int getMovieID() {return movieID;}
    public int getRating() {return rating;}
    // person
    // movie
    // rating

    private int personID;
    private int movieID;
    private int rating;

}
