import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;


public class barGraph extends JPanel {
    private Map<Color, Float> bars;

    public barGraph(List<MachineGUI.Score> list){
        bars = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            addBar(list.get(i).color, list.get(i).score);
        }
    }

    public void addBar(Color color, float value) {
        bars.put(color, value);
        repaint();
    }



    @Override
    protected void paintComponent(Graphics g) {

        float max = Float.MIN_VALUE;
        for (Float value : bars.values()) {
            max = Math.max(max, value);
        }

        int width = (getWidth() / bars.size()) - 2;
        int x = 1;
        for (Color color : bars.keySet()) {
            float value = bars.get(color);
            int height = (int)
                    ((getHeight() - 5) * ((double) value / max));
            g.setColor(color);
            g.fillRect(x, getHeight() - height, width, height);
            g.setColor(Color.black);
            g.drawRect(x, getHeight() - height, width, height);
            x += (width + 2);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(bars.size() * 10 + 2, 50);
    }


}