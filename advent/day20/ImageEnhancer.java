package advent.day20;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import advent.common.util.Coord2D;
import advent.common.util.InputReader;

public class ImageEnhancer {
    
    private static class Image {
        private Set<Coord2D> pixels = new HashSet<>();

        private boolean frontierIsLit = false;

        private Coord2D minBounds = new Coord2D(Integer.MAX_VALUE, Integer.MAX_VALUE);

        private Coord2D maxBounds = new Coord2D(Integer.MIN_VALUE, Integer.MIN_VALUE);

        public void lightPixel(Coord2D pixel) {
            pixels.add(pixel);
            if (pixel.x < minBounds.x) {
                minBounds.x = pixel.x;
            }
            if (pixel.x > maxBounds.x) {
                maxBounds.x = pixel.x;
            }
            if (pixel.y < minBounds.y) {
                minBounds.y = pixel.y;
            }
            if (pixel.y > maxBounds.y) {
                maxBounds.y = pixel.y;
            }
        }

        private boolean isFrontierPixel(Coord2D pixel) {
            return pixel.x < minBounds.x || pixel.x > maxBounds.x || pixel.y < minBounds.y || pixel.y > maxBounds.y;
        }

        public int getEnhancementCode(Coord2D pixel) {
            StringBuilder builder = new StringBuilder();
            for (int x = pixel.x - 1; x <= pixel.x + 1; x++) {
                for (int y = pixel.y - 1; y <= pixel.y + 1; y++) {
                    Coord2D neighbor = new Coord2D(x, y);
                    char c;
                    if (isFrontierPixel(neighbor)) {
                        c = frontierIsLit ? '1' : '0';
                    } else {
                        c = pixels.contains(neighbor) ? '1' : '0';
                    }
                    
                    builder.append(c);
                }
            }

            return Integer.parseInt(builder.toString(), 2);
        }

        public void print() {
            for (int x = minBounds.x - 5; x <= maxBounds.x + 5; x++) {
                for (int y = minBounds.y - 5; y <= maxBounds.y + 5; y++) {
                    Coord2D pixel = new Coord2D(x, y);
                    char c;
                    if (isFrontierPixel(pixel)) {
                        c = frontierIsLit ? '#' : '.';
                    } else {
                        c = pixels.contains(pixel) ? '#' : '.';
                    }
                    System.out.print(c);
                }
                System.out.println();
            }
        }

        public int getNumLitPixels() {
            return pixels.size();
        }

        public Coord2D getMinBounds() {
            return minBounds;
        }

        public Coord2D getMaxBounds() {
            return maxBounds;
        }

        public boolean getFrontierIsLit() {
            return this.frontierIsLit;
        }

        public void setFrontierIsLit(boolean frontierIsLit) {
            this.frontierIsLit = frontierIsLit;
        }
    }

    private static class EnhancementAlgorithm {

        private Set<Integer> lightCodes = new HashSet<>();

        public EnhancementAlgorithm(String input) {
            for (int i=0; i<input.length(); i++) {
                if (input.charAt(i) == '#') {
                    lightCodes.add(i);
                }
            }
        }

        public Image enhance(Image originalImage) {
            Image enhancedImage = new Image();

            for (int x = originalImage.getMinBounds().x - 1; x <= originalImage.getMaxBounds().x + 1; x++) {
                for (int y = originalImage.getMinBounds().y - 1; y <= originalImage.getMaxBounds().y + 1; y++) {
                    Coord2D pixel = new Coord2D(x, y);
                    if (lightCodes.contains(originalImage.getEnhancementCode(pixel))) {
                        enhancedImage.lightPixel(pixel);
                    }
                }
            }

            if (lightCodes.contains(0)) {
                enhancedImage.setFrontierIsLit(!originalImage.getFrontierIsLit());
            }

            return enhancedImage;
        }
    }

    public static void main(String[] args) {
        List<String> lines = InputReader.readLinesFromInput("advent/day20/input.txt");
        
        EnhancementAlgorithm enhancer = new EnhancementAlgorithm(lines.get(0));
        Image image = new Image();

        for (int i=2; i<lines.size(); i++) {
            for (int j=0; j<lines.get(i).length(); j++) {
                if (lines.get(i).charAt(j) == '#') {
                    image.lightPixel(new Coord2D(i-2, j));
                }
            }
        }

        for (int i=0; i<50; i++) {
            image = enhancer.enhance(image);
        }
        System.out.println(image.getNumLitPixels());
    }
}
