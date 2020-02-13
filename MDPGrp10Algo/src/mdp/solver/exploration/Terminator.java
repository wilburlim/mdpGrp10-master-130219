package mdp.solver.exploration;

import java.util.Timer;
import java.util.TimerTask;
import mdp.map.Map;

public class Terminator {

    private enum TerminatorType {
        Coverage, Time, Round
    }

    private static final int _PROBING_PERIOD = 10;

    private float _maxCoverage;
    private long _maxDiffTime;
    private TerminatorType _terminationType;
    private Runnable _callback;

    private java.util.Timer _thread;

    public Terminator(int round, Runnable callback) {
        // round not used for now
        _terminationType = TerminatorType.Round;
        _callback = callback;
    }

    public Terminator(float maxCoverage, Runnable callback) {
        _maxCoverage = maxCoverage;
        _terminationType = TerminatorType.Coverage;
        _callback = callback;
    }

    public Terminator(long maxDiffTime, Runnable callback) {
        _maxDiffTime = maxDiffTime;
        _terminationType = TerminatorType.Time;
        _callback = callback;
    }

    public void observe() {
        System.out.println("///////////////// " + _terminationType);
        switch (_terminationType) {
            case Coverage:
                int maxExplored = Map.DIM_I * Map.DIM_J;
                _thread = new Timer();
                _thread.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        MapViewer mapViewer = ExplorationSolver.getMapViewer();
                        if (mapViewer != null) {
                            int[][] explored = mapViewer.getExplored();
                            int exploredCount = 0;
                            for (int[] row : explored) {
                                for (int exploreState : row) {
                                    exploredCount += (exploreState >= 1) ? 1 : 0;
                                }
                            }
                            if (((float) exploredCount) / ((float) maxExplored) >= _maxCoverage) {
                                System.out.println("Coverage Terminator activated");
                                _thread.cancel();
                                _callback.run();
                            }
                        }
                    }
                }, 0, _PROBING_PERIOD);
                break;
            case Time:
                _thread = new Timer();
                _thread.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Time Terminator activated");
                        _callback.run();
                    }
                }, _maxDiffTime * 1000);
                break;
            case Round:
                _thread = new Timer();
                _thread.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (ExplorationSolver.hasFinishedFirstRound()) {
                            System.out.println("Round Terminator activated");
                            _thread.cancel();
                            _callback.run();
                        }
                    }
                }, 0, _PROBING_PERIOD);
        }
    }

}
