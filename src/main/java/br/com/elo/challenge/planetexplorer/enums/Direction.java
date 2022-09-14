package br.com.elo.challenge.planetexplorer.enums;

public enum Direction {
        North,
        East,
        South,
        West;

        public static Direction nextDirection(Direction orientation) {
                Direction nextDir = orientation;
                switch (orientation) {
                        case North:
                                nextDir = East;
                                break;
                        case East:
                                nextDir = South;
                                break;
                        case South:
                                nextDir = West;
                                break;
                        case West:
                                nextDir = North;
                                break;
                }
                return nextDir;
        }
}
