package View;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ScatterPlotPanel extends JPanel {
    private List<Double> longitudes;
    private List<Double> latitudes;

    public ScatterPlotPanel(List<Double> longitudes, List<Double> latitudes) {
        this.longitudes = longitudes;
        this.latitudes = latitudes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Set rendering hints for better graphics quality
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Find min and max values
        double minLongitude = longitudes.stream().min(Double::compareTo).orElse(0.0);
        double maxLongitude = longitudes.stream().max(Double::compareTo).orElse(1.0);
        double minLatitude = latitudes.stream().min(Double::compareTo).orElse(0.0);
        double maxLatitude = latitudes.stream().max(Double::compareTo).orElse(1.0);

        // Draw axes
        g2d.drawLine(50, height - 50, 50, 50); // Y-axis
        g2d.drawLine(50, height - 50, width - 50, height - 50); // X-axis

        // Draw points
        for (int i = 0; i < longitudes.size(); i++) {
            int x = (int) ((longitudes.get(i) - minLongitude) / (maxLongitude - minLongitude) * (width - 100)) + 50;
            int y = (int) ((latitudes.get(i) - minLatitude) / (maxLatitude - minLatitude) * (height - 100)) + 50;
            y = height - y; // Invert y-coordinate for correct orientation

            g2d.fillOval(x - 3, y - 3, 6, 6); // Draw point
        }
    }
}
