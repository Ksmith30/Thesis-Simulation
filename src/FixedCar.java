class FixedCar extends Car {

    FixedCar(Map map, int carNumber) {
        super(map, carNumber);
    }

    MovieSegment getMovieSegment(double end) {

        int coordinatorNumber = Map.getLengthOfRoad() / this.getCoordinator().getPosition();
        int currentNodeNumber = Map.getLengthOfRoad() / this.getOptimalFogNode().getPosition();
        int numberOfHops = Math.abs(currentNodeNumber - coordinatorNumber);
        this.getMap().updateNodeHops(numberOfHops);

        double milesLeftInRange = (this.getOptimalFogNode().getPosition() + (Map.getNodeCoverage() / 2)) - this.getCurrentPosition();
        double minutesLeftInRange = milesLeftInRange / (this.getCarSpeed() / 60);
        if (Map.getLengthOfVideoSegments() <= minutesLeftInRange) {
            minutesLeftInRange = Map.getLengthOfVideoSegments();
        }

        if (minutesLeftInRange + end > this.getCurrentMovie().getMovieLength()) {
            minutesLeftInRange = this.getCurrentMovie().getMovieLength() - end;
        }

        return new MovieSegment(end, end + minutesLeftInRange,
                this.getCurrentMovie().getMovieNumber());
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
