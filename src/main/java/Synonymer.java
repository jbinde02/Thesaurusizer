import java.util.Random;
import java.util.concurrent.Callable;

// Offline Thesaurus provided by https://github.com/zaibacu/thesaurus

public class Synonymer implements Callable {
    private String[] inputWordArray;
    private int chanceToSkip;
    private ThesaurusMap thesaurusMap;

    public Synonymer(String[] inputWordArray, int chanceToSkip, ThesaurusMap thesaurusMap){
        this.inputWordArray = inputWordArray;
        this.chanceToSkip = chanceToSkip;
        this.thesaurusMap = thesaurusMap;
    }

    @Override
    public String[] call() throws Exception {
        return synomizeArray(inputWordArray);
    }

    public String[] synomizeArray(String[] input){
        String[] result = new String[input.length];
        for(int i = 0; i<input.length; i++){
            if(randomChance(chanceToSkip)){
                result[i] = input[i];
                continue;
            }
            String[] synonymsArray = searchLocalThesaurus(input[i]);
            result[i] = chooseRandomSynonymFromArray(synonymsArray);
        }
        return result;
    }

    private String[] searchLocalThesaurus(String input){
        String[] synonyms = thesaurusMap.getSynonyms(input);
        if (synonyms!=null){
            return synonyms;
        }else {
            return new String[]{input, input};
        }
    }

    private String chooseRandomSynonymFromArray(String[] synonymArray){
        if(synonymArray.length == 1)
            return synonymArray[0];
        Random random = new Random();
        return synonymArray[random.nextInt(synonymArray.length - 1 )];
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
