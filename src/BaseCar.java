class BaseCar extends Car {

    BaseCar(Map map, int carNumber) {
        super(map, carNumber);
    }

    MovieSegment getMovieSegment(double end) {
        double milesLeftInRange = (getOptimalFogNode().getPosition() + (Map.getNodeCoverage() / 2)) - this.getCurrentPosition() ;
        double minutesLeftInRange = milesLeftInRange / (this.getCarSpeed() / 60);
        if (Map.getLengthOfVideoSegments() <= minutesLeftInRange) {
            minutesLeftInRange = Map.getLengthOfVideoSegments();
        }

        if (minutesLeftInRange + end > getCurrentMovie().getMovieLength()) {
            minutesLeftInRange = getCurrentMovie().getMovieLength() - end;
        }

        return new MovieSegment(end, end + minutesLeftInRange,
                getCurrentMovie().getMovieNumber());
    }

    void drive(int numberOfMiles) {
        for (int miles = 0; miles < numberOfMiles; ++miles) {
            if (this.getCurrentPosition() >= Map.getLengthOfRoad()) {
                break;
            }
            this.setOptimalFogNode(this.getMap().getOptimalFogNode(this.getCurrentPosition()));
            this.updateMovie();
            this.setCurrentPosition(getCurrentPosition() + 1);
        }
    }
}
