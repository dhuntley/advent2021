package advent.day22;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import advent.common.util.Coord3D;
import advent.common.util.InputReader;

public class Reactor {

    private static class CuboidArea {
        Coord3D minBounds;
        Coord3D maxBounds;

        public CuboidArea(Coord3D minBounds, Coord3D maxBounds) {
            this.minBounds = minBounds;
            this.maxBounds = maxBounds;
        }

        public CuboidArea(CuboidArea other) {
            minBounds = new Coord3D(other.minBounds);
            maxBounds = new Coord3D(other.maxBounds);
        }

        public long getArea() {
            if (maxBounds.x < minBounds.x || maxBounds.y < minBounds.y || maxBounds.z < minBounds.z) {
                return 0;
            }
            return (long)(maxBounds.x - minBounds.x) * (long)(maxBounds.y - minBounds.y) * (long)(maxBounds.z - minBounds.z);
        }

        @Override
        public String toString() {
            return minBounds.toString() + " to " + maxBounds.toString();
        }

        // Returns true iff this area fully contains the other area
        public boolean contains(CuboidArea other) {
            return (minBounds.x <= other.minBounds.x) && (maxBounds.x >= other.maxBounds.x) &&
                (minBounds.y <= other.minBounds.y) && (maxBounds.y >= other.maxBounds.y) &&
                (minBounds.z <= other.minBounds.z) && (maxBounds.z >= other.maxBounds.z);
        }

        public static boolean getAreDisjunct(CuboidArea areaA, CuboidArea areaB) {
            return (areaA.minBounds.x <= areaB.minBounds.x && areaA.maxBounds.x <= areaB.minBounds.x) ||
                (areaA.minBounds.x >= areaB.maxBounds.x && areaA.maxBounds.x >= areaB.maxBounds.x) ||
                (areaA.minBounds.y <= areaB.minBounds.y && areaA.maxBounds.y <= areaB.minBounds.y) ||
                (areaA.minBounds.y >= areaB.maxBounds.y && areaA.maxBounds.y >= areaB.maxBounds.y) ||
                (areaA.minBounds.z <= areaB.minBounds.z && areaA.maxBounds.z <= areaB.minBounds.z) ||
                (areaA.minBounds.z >= areaB.maxBounds.z && areaA.maxBounds.z >= areaB.maxBounds.z);
        }

        public void trimToArea(CuboidArea other) {
            minBounds.x = Integer.max(minBounds.x, other.minBounds.x);
            maxBounds.x = Integer.min(maxBounds.x, other.maxBounds.x);
            minBounds.y = Integer.max(minBounds.y, other.minBounds.y);
            maxBounds.y = Integer.min(maxBounds.y, other.maxBounds.y);
            minBounds.z = Integer.max(minBounds.z, other.minBounds.z);
            maxBounds.z = Integer.min(maxBounds.z, other.maxBounds.z);
        }

        public static List<CuboidArea> getSubAreas(CuboidArea areaA, CuboidArea areaB) {
            List<CuboidArea> subAreas = new ArrayList<>();
            
            // Sweep by dimension
            int[] xBounds = {areaA.minBounds.x, areaB.minBounds.x, areaA.maxBounds.x, areaB.maxBounds.x};
            int[] yBounds = {areaA.minBounds.y, areaB.minBounds.y, areaA.maxBounds.y, areaB.maxBounds.y};
            int[] zBounds = {areaA.minBounds.z, areaB.minBounds.z, areaA.maxBounds.z, areaB.maxBounds.z};

            Arrays.sort(xBounds);
            Arrays.sort(yBounds);
            Arrays.sort(zBounds);

            for (int x=0; x < xBounds.length-1; x++) {
                int minX = xBounds[x];
                int maxX = xBounds[x+1];

                for (int y=0; y < yBounds.length-1; y++) {
                    int minY = yBounds[y];
                    int maxY = yBounds[y+1];

                    for (int z=0; z < zBounds.length-1; z++) {
                        int minZ = zBounds[z];
                        int maxZ = zBounds[z+1];

                        subAreas.add(new CuboidArea(new Coord3D(minX, minY, minZ), new Coord3D(maxX, maxY, maxZ)));
                    }
                }
            }

            return subAreas;
        }

        public static List<CuboidArea> getDifference(CuboidArea areaA, CuboidArea areaB) {
            List<CuboidArea> areas = new ArrayList<>();

            if (getAreDisjunct(areaA, areaB)) {
                areas.add(new CuboidArea(areaA));
            } else {
                getSubAreas(areaA, areaB).forEach(subArea -> {
                    if (areaA.contains(subArea) && !areaB.contains(subArea)) {
                        areas.add(subArea);
                    }
                });
            }

            return areas;
        }

        /*public static List<CuboidArea> getUnion(CuboidArea areaA, CuboidArea areaB) {
            List<CuboidArea> areas = new ArrayList<>();

            if (getAreDisjunct(areaA, areaB)) {
                areas.add(new CuboidArea(areaA));
                areas.add(new CuboidArea(areaB));
            } else {
                getSubAreas(areaA, areaB).forEach(subArea -> {
                    if (areaA.contains(subArea) || areaB.contains(subArea)) {
                        areas.add(subArea);
                    }
                });
            }

            return areas;
        }*/

        /*public static List<CuboidArea> getIntersection(CuboidArea areaA, CuboidArea areaB) {
            List<CuboidArea> areas = new ArrayList<>();

            if (!getAreDisjunct(areaA, areaB)) {
                getSubAreas(areaA, areaB).forEach(subArea -> {
                    if (areaA.contains(subArea) && areaB.contains(subArea)) {
                        areas.add(subArea);
                    }
                });
            }

            return areas;
        }*/
    }

    private static class CompositeArea {
        // Compontent areas are guaranteed to be mutually disjunct
        private List<CuboidArea> components = new ArrayList<>();
        
        public void addArea(CuboidArea other) {
            if (components.isEmpty()) {
                components.add(new CuboidArea(other));
                return;
            }

            List<CuboidArea> addingComponents = new ArrayList<>();

            addingComponents.add(other);

            for (CuboidArea oldComponent : components) {
                List<CuboidArea> remainderComponents = new ArrayList<>();

                for (CuboidArea addingComponent : addingComponents) {
                    remainderComponents.addAll(CuboidArea.getDifference(addingComponent, oldComponent));
                }

                addingComponents = remainderComponents;
            }
            
            components.addAll(addingComponents);
        }

        public void subtractArea(CuboidArea other) {
            List<CuboidArea> updatedComponents = new ArrayList<>();

            for (CuboidArea oldComponent : components) {
                updatedComponents.addAll(CuboidArea.getDifference(oldComponent, other));
            }
            
            components = updatedComponents;
        }

        public void trimToArea(CuboidArea other) {
            for (CuboidArea component : components) {
                component.trimToArea(other);
            }
        }

        public long getArea() {
            return components.stream().collect(Collectors.summingLong(CuboidArea::getArea));
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day22/input.txt");

        CompositeArea area = new CompositeArea();

        for (String line : lines) {
            String[] tokens = line.split("\\s|,");
            String[] xTokens = tokens[1].split("x=|\\..");
            String[] yTokens = tokens[2].split("y=|\\..");
            String[] zTokens = tokens[3].split("z=|\\..");

            int minX = Integer.parseInt(xTokens[1]);
            int maxX = Integer.parseInt(xTokens[2]);
            int minY = Integer.parseInt(yTokens[1]);
            int maxY = Integer.parseInt(yTokens[2]);
            int minZ = Integer.parseInt(zTokens[1]);
            int maxZ = Integer.parseInt(zTokens[2]);

            CuboidArea modArea = new CuboidArea(new Coord3D(minX, minY, minZ), new Coord3D(maxX + 1, maxY + 1, maxZ + 1));
            //System.out.println(modArea.toString());

            if (tokens[0].equals("on")) {
                area.addArea(modArea);
            } else {
                area.subtractArea(modArea);
            }
            //System.out.println(line);
        }

        //CuboidArea calcArea = new CuboidArea(new Coord3D(-50, -50, -50), new Coord3D(51, 51, 51));
        //area.trimToArea(calcArea);

        System.out.println(area.getArea());
    }
}
