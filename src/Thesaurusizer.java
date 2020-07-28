import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Thesaurusizer {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea resultArea, inputArea;
    private JScrollPane resultPane, inputPane;
    private JButton clearButton, submitButton;
    private Thesaurusizer(){
        // <Swing>
        frame = new JFrame("Thesaurusizer");
        frame.setSize(600,600);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        frame.add(mainPanel);

        inputArea = new JTextArea("Insert text...");
        inputArea.setLineWrap(true);
        inputPane = new JScrollPane(inputArea);
        final Dimension DIMENSION = new Dimension(300, 150);
        inputPane.setPreferredSize(DIMENSION);
        inputPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(inputPane, constraints);

        submitButton = new JButton("Submit");
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(submitButton, constraints);

        clearButton = new JButton("Clear");
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(clearButton, constraints);

        addActionListenersToButtons();

        resultArea = new JTextArea("");
        constraints.anchor = GridBagConstraints.CENTER;
        resultArea.setLineWrap(true);
        resultPane = new JScrollPane(resultArea);
        resultPane.setPreferredSize(DIMENSION);
        resultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(resultPane, constraints);

        frame.validate();

        // </Swing>

    }

    private void addActionListenersToButtons(){
        submitButton.addActionListener(e -> {
            String[] inputWords = inputArea.getText().split(" ");
            String[] synonyms = new String[0];

            for (String input : inputWords) {
                try {
                    synonyms = searchThesaurus(input);
                    resultArea.append(synonyms[0]+ " ");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        clearButton.addActionListener(e ->{
            resultArea.setText("");
            inputArea.setText("");
        });
    }

    private String[] searchThesaurus(String input) throws IOException {

        Document doc = Jsoup.connect("https://www.thesaurus.com/browse/" + input).get();
        String ele = doc.select(".et6tpn80").first().text();
        String[] result = ele.split(" ");
        return result;
    }

    public static void main(String[] args){
        Thesaurusizer th = new Thesaurusizer();
    }
}
