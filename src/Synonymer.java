import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Callable;

public class Synonymer implements Callable {
    private String[] inputWordArray;
    private int chanceToSkip;

    public Synonymer(String[] inputWordArray, int chanceToSkip){
        this.inputWordArray = inputWordArray;
        this.chanceToSkip = chanceToSkip;
    }

    public Synonymer(String[] inputWordArray){
        this.inputWordArray = inputWordArray;
        this.chanceToSkip = 0;
    }

    public Synonymer(){

    }

    @Override
    public String[] call() throws Exception {
        return synomizeArray(inputWordArray);
    }

    public String[] synomizeArray(String[] input) throws IOException {
        String[] result = new String[input.length];
        for(int i = 0; i<input.length; i++){
            String[] synonymsArray = searchThesaurus(input[i], chanceToSkip);
            result[i] = chooseRandomSynonymFromArray(synonymsArray);
        }
        return result;
    }

    public String synomizeWord(String input) throws IOException {
        return chooseRandomSynonymFromArray(searchThesaurus(input, chanceToSkip));
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

    private String chooseRandomSynonymFromArray(String[] synonymArray){
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

    public void setChanceToSkip(int chanceToSkip) {
        this.chanceToSkip = chanceToSkip;
    }

    public void setInputWordArray(String[] inputWordArray) {
        this.inputWordArray = inputWordArray;
    }
}
