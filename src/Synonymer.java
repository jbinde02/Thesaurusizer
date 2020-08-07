import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.Callable;

public class Synonymer implements Callable {
    String[] inputWordArray;
    int chanceToSkip;

    public Synonymer(String[] inputWordArray, int chanceToSkip){
        this.inputWordArray = inputWordArray;
        this.chanceToSkip = chanceToSkip;
    }

    @Override
    public String[] call() throws Exception {
        String[] result = new String[inputWordArray.length];
        for(int i = 0; i<inputWordArray.length; i++){
            String word = inputWordArray[i];
            String[] synonymsArray = searchThesaurus(word, chanceToSkip);
            result[i] = chooseRandomSynonym(synonymsArray);
        }
        return result;
    }

    public String[] searchThesaurus(String input, int chanceToSkip) throws IOException {
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

    public String chooseRandomSynonym(String[] synonymArray){
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
}
