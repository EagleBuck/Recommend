
Justin DiPietro
COMP30490 - Recommendation Project
2017-03-10


RUNNING:
	The way it is set up by default, it will only run the Resnick version. The other 2 systems can be accessed by uncommenting them in BigRateLoop.
	The CSV filename can be changed at the end of looMeanLoop and dbusLoop

SOURCE CODE:
	In folder labled "src"
	CoRater:
		The neighbor class. 
		Co-Rated a movie with the target person
	DuoMovie:
		Pairs up a movie and a rating.
		Stored with the respective person
	DuoPerson:
		Pairs up a person and a rating
		Stored with the respective movie
	Movie:
		Item class
		Stores all of the information about the movie
		Keeps maps of people that have rated it
	Person:
		User class
		Stores all of the information about the person
		Keeps map of all the movies it has rated
		Keeps map of all of the other people it has rated alongside
	Rating:
		Rating class
		Stores the information paired with each line of the CSV
		Does not include timestamp
	Start:
		Main Class
		Runs all of the processes
	TestResults:
		Keeps all of the data about individual tests 
		Can be easily averaged

CSV FILES:
	dbusLoop_baseline.csv
		Data for the distance based method.
		This applied no limits to the neighbor hood.
		It slightly wheighted near neighbors
	dbusLoop_inverted.csv
		Data for distance based method
		This limited to neighbors with inverse interests of the target
		It mostly failed beccause so few people had other users with opposite interests
	dbusLoop_upper_.5.csv
		Data for distance based method
		this cut off neighbors that were far from the user
	dbusLoop_upper_.8.csv
		Data for distance based method
		this cut off almost all neighbors by requiring neighbors be very close
	looMeanLoop.csv
		Data from the Leave one out mean method
		based purely on filling in for the adjusted mean
		not very accurate
	resnick_baseline.csv
		Data for resnick + distance based
		This applied no limits to the neighbor hood.
		It slightly wheighted near neighbors
		all averages were adjusted to 3
	resnick_inverted.csv
		Resnick Data for distance based method
		all averages were adjusted to 3
		This limited to neighbors with inverse interests of the target
		It mostly failed beccause so few people had other users with opposite interests
	resnick_upper_.5.csv
		Data for distance based Resnick method
		all averages were adjusted to 3
		this cut off neighbors that were far from the user
	resnick_upper_.8.csv
		all averages were adjusted to 3
		Data for resnick distance based method
		this cut off almost all neighbors by requiring neighbors be very close