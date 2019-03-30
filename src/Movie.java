import java.util.Random;

class Movie {

    private int movieNumber;
    private double movieLength;
    private double moviePosition;

    Movie() {
        Random rand = new Random();
        this.movieNumber = rand.nextInt(Map.getNumberOfMovies() - 1) + 1;
        this.movieLength = Map.getMovieLengths(this.movieNumber);
        this.moviePosition = 0.0;
    }

    int getMovieNumber() {
        return this.movieNumber;
    }

    double getMovieLength() {
        return this.movieLength;
    }

    double getMoviePosition() {
        return this.moviePosition;
    }

    void updateMoviePosition(double moviePosition) {
        this.moviePosition += moviePosition;
    }
}
