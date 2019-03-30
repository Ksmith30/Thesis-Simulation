import java.util.ArrayList;
import java.util.HashMap;

class Node {

    private HashMap<Integer, ArrayList<MovieSegment>> movieSegments;
    private HashMap<Integer, ArrayList<MovieSegment>> requestHistory;
    private int position;
    private double amountOfVideoStored;

    Node() {
        this.movieSegments = new HashMap<>();
        this.requestHistory = new HashMap<>();
    }

    void setPosition(int position) {
        this.position = position;
    }

    void addRequest(int carNumber, MovieSegment movieSegment) {
        if (requestHistory.containsKey(carNumber)) {
            requestHistory.get(carNumber).add(movieSegment);
        } else {
            ArrayList<MovieSegment> movieSeg = new ArrayList<>();
            movieSeg.add(movieSegment);
            requestHistory.put(carNumber, movieSeg);
        }
    }

    int getPosition() {
        return this.position;
    }

    void addMovieSegment(MovieSegment movieSegment) {
        if (movieSegments.containsKey(movieSegment.getMovieNumber())) {
            movieSegments.get(movieSegment.getMovieNumber()).add(movieSegment);
        } else {
            ArrayList<MovieSegment> segment = new ArrayList<>();
            segment.add(movieSegment);
            movieSegments.put(movieSegment.getMovieNumber(), segment);
        }
        addSegmentToStorage(movieSegment.getLength());
    }

    private void addSegmentToStorage(double currentVideoStorage) {
        if (this.amountOfVideoStored + currentVideoStorage <= Map.getMaxVideoStoragePerNode()) {
            this.amountOfVideoStored += currentVideoStorage;
        } else {
            int leastPopularMovie = getLeastPopularMovie();
            purgeLeastPopularVideoSegment(leastPopularMovie);
            this.amountOfVideoStored += currentVideoStorage;
        }

    }

    private void purgeLeastPopularVideoSegment(int leastPopularMovie) {
        for (int i = 0; i < this.movieSegments.get(leastPopularMovie).size(); ++i) {
            if (findSegmentNotInUse(leastPopularMovie, i)) return;
        }
        while (true) {
            leastPopularMovie = (leastPopularMovie + 1) % (Map.getNumberOfMovies() - 1);
            if (this.movieSegments.containsKey(leastPopularMovie)) {
                for (int i = 0; i < this.movieSegments.get(leastPopularMovie).size(); ++i) {
                    if (findSegmentNotInUse(leastPopularMovie, i)) return;
                }
            }
        }
    }

    private boolean findSegmentNotInUse(int leastPopularMovie, int i) {
        if (!this.movieSegments.get(leastPopularMovie).get(i).isInUse()) {
            this.amountOfVideoStored -= (this.movieSegments.get(leastPopularMovie).get(i).getLength());
            movieSegments.remove(i);
            return true;
        }
        return false;
    }

    private int getLeastPopularMovie() {
        HashMap<Integer, Integer> movieNumberUses = Map.getMovieNumberUses();
        int leastPopularMovie = Integer.MAX_VALUE;
        int minWatches = Integer.MAX_VALUE;

        for (Integer movieNumber : this.movieSegments.keySet()) {
            if (minWatches > movieNumberUses.get(movieNumber)) {
                minWatches = movieNumberUses.get(movieNumber);
                leastPopularMovie = movieNumber;
            }
        }
        return leastPopularMovie;
    }

    boolean contains(MovieSegment movieSegment) {
        if (movieSegments.containsKey(movieSegment.getMovieNumber())) {
            ArrayList<MovieSegment> arrayList = movieSegments.get(movieSegment.getMovieNumber());
            for (int i = 0; i < arrayList.size(); ++i) {
                MovieSegment cur = movieSegments.get(movieSegment.getMovieNumber()).get(i);
                if (cur.getStart() == movieSegment.getStart() && cur.getEnd() == movieSegment.getEnd()) {
                    return true;
                }
            }
        }
        return false;
    }
}
