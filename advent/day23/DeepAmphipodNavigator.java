package advent.day23;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import advent.day15.BigCavePathFinder.PrioritizedEntity;
import advent.day15.BigCavePathFinder.PriorityMap;

public class DeepAmphipodNavigator {    
    private static final int NUM_HALLWAYS_POSITIONS = 11;
    private static final int NUM_ROOMS = 4;
    private static final int NUM_POSITIONS = NUM_HALLWAYS_POSITIONS + 4 * NUM_ROOMS;

    private static final int[] HALLWAY_DESTINATIONS = { 0, 1, 3, 5, 7, 9, 10};

    private static boolean isHallway(int position) {
        return position < NUM_HALLWAYS_POSITIONS;
    }

    private static List<Integer> getUpperRoomPositions(int room) {
        if (isHallway(room)) {
            return null;
        }

        List<Integer> positions = new ArrayList<>();
        int upperRoom = room - 4;
        while (upperRoom > 10) {
            positions.add(upperRoom);
            upperRoom -= 4;
        }
        return positions;
    }

    private static int getExitPosition(int room) {
        if (room == 11 || room == 15 || room == 19 || room == 23) {
            return 2;
        } else if (room == 12 || room == 16 || room == 20 || room == 24) {
            return 4;
        } else if (room == 13 || room == 17 || room == 21 || room == 25) {
            return 6;
        } else if (room == 14 || room == 18 || room == 22 || room == 26) {
            return 8;
        }
        System.err.println("Shouldn't get here");
        System.exit(1);
        return -1;
    }

    private static int getLowRoomPosition(char name) {
        if (name == 'A') {
            return 23;
        } else if (name == 'B') {
            return 24;
        } else if (name == 'C') {
            return 25;
        } else {
            return 26;
        }
    }

    private static List<Integer> getRoomPositions(char name) {
        if (name == 'A') {
            Integer[] rooms = {23, 19, 15, 11};
            return Arrays.asList(rooms);
        } else if (name == 'B') {
            Integer[] rooms = {24, 20, 16, 12};
            return Arrays.asList(rooms);
        } else if (name == 'C') {
            Integer[] rooms = {25, 21, 17, 13};
            return Arrays.asList(rooms);
        } else {
            Integer[] rooms = {26, 22, 18, 14};
            return Arrays.asList(rooms);
        }
    }

    private static int getRoomEntrance(char name) {
        if (name == 'A') {
            return 2;
        } else if (name == 'B') {
            return 4;
        } else if (name == 'C') {
            return 6;
        } else {
            return 8;
        }
    }

    private static int getStepCost(char name) {
        if (name == 'A') {
            return 1;
        } else if (name == 'B') {
            return 10;
        } else if (name == 'C') {
            return 100;
        } else {
            return 1000;
        }
    }

    private static class Agent {
        char name;

        String id;

        public Agent(String id, char name) {
            this.id = id;
            this.name = name;
        }
    }

    private static class AmphipodState {
        Map<Integer, Agent> agents = new HashMap<>();

        public AmphipodState() {}

        public AmphipodState(AmphipodState other) {
            agents.putAll(other.agents);
        }

        public void addAgent(Agent agent, Integer position) {
            agents.put(position, agent);
        }

        @Override
        public String toString() {
            char[] state = new char[NUM_POSITIONS];
            Arrays.fill(state, '.');
            for (var entry : agents.entrySet()) {
                state[entry.getKey()] = entry.getValue().name;
            }
            return new String(state);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof AmphipodState && this.toString().equals(obj.toString());
        }

        @Override
        public int hashCode() {
            return Objects.hash(toString());
        }
    }

    public static Map<AmphipodState, Integer> getOpenChildStates(AmphipodState parent, Set<AmphipodState> closed) {
        Map<AmphipodState, Integer> childStates = new HashMap<>();

        // Enumerate possible moves for each child
        for (var entry : parent.agents.entrySet()) {
            Agent agent = entry.getValue();
            int agentStartPosition = entry.getKey();
            
            int targetLowRoom = getLowRoomPosition(agent.name);
            List<Integer> targetRooms = getRoomPositions(agent.name);
            
            int targetHallwayRoom = getRoomEntrance(agent.name);

            // 1. Won't stop outside ANY room
            // 2. Won't move into room unless it's vacant AND the correct room, or contains an agent of the same name.
            // 3. If in hallway, will only move if can get to room

            // Additional rule: Don't move into upperRoom if departing a room!

            // If low in the right room, don't move
            if (agentStartPosition == targetLowRoom) {
                continue;
            }
            
            if (targetRooms.indexOf(agentStartPosition) != -1) {
                boolean isBlocking = false;
                // If higher in the right room, don't allow a move unless blocking someone that must move.
                for (int i=0; i<targetRooms.indexOf(agentStartPosition); i++) {
                    int room = targetRooms.get(i);
                    if (parent.agents.containsKey(room)) {
                        Agent lowerAgent = parent.agents.get(room);
                        if (lowerAgent.name != agent.name) {
                            isBlocking = true;
                        }
                    }
                }
    
                if (!isBlocking) {
                    continue;
                }
            }
            
            if (isHallway(agentStartPosition)) {
                // If starting in hallway, must end up in right room.
                boolean isObstructed = false;

                // Check entire path to room entrance
                if (agentStartPosition < targetHallwayRoom) {
                    for (int i=agentStartPosition+1; i<=targetHallwayRoom; i++) {
                        if (parent.agents.containsKey(i)) {
                            // Path obstructed
                            isObstructed = true;
                        }
                    }
                } else {
                    for (int i=agentStartPosition-1; i>=targetHallwayRoom; i--) {
                        if (parent.agents.containsKey(i)) {
                            // Path obstructed
                            isObstructed = true;
                        }
                    }
                }

                int pathLength = Math.abs(targetHallwayRoom - agentStartPosition);
                
                // If room has wrong other agent, don't move in
                for (int targetRoom : targetRooms) {
                    if (parent.agents.containsKey(targetRoom)) {
                        Agent otherAgent = parent.agents.get(targetRoom);
                        if (otherAgent.name != agent.name) {
                            isObstructed = true;
                        }
                    }
                }

                if (isObstructed) {
                    continue;
                }

                // Move into the lowest room we can
                for (int i=0; i<targetRooms.size(); i++) {
                    int targetRoom = targetRooms.get(i);
                    if (!parent.agents.containsKey(targetRoom)) {
                        AmphipodState childState = new AmphipodState(parent);
                        childState.agents.remove(agentStartPosition);
                        childState.agents.put(targetRoom, agent);

                        pathLength += 4 - i;
                        if (!closed.contains(childState)) {   
                            childStates.put(childState, pathLength * getStepCost(agent.name));
                            break;
                        }
                    }
                }
            } else {
                boolean hasRoomBlocker = false;
                for (Integer upperRoom : getUpperRoomPositions(agentStartPosition)) {
                    if (parent.agents.containsKey(upperRoom)) {
                        hasRoomBlocker = true;
                    }
                }
                if (hasRoomBlocker) {
                    continue;
                }

                int pathLengthToHallway = (agentStartPosition - 7) / 4;
                
                int hallwayOrigin = getExitPosition(agentStartPosition);

                for (int hallwayDestination : HALLWAY_DESTINATIONS) {
                    // Check entire path to hallway destination
                    boolean isObstructed = false;
                    if (hallwayOrigin < hallwayDestination) {
                        for (int i=hallwayOrigin+1; i<=hallwayDestination; i++) {
                            if (parent.agents.containsKey(i)) {
                                // Path obstructed
                                isObstructed = true;
                            }
                        }
                    } else {
                        for (int i=hallwayOrigin-1; i>=hallwayDestination; i--) {
                            if (parent.agents.containsKey(i)) {
                                // Path obstructed
                                isObstructed = true;
                            }
                        }
                    }

                    if (isObstructed) {
                        continue;
                    }

                    int pathLength = Math.abs(hallwayDestination - hallwayOrigin) + pathLengthToHallway;
                    AmphipodState childState = new AmphipodState(parent);
                    childState.agents.remove(agentStartPosition);
                    childState.agents.put(hallwayDestination, agent);

                    if (!closed.contains(childState)) {   
                        childStates.put(childState, pathLength * getStepCost(agent.name));
                    }
                }
            }

            // Do we need to add moves that go all the way to from start room to dest room? We might!
        }

        return childStates;
    }

    public static int getMinPathCost(AmphipodState start, AmphipodState goal) {
        PriorityMap<AmphipodState> openNodes = new PriorityMap<>();
        Set<AmphipodState> closedNodes = new HashSet<>();
        Map<AmphipodState, Integer> pathCosts = new HashMap<>();

        openNodes.put(start, 0);
        pathCosts.put(start, 0);

        while (!openNodes.isEmpty()) {
            PrioritizedEntity<AmphipodState> priorityEntry = openNodes.poll();
            int minPathCost = priorityEntry.getPriority();
            AmphipodState minCostNode = priorityEntry.getEntity();

            closedNodes.add(minCostNode);
            if (goal.equals(minCostNode)) {
                break;
            }

            for (var entry : getOpenChildStates(minCostNode, closedNodes).entrySet()) {
                AmphipodState childState = entry.getKey();
                pathCosts.putIfAbsent(childState, Integer.MAX_VALUE);
                int pathCost = Integer.min(minPathCost + entry.getValue(), pathCosts.get(childState));
                pathCosts.put(childState, pathCost);
                openNodes.put(childState, pathCost);
            }
        }

        return pathCosts.get(goal);
    }

    public static void main(String[] args) {
        /*AmphipodState start = new AmphipodState();
        start.addAgent(new Agent("A1", 'A'), 18);
        start.addAgent(new Agent("A2", 'A'), 21);
        start.addAgent(new Agent("A3", 'A'), 23);
        start.addAgent(new Agent("A4", 'A'), 26);
        start.addAgent(new Agent("B1", 'B'), 11);
        start.addAgent(new Agent("B2", 'B'), 13);
        start.addAgent(new Agent("B3", 'B'), 17);
        start.addAgent(new Agent("B4", 'B'), 20);
        start.addAgent(new Agent("C1", 'C'), 12);
        start.addAgent(new Agent("C2", 'C'), 16);
        start.addAgent(new Agent("C3", 'C'), 22);
        start.addAgent(new Agent("C4", 'C'), 25);
        start.addAgent(new Agent("D1", 'D'), 14);
        start.addAgent(new Agent("D2", 'D'), 15);
        start.addAgent(new Agent("D3", 'D'), 19);
        start.addAgent(new Agent("D4", 'D'), 24);*/

        AmphipodState start = new AmphipodState();
        start.addAgent(new Agent("A1", 'A'), 13);
        start.addAgent(new Agent("A2", 'A'), 18);
        start.addAgent(new Agent("A3", 'A'), 21);
        start.addAgent(new Agent("A4", 'A'), 26);
        start.addAgent(new Agent("B1", 'B'), 14);
        start.addAgent(new Agent("B2", 'B'), 17);
        start.addAgent(new Agent("B3", 'B'), 20);
        start.addAgent(new Agent("B4", 'B'), 23);
        start.addAgent(new Agent("C1", 'C'), 12);
        start.addAgent(new Agent("C2", 'C'), 16);
        start.addAgent(new Agent("C3", 'C'), 22);
        start.addAgent(new Agent("C4", 'C'), 24);
        start.addAgent(new Agent("D1", 'D'), 11);
        start.addAgent(new Agent("D2", 'D'), 15);
        start.addAgent(new Agent("D3", 'D'), 19);
        start.addAgent(new Agent("D4", 'D'), 25);

        // Isometric goal states are checked for by our state equality
        AmphipodState goal = new AmphipodState();
        goal.addAgent(new Agent("A1", 'A'), 11);
        goal.addAgent(new Agent("A2", 'A'), 15);
        goal.addAgent(new Agent("A3", 'A'), 19);
        goal.addAgent(new Agent("A4", 'A'), 23);
        goal.addAgent(new Agent("B1", 'B'), 12);
        goal.addAgent(new Agent("B2", 'B'), 16);
        goal.addAgent(new Agent("B3", 'B'), 20);
        goal.addAgent(new Agent("B4", 'B'), 24);
        goal.addAgent(new Agent("C1", 'C'), 13);
        goal.addAgent(new Agent("C2", 'C'), 17);
        goal.addAgent(new Agent("C3", 'C'), 21);
        goal.addAgent(new Agent("C4", 'C'), 25);
        goal.addAgent(new Agent("D1", 'D'), 14);
        goal.addAgent(new Agent("D2", 'D'), 18);
        goal.addAgent(new Agent("D3", 'D'), 22);
        goal.addAgent(new Agent("D4", 'D'), 26);

        System.out.println(getMinPathCost(start, goal));
    }
}
