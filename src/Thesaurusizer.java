import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

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
            int wordsPerThread = 10;

            String[][] splitInputWords = splitWordArray(inputWords, wordsPerThread);
            FutureTask[] synonymTasks = new FutureTask[splitInputWords.length];
            for(int i = 0; i<synonymTasks.length; i++){
                synonymTasks[i] = new FutureTask<Synonymer>(new Synonymer(splitInputWords[i], chanceToSkip));
            }

            for(FutureTask ft : synonymTasks){
                Thread t = new Thread(ft);
                t.start();
            }

            for(FutureTask ft : synonymTasks){
                try {
                    String[] result = (String[]) ft.get();
                    for(String s : result){
                        resultArea.append(s);
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

    private String[][] splitWordArray(String[] inputArray, int split){
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

    public static void main(String[] args){
        Thesaurusizer th = new Thesaurusizer();
    }
}
