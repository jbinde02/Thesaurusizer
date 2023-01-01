import java.util.Random;

// Offline Thesaurus provided by https://github.com/zaibacu/thesaurus

public class Synonymer{
    private ThesaurusMap thesaurusMap;

    public Synonymer(ThesaurusMap thesaurusMap){
        this.thesaurusMap = thesaurusMap;
    }

    public String[] synomizeArray(String[] input, int chanceToSkip){
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
