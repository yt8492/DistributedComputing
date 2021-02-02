import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class FadeToGray extends RecursiveAction {

    private static final int MAX_PIXEL = 100;

    private final WritableRaster raster;
    private final WritableRaster newRaster;
    private final int startX;
    private final int endX;
    private final int startY;
    private final int endY;

    public FadeToGray(WritableRaster raster, WritableRaster newRaster, int startX, int endX, int startY, int endY) {
        this.raster = raster;
        this.newRaster = newRaster;
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
    }

    @Override
    protected void compute() {
        if (endX - startX < MAX_PIXEL && endY - startY < MAX_PIXEL) {
            for (int x = startX; x < endX; x++) {
                for (int y = startY; y < endY; y++) {
                    double R = raster.getSample(x, y, 0);
                    double G = raster.getSample(x, y, 1);
                    double B = raster.getSample(x, y, 2);
                    double level = 0.3 * R + 0.59 * G + 0.11 * B;

                    newRaster.setSample(x, y, 0, level);
                    newRaster.setSample(x, y, 1, level);
                    newRaster.setSample(x, y, 2, level);
                }
            }
        }
        List<FadeToGray> tasks = new ArrayList<>();
        if (endX - startX >= MAX_PIXEL) {
            FadeToGray task1 = new FadeToGray(
                    raster,
                    newRaster,
                    startX,
                    (startX + endX) / 2,
                    startY,
                    endY
            );
            FadeToGray task2 = new FadeToGray(
                    raster,
                    newRaster,
                    (startX + endX) / 2,
                    endX,
                    startY,
                    endY
            );
            tasks.add(task1);
            tasks.add(task2);
        }
        if (endY - startY >= MAX_PIXEL) {
            FadeToGray task1 = new FadeToGray(
                    raster,
                    newRaster,
                    startX,
                    endX,
                    startY,
                    (startY + endY) / 2
            );
            FadeToGray task2 = new FadeToGray(
                    raster,
                    newRaster,
                    startX,
                    endX,
                    (startY + endY) / 2,
                    endY
            );
            tasks.add(task1);
            tasks.add(task2);
        }
        invokeAll(tasks);
    }

    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File(args[0]));
        BufferedImage new_img = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        WritableRaster raster = img.getRaster();
        WritableRaster newRaster = new_img.getRaster();

        int width = img.getWidth();
        int height = img.getHeight();

        FadeToGray task = new FadeToGray(
                raster,
                newRaster,
                0,
                width,
                0,
                height
        );
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(task);

        ImageIO.write(new_img, "png", new File(args[1]));
    }
}
