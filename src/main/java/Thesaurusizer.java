import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/*
Main class. Contains UI and controller code.
 */
public class Thesaurusizer {
    private JFrame frame;
    private JPanel mainPanel;
    private JTextArea resultArea, inputArea;
    private JScrollPane resultPane, inputPane;
    private JButton clearButton, submitButton;
    private JSlider chanceSlider;
    private final int CHANCE_TO_SKIP_MIN = 0;
    private final int CHANCE_TO_SKIP_MAX = 100;
    private final int CHANCE_TO_SKIP_INIT = 60;
    private int chanceToSkip = CHANCE_TO_SKIP_INIT;
    private ThesaurusMap thesaurusMap;
    private Thesaurusizer(){
        // <Swing>
        // <Frame>
        frame = new JFrame("Thesaurusizer");
        frame.setSize(1000,800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        // <Main Panel>

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        frame.add(mainPanel);
        // <Input Text Area and Pane>

        inputArea = new JTextArea("Insert text...");
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputPane = new JScrollPane(inputArea);
        final Dimension DIMENSION = new Dimension(600, 200);
        inputPane.setPreferredSize(DIMENSION);
        inputPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(inputPane, constraints);

        // <Submit Button>
        submitButton = new JButton("Submit");
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(submitButton, constraints);

        // <Chance to Skip Word Slider>
        chanceSlider = new JSlider(JSlider.HORIZONTAL, CHANCE_TO_SKIP_MIN, CHANCE_TO_SKIP_MAX, CHANCE_TO_SKIP_INIT);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(chanceSlider,constraints);
        chanceSlider.setPaintLabels(true);
        chanceSlider.setPaintTicks(true);
        chanceSlider.setSnapToTicks(true);
        chanceSlider.setMajorTickSpacing(20);
        chanceSlider.setMinorTickSpacing(5);
        chanceSlider.addChangeListener(e -> {
            setChanceToSkip(chanceSlider.getValue());
        });
        JLabel sliderLabel = new JLabel("Chance To Skip");
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainPanel.add(sliderLabel);

        // <Clear Result Area Button>
        clearButton = new JButton("Clear");
        constraints.anchor = GridBagConstraints.LINE_END;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainPanel.add(clearButton, constraints);


        // <Result Area and Pane>
        resultArea = new JTextArea("");
        constraints.anchor = GridBagConstraints.CENTER;
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultPane = new JScrollPane(resultArea);
        resultPane.setPreferredSize(DIMENSION);
        resultPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        constraints.gridx = 0;
        constraints.gridy = 2;
        mainPanel.add(resultPane, constraints);

        //<Add actions to buttons>
        addActionListenersToButtons();

        frame.validate();

        // </Swing>

        //Create thesaurus to pass around
        thesaurusMap = new ThesaurusMap();
    }

    private void addActionListenersToButtons(){
        submitButton.addActionListener(e -> {
            final int WORDS_PER_THREAD = 200;
            String[] inputWords = inputArea.getText().split(" ");
            String[][] splitInputWords = splitStringArray(inputWords, WORDS_PER_THREAD);
            FutureTask[] synonymTasks = new FutureTask[splitInputWords.length];

            resultArea.setText("");

            for(int i = 0; i<synonymTasks.length; i++){
                synonymTasks[i] = new FutureTask<Synonymer>(new Synonymer(splitInputWords[i], chanceToSkip, thesaurusMap));
            }

            for(FutureTask ft : synonymTasks){
                Thread t = new Thread(ft);
                t.start();
            }

            for(FutureTask ft : synonymTasks){
                try {
                    String[] result = (String[]) ft.get();
                    for(String s : result){
                        resultArea.append(s + " ");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        });

        clearButton.addActionListener(e ->{
            resultArea.setText("");
            inputArea.setText("");
        });
    }

    /**
     * Split a string array into multiple string arrays every x strings.
     * @param inputArray An array of Strings to be split
     * @param split The number of strings that will be in each resulting array
     * @return String[][] The input String array split into multiple smaller arrays based on the split paramerter
     */
    private String[][] splitStringArray(String[] inputArray, int split){
        String[][] splitWordArrays = new String[inputArray.length / split + 1][];
        String[] splitArray = new String[3];
        for(int i = 0; i<splitWordArrays.length; i++){
            if((inputArray.length - 1) > i * split + split - 1){
                splitArray = Arrays.copyOfRange(inputArray,i * split,i * split + split);
            }else{
                splitArray = Arrays.copyOfRange(inputArray,i * split, inputArray.length);
            }
            splitWordArrays[i] = splitArray;
        }
        return splitWordArrays;
    }

    private void setChanceToSkip(int chanceToSkip){
        this.chanceToSkip = chanceToSkip;
    }

    public static void main(String[] args){
        Thesaurusizer th = new Thesaurusizer();
    }
}
