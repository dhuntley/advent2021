package advent.day19;

import java.util.ArrayList;
import java.util.List;

import advent.common.util.Coord3D;
import advent.common.util.InputReader;

public class BeaconMapper {

    private static List<Coord3D> validRotations = new ArrayList<>() {{
        add(new Coord3D(1, 2, 3));
        add(new Coord3D(1, 2, -3));
        add(new Coord3D(1, -2, 3));
        add(new Coord3D(1, -2, -3));
        add(new Coord3D(-1, 2, 3));
        add(new Coord3D(-1, 2, -3));
        add(new Coord3D(-1, -2, 3));
        add(new Coord3D(-1, -2, -3));

        add(new Coord3D(1, 3, 2));
        add(new Coord3D(1, 3, -2));
        add(new Coord3D(1, -3, 2));
        add(new Coord3D(1, -3, -2));
        add(new Coord3D(-1, 3, 2));
        add(new Coord3D(-1, 3, -2));
        add(new Coord3D(-1, -3, 2));
        add(new Coord3D(-1, -3, -2));

        add(new Coord3D(2, 1, 3));
        add(new Coord3D(2, 1, -3));
        add(new Coord3D(2, -1, 3));
        add(new Coord3D(2, -1, -3));
        add(new Coord3D(-2, 1, 3));
        add(new Coord3D(-2, 1, -3));
        add(new Coord3D(-2, -1, 3));
        add(new Coord3D(-2, -1, -3));

        add(new Coord3D(2, 3, 1));
        add(new Coord3D(2, 3, -1));
        add(new Coord3D(2, -3, 1));
        add(new Coord3D(2, -3, -1));
        add(new Coord3D(-2, 3, 1));
        add(new Coord3D(-2, 3, -1));
        add(new Coord3D(-2, -3, 1));
        add(new Coord3D(-2, -3, -1));

        add(new Coord3D(3, 2, 1));
        add(new Coord3D(3, 2, -1));
        add(new Coord3D(3, -2, 1));
        add(new Coord3D(3, -2, -1));
        add(new Coord3D(-3, 2, 1));
        add(new Coord3D(-3, 2, -1));
        add(new Coord3D(-3, -2, 1));
        add(new Coord3D(-3, -2, -1));

        add(new Coord3D(3, 1, 2));
        add(new Coord3D(3, 1, -2));
        add(new Coord3D(3, -1, 2));
        add(new Coord3D(3, -1, -2));
        add(new Coord3D(-3, 1, 2));
        add(new Coord3D(-3, 1, -2));
        add(new Coord3D(-3, -1, 2));
        add(new Coord3D(-3, -1, -2));
    }};
    
    public static Coord3D rotateAroundOrigin(Coord3D rotation, Coord3D point) {
        int[] coordVector = {point.x, point.y, point.z};
        
        int x = coordVector[Math.abs(rotation.x) - 1] * (rotation.x > 0 ? 1 : -1);
        int y = coordVector[Math.abs(rotation.y) - 1] * (rotation.y > 0 ? 1 : -1);
        int z = coordVector[Math.abs(rotation.z) - 1] * (rotation.z > 0 ? 1 : -1);
        return new Coord3D(x, y, z);
    }

    private static class Scanner {
        private List<Coord3D> observedBeacons = new ArrayList<>();
        private Coord3D position;

        private void add(Coord3D beacon) {
            observedBeacons.add(beacon);
        }

        public List<Coord3D> getObservedBeacons() {
            return observedBeacons;
        }

        public void translateAllBeacons(Coord3D rotation, Coord3D offset) {
            for (Coord3D beacon : observedBeacons) {
                beacon.set(rotateAroundOrigin(rotation, beacon));
                beacon.x += offset.x;
                beacon.y += offset.y;
                beacon.z += offset.z;
            }
        }

        public void setPosition(Coord3D position) {
            this.position = new Coord3D(position.x, position.y, position.z);
        }

        public Coord3D getPosition() {
            return position;
        }
    }

    private static Coord3D parseInput(String line) {
        String[] tokens = line.split(",");
        return new Coord3D(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
    }

    private static int getMaxOverlaps(Scanner scannerA, Scanner scannerB, Coord3D outRotation, Coord3D outOffset) {
        // Among all possible relative positions and rotations, get the maximum number of overlaps

        //Coord3D offset = null;

        // Compare each pair of trios to find an orientation where there are 12 points in common.
        // 1. Select one trio of points from each set
        // 2. Arrange all points relative to these points
        // 3. See how many points are common to both sets

        List<Coord3D> aBeacons = scannerA.getObservedBeacons();
        List<Coord3D> bBeacons = scannerB.getObservedBeacons();

        int maxShared = 0;

        for (int r=0; r<validRotations.size(); r++) {
            Coord3D rotation = validRotations.get(r);
            for (int a_0 = 0; a_0 < aBeacons.size(); a_0++) {
                Coord3D a_0_coord = aBeacons.get(a_0);
                for (int b_0 = 0; b_0 < bBeacons.size(); b_0++) {
                    Coord3D b_0_coord = rotateAroundOrigin(rotation, bBeacons.get(b_0));
                    Coord3D offset = new Coord3D(a_0_coord.x - b_0_coord.x, a_0_coord.y - b_0_coord.y, a_0_coord.z - b_0_coord.z);

                    int numShared = 0;
                    for (int a_n = 0; a_n < aBeacons.size(); a_n++) {
                        Coord3D a_n_coord = aBeacons.get(a_n);
                        for (int b_n = 0; b_n < bBeacons.size(); b_n++) {
                            Coord3D b_n_coord = rotateAroundOrigin(rotation, bBeacons.get(b_n));
                            b_n_coord.x += offset.x;
                            b_n_coord.y += offset.y;
                            b_n_coord.z += offset.z;

                            if (a_n_coord.equals(b_n_coord)) {
                                numShared++;
                            }
                        }
                    }

                    if (numShared > maxShared) {
                        maxShared = numShared;
                        outRotation.set(rotation);
                        outOffset.set(offset);
                    }

                    if (numShared >= 12) {
                        return numShared;
                    }
                }
            }
        }

        return maxShared;
    }
    
    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day19/input.txt");
        
        List<Scanner> openScanners = new ArrayList<>();
        List<Scanner> closedScanners = new ArrayList<>();

        Scanner scanner = null;
        for (String line : lines) {
            if (line.contains("scanner")) {
                scanner = new Scanner();
            } else if (line.contains(",")) {
                scanner.add(parseInput(line));
            } else {
                openScanners.add(scanner);
            }
        }

        if (!openScanners.contains(scanner)) {
            openScanners.add(scanner);
        }

        // Absolute positions recorded relative to Scanner 0's position and rotation
        closedScanners.add(openScanners.remove(0));
        closedScanners.get(0).setPosition(new Coord3D(0, 0, 0));

        Coord3D rotation = new Coord3D(0, 0, 0);
        Coord3D offset = new Coord3D(0, 0, 0);

        while (!openScanners.isEmpty()) {
            Scanner scannerB = null;
            boolean hasPair = false;
            
            for (int i=0; i<closedScanners.size(); i++) {
                for (int j=0; j<openScanners.size(); j++) {
                    if (getMaxOverlaps(closedScanners.get(i), openScanners.get(j), rotation, offset) >= 12) {
                        scannerB = openScanners.get(j);
                        hasPair = true;
                        break;
                    }
                }
                if (hasPair) {
                    break;
                }
            }

            if (hasPair) {
                scannerB.translateAllBeacons(rotation, offset);
                scannerB.setPosition(offset);
                openScanners.remove(scannerB);
                closedScanners.add(scannerB);
                System.out.println("Closed scanners: " + closedScanners.size());
            } else {
                System.err.println("No pair found.");
                System.exit(1);
            }

        }

        // System.out.println(getMaxOverlaps(scanners.get(1), scanners.get(4)));

        // Absolute positions recorded relative to Scanner 0's position and rotation
        /*Set<Coord3D> absoluteBeacons = new HashSet<>();
        for (Scanner closedScanner : closedScanners) {
            absoluteBeacons.addAll(closedScanner.getObservedBeacons());
        }
        
        System.out.println(absoluteBeacons.size());*/

        int maxDistance = Integer.MIN_VALUE;
        for (Scanner a : closedScanners) {
            for (Scanner b : closedScanners) {
                int distance = 0;
                distance += Math.abs(a.getPosition().x - b.getPosition().x);
                distance += Math.abs(a.getPosition().y - b.getPosition().y);
                distance += Math.abs(a.getPosition().z - b.getPosition().z);
                maxDistance = Math.max(maxDistance, distance); 
            }   
        }

        System.out.println(maxDistance);

    }
}
