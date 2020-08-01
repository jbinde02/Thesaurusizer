import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

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
            int chanceToSkip = 60;
            for (String input : inputWords) {
                try {
                    String[] synonymsArray = searchThesaurus(input, chanceToSkip);
                    resultArea.append(chooseRandomSynonym(synonymsArray));
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

    private String[] searchThesaurus(String input, int chanceToSkip) throws IOException {
        if(randomChance(chanceToSkip)){
            return new String[]{input, input};
        }
        String url = "https://www.thesaurus.com/browse/" + input;
        if(isValidURL(url)){
            Document doc = Jsoup.connect(url).get();
            String element = doc.select(".et6tpn80").first().text(); // ".et6tpn80" is the HTML Class where the synonyms are displayed
            return element.split(" ");
        }
        return new String[]{input, input};
    }

    private boolean isValidURL(String urlInput) throws IOException {
        URL url = new URL(urlInput);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        if(huc.getResponseCode() == 200){
            return true;
        }else{
            return false;
        }
    }

    private String chooseRandomSynonym(String[] synonymArray){
        Random random = new Random();
        return synonymArray[random.nextInt(synonymArray.length - 1 )]+ " ";
    }

    private boolean randomChance(int chance){
        Random random = new Random();
        if(chance>100){
            chance = 100;
        }else if(chance<0){
            chance = 0;
        }

        return chance >= random.nextInt(100);
    }

    public static void main(String[] args){
        Thesaurusizer th = new Thesaurusizer();
    }
}
