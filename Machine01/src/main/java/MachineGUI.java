import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.visual_recognition.v3.model.ClassifyOptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MachineGUI extends JFrame{
    private JTextArea textArea1;
    private JPanel panel1;
    private JScrollPane scrollArea;
    private JTextField textField1;
    private JButton button1;

    public MachineGUI() {

        panel1 = new JPanel();
        textField1 = new JTextField();
        button1 = new JButton();
        scrollArea = new JScrollPane(textArea1);
        textArea1 = new JTextArea();

        initializationWindows();
        initializationComponents();

    }


    private void initializationWindows() {
        this.setSize(750, 560);
        this.setLocationRelativeTo(null);
        this.setTitle("Visual Recognition Watson");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void initializationComponents() {
        initializationPanel();
        informationArea();
        initializationButtons();
        initializationText();
    }

    private void initializationPanel() {
        panel1.setLayout(null);
        this.getContentPane().add(panel1);
    }

    private void informationArea() {
        // information of visual
        textArea1.setOpaque(true);
        scrollArea.setBounds(new Rectangle(10,10,400,500));
        scrollArea.setVisible(true);
        panel1.add(scrollArea);
    }

    private void initializationButtons() {
        button1.setText("Add Url");
        button1.setBounds(550, 80, 100, 40);
        panel1.add(button1);
        ActionListener inputUrl = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Watson();
            }
        };
        button1.addActionListener(inputUrl);

    }

    private void initializationText() {
        textField1.setBounds(500, 160, 200, 30);
        textField1.getText();
        panel1.add(textField1);
    }

    private void Watson(){
        IamAuthenticator options = new IamAuthenticator("FPYNdle0dJvyFV1uXI6bA8EHmL_MnHs9mzx9Xl43NlRt");

        VisualRecognition service = new VisualRecognition("2018-03-19", options);
        //https://i.ibb.co/BsQDds2/1200px-Before-Machu-Picchu.jpg
        ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                .url(textField1.getText())
                .build();
        ClassifiedImages result = service.classify(classifyOptions).execute().getResult();
        System.out.println(result.toString());
        textArea1.setText(result.toString());
    }




}
