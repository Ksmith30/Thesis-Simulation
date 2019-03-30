import java.util.Random;

abstract class Car {

    private int carNumber;
    private double currentPosition;
    private double carSpeed;
    private double minutesPerMile;
    private Movie currentMovie;
    private MovieSegment currentSegment;
    private Map map;
    private Node optimalFogNode;
    private Node coordinator;

    Car(Map map, int carNumber) {
        Random rand = new Random();
        this.map = map;
        this.carNumber = carNumber;
        this.carSpeed = rand.nextInt(35) + 50;
        this.minutesPerMile = 60 / this.carSpeed;
        this.currentPosition = rand.nextInt(500);
        this.currentMovie = new Movie();
        this.map.updateMovieUses(this.currentMovie.getMovieNumber());
        this.coordinator = map.getOptimalFogNode(currentPosition);
        this.optimalFogNode = map.getOptimalFogNode(currentPosition);
        this.currentSegment = getMovieSegment(0.0);
        addSegmentAndRequest();
    }

    abstract MovieSegment getMovieSegment(double end);

    void drive(int numberOfMiles) {
        for (int miles = 0; miles < numberOfMiles; ++miles) {
            this.optimalFogNode = map.getOptimalFogNode(currentPosition);
            updateMovie();
            this.currentPosition++;
            if (this.currentPosition > Map.getLengthOfRoad()) {
                break;
            }
        }
    }

    private boolean stillInCurrentMovie() {
        return this.currentMovie.getMoviePosition() + this.minutesPerMile < this.currentMovie.getMovieLength();
    }

    private void addSegmentAndRequest() {
        if (!this.optimalFogNode.contains(this.currentSegment)) {
            this.map.checkNeighboringFogNodes(this.currentSegment, this.currentMovie.getMovieNumber());
            this.optimalFogNode.addMovieSegment(this.currentSegment);
        }
        this.coordinator.addRequest(carNumber, this.currentSegment);
    }

    private void updateMovie() {
        if (stillInCurrentMovie()) {
            this.currentMovie.updateMoviePosition(this.minutesPerMile);
            updateMovieSegment();
        } else {
            this.currentSegment.updateTimeLeft(this.currentMovie.getMovieLength() - this.currentMovie.getMoviePosition());
            this.currentSegment.setInUse(false);
            this.currentMovie.updateMoviePosition(this.currentMovie.getMovieLength() - this.currentMovie.getMoviePosition());
            this.currentMovie = new Movie();
            this.map.updateMovieUses(this.currentMovie.getMovieNumber());
            this.currentSegment = getMovieSegment(0);
            addSegmentAndRequest();
        }
    }

    private void updateMovieSegment() {
        if (this.currentSegment.getTimeLeft() < this.minutesPerMile) {
            double timeLeft = this.currentSegment.getTimeLeft();
            this.currentSegment.updateTimeLeft(timeLeft);
            this.currentSegment.setInUse(false);
            this.currentSegment = getMovieSegment(this.currentSegment.getEnd());
            this.currentSegment.updateTimeLeft(this.minutesPerMile - timeLeft);
            addSegmentAndRequest();
        }  else {
            this.currentSegment.updateTimeLeft(this.minutesPerMile);
        }
    }

    double getCurrentPosition() {
        return this.currentPosition;
    }

    double getCarSpeed() {
        return this.carSpeed;
    }

    Movie getCurrentMovie() {
        return this.currentMovie;
    }

    Node getOptimalFogNode() {
        return this.optimalFogNode;
    }

    Node getCoordinator() {
        return this.coordinator;
    }

    Map getMap() {
        return this.map;
    }
}
