class MovieSegment {

    private double start;
    private double end;
    private double timeLeft;
    private int movieNumber;
    private boolean inUse;

    MovieSegment(double start, double end, int movieNumber) {
        this.start = start;
        this.end = end;
        this.timeLeft = end - start;
        this.movieNumber = movieNumber;
        this.inUse = true;
    }

    void updateTimeLeft(double timePassed) {
        this.timeLeft -= timePassed;
    }

    void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    boolean isInUse() {
        return this.inUse;
    }

    double getTimeLeft() {
        return this.timeLeft;
    }

    double getStart() {
        return this.start;
    }

    double getEnd() {
        return this.end;
    }

    int getMovieNumber() {
        return this.movieNumber;
    }

    double getLength() {
        return this.end - this.start;
    }


}