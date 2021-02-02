import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class ex11_grayscale {
    public static void main(String[] args) throws Exception {
        BufferedImage img = ImageIO.read(new File(args[0]));
        BufferedImage new_img = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        WritableRaster raster = img.getRaster();
        WritableRaster newRaster = new_img.getRaster();

        int width = img.getWidth();
        int height = img.getHeight();

        int processors = Runtime.getRuntime().availableProcessors();
        List<GrayScaler> tasks = new ArrayList<>();
        for (int i = 0; i < processors - 1; i++) {
            GrayScaler task = new GrayScaler(
                    raster,
                    newRaster,
                    width,
                    height,
                    i * (width / processors),
                    (i + 1) * (width / processors)
            );
            tasks.add(task);
        }
        GrayScaler task = new GrayScaler(
                raster,
                newRaster,
                width,
                height,
                (processors - 1) * (width / processors),
                width
        );
        tasks.add(task);

        ExecutorService executor = Executors.newFixedThreadPool(processors);
        executor.invokeAll(tasks);
        executor.shutdown();

        ImageIO.write(new_img, "png", new File(args[1]));
    }
}

class GrayScaler implements Callable<Void> {

    private final WritableRaster raster;
    private final WritableRaster newRaster;
    private final int height;
    private final int startX;
    private final int endX;

    public GrayScaler(
            WritableRaster raster,
            WritableRaster newRaster,
            int width,
            int height,
            int startX,
            int endX
    ) {
        this.raster = raster;
        this.newRaster = newRaster;
        this.height = height;
        this.startX = startX;
        this.endX = endX;
    }

    @Override
    public Void call() throws Exception {
        for (int x = startX; x < endX; x++) {
            for (int y = 0; y < height; y++) {
                double R = raster.getSample(x, y, 0);
                double G = raster.getSample(x, y, 1);
                double B = raster.getSample(x, y, 2);
                double level = 0.3 * R + 0.59 * G + 0.11 * B;

                newRaster.setSample(x, y, 0, level);
                newRaster.setSample(x, y, 1, level);
                newRaster.setSample(x, y, 2, level);
            }
        }
        return null;
    }
}