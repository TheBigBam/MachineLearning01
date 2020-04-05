import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MachineGUI extends JFrame{
    private JPanel mainUI;
    private JTabbedPane tabPanel;
    private JTextField txtURL;
    private JTextPane txtOutput;
    private JButton btnSearch;
    private JLabel lblImage;
    private JPanel tabMain;
    private JPanel tabParametros;
    private JTextField txtKey;
    private JButton btnNew;
    JFrame frame = new JFrame("Bar Chart");
    barGraph chart;
    List<Score> myList;
    
    public MachineGUI(String appName) {
        this.setTitle(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainUI);
        this.setSize(1040, 680);

        lblImage.setIcon(reDimensionImage(imageProcesor("https://i.ibb.co/VYqvBQy/White-square.jpg")));

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Watson();
                //ImageIcon icon = new ImageIcon(imageProcesor());
                lblImage.setIcon(reDimensionImage(imageProcesor(txtURL.getText())));
            }
        });
        btnNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.setVisible(false);
                txtOutput.setText("");
                txtURL.setText("");
                frame.remove(chart);
                lblImage.setIcon(reDimensionImage(imageProcesor("https://i.ibb.co/VYqvBQy/White-square.jpg")));
            }
        });
        txtURL.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                txtURL.setText("");
            }
        });
    }

    public ImageIcon reDimensionImage(Image img){
        Image dimg = img.getScaledInstance(340, 340, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(dimg);
        return icon;
    }

    public Image imageProcesor(String url) {
        Image image = null;
        try {
            image = ImageIO.read(new URL(url));
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void Watson(){
        try {
            IamAuthenticator options = new IamAuthenticator(txtKey.getText());

            VisualRecognition service = new VisualRecognition("2018-03-19", options);
            //https://i.ibb.co/BsQDds2/1200px-Before-Machu-Picchu.jpg
            //https://i.ibb.co/JtXf5wk/circuit.jpg
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .url(txtURL.getText())
                    .build();
            ClassifiedImages result = service.classify(classifyOptions).execute().getResult();
            parseText(result);
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public void parseText(ClassifiedImages result){

        JSONObject body = new JSONObject(result.toString());
        JSONArray arr = body.getJSONArray("images").getJSONObject(0).getJSONArray("classifiers").getJSONObject(0).getJSONArray("classes");

        myList = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            float sco = arr.getJSONObject(i).getFloat("score");
            String name = arr.getJSONObject(i).getString("class");
            Score score = new Score(name, sco, randomColor());
            myList.add(score);
        }

        myList.sort(Comparator.comparing(Score::getScore).reversed());
        chart = new barGraph(myList);

        for (Score score : myList) {
            DecimalFormat df = new DecimalFormat("###.##");
            txtOutput.setText(txtOutput.getText() + "\n" + "      - " + score.name + " - " + df.format(score.score*100) + "%");
        }
        
        frame.getContentPane().add(chart);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocation(580, 335);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }

    public Color randomColor(){
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        Color randomColor = new Color(r, g, b);
        return randomColor;
    }

    public class Score{
        public String name;
        public float score;
        public Color color;

        public Score(String n, float s, Color c){
            name = n;
            score = s;
            color = c;
        }
        
        public float getScore() {
            return score;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new MachineGUI("Image Viewer");
        frame.setVisible(true);
    }
}
