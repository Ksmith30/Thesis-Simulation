import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Map {

    private static int numberOfMilesTravelled = 200;
    private static int numberOfMovies = 50;
    private static int lengthOfRoad = 500;
    private static int numberOfCars = 100;
    private static int nodeCoverage = 50;
    private static int lengthOfVideoSegments = 5;
    private static int nodeHopsAllowed = 2;
    private static int maxVideoStoragePerNode = 500;

    private static String modelType = ModelType.FIXED;

    private static HashMap<Integer, Integer> movieNumberUses;
    private static HashMap<Integer, Integer> movieLengths;
    private ArrayList<Node> nodes;
    private ArrayList<Car> cars;
    private int nodeNumber;
    private int nodeHops;
    private int totalNodeHops;
    private int cloudCalls;
    private int nodeSuccess;

    Map() {
        Random rand = new Random();
        this.nodeSuccess = 0;
        this.totalNodeHops = 0;
        this.cloudCalls = 0;
        movieNumberUses = new HashMap<>();
        movieLengths = new HashMap<>();
        for (int i = 1; i <= numberOfMovies; ++i) {
            movieNumberUses.put(i, 0);
            movieLengths.put(i,rand.nextInt(80) + 70);
        }
        this.nodes = new ArrayList<>();
        this.cars = new ArrayList<>();
        initializeNodes();
        initializeCars();
        start();
    }

    Node getOptimalFogNode(double position) {
        Node optimalNode = new Node();
        for (int i = 0; i < nodes.size(); ++i) {
            Node currentNode = nodes.get(i);
            if ((currentNode.getPosition() - (nodeCoverage / 2)) <= position
                    && position < currentNode.getPosition() + (nodeCoverage / 2)) {
                this.nodeNumber = i;
                return currentNode;
            }
        }
        return optimalNode;
    }

    void checkNeighboringFogNodes(MovieSegment movieSegment, int movieNumber) {
        this.nodeHops = 0;
        int high = this.nodeNumber;
        int low = this.nodeNumber;
        while (high < nodes.size()) {
            if (checkNodeHops(movieSegment, movieNumber, high)) break;
            high++;
        }
        this.nodeHops = 0;
        while (low >= 0) {
            if (checkNodeHops(movieSegment, movieNumber, low)) return;
            low--;
        }
        this.cloudCalls++;
    }

    void updateMovieUses(int movieNumber) {
        if (movieNumberUses.containsKey(movieNumber)) {
            movieNumberUses.put(movieNumber, movieNumberUses.get(movieNumber) + 1);
        } else {
            movieNumberUses.put(movieNumber, 1);
        }
    }

    private boolean checkNodeHops(MovieSegment movieSegment, int movieNumber, int nodeNumber) {
        if (nodeHopsAllowed == this.nodeHops) {
            this.cloudCalls++;
            return true;
        }
        if (!this.nodes.get(nodeNumber).contains(movieSegment)) {
            this.totalNodeHops++;
            this.nodeHops++;
        } else {
            this.nodeSuccess++;
            return true;
        }
        return false;
    }

    static HashMap<Integer, Integer> getMovieNumberUses() {
        return movieNumberUses;
    }

    static int getMovieLengths(int movieNumber) {
        return movieLengths.get(movieNumber);
    }

    static int getLengthOfRoad() {
        return lengthOfRoad;
    }

    static int getNodeCoverage() {
        return nodeCoverage;
    }

    static int getLengthOfVideoSegments() {
        return lengthOfVideoSegments;
    }

    static int getMaxVideoStoragePerNode() {
        return maxVideoStoragePerNode;
    }

    static int getNumberOfMovies() {
        return numberOfMovies;
    }

    void updateNodeHops(int nodeHops) {
        this.totalNodeHops += nodeHops;
    }

    private void start() {
        for (int i = 0; i < numberOfCars; ++i) {
            cars.get(i).drive(numberOfMilesTravelled);
        }
    }

    private void initializeNodes() {
        for (int i = nodeCoverage; i <= lengthOfRoad; i = i + nodeCoverage) {
            Node node = new Node();
            node.setPosition(i - (nodeCoverage / 2));
            nodes.add(node);
        }
    }

    private void initializeCars() {
        switch (modelType) {
            case ModelType.FIXED:
                for (int i = 1; i <= numberOfCars; ++i) {
                    Car car = new FixedCar(this, i);
                    cars.add(car);
                }
                break;
            case ModelType.DYNAMIC:
                for (int i = 1; i <= numberOfCars; ++i) {
                    Car car = new DynamicCar(this, i);
                    cars.add(car);
                }
                break;
        }
    }

    private void print(Car car) {
        System.out.println(
                "Current Position: " + car.getCurrentPosition() + "\n"
                        + "Car Speed: " + car.getCarSpeed() + "\n"
                        + "Movie Number: " + car.getCurrentMovie().getMovieNumber() + "\n"
                        + "Movie Length: " + car.getCurrentMovie().getMovieLength() + "\n"
                        + "Movie Position: " + car.getCurrentMovie().getMoviePosition() + "\n"
        );
    }
}
