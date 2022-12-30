import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import org.json.simple.*;
import org.json.simple.parser.*;

public class ThesaurusMap {
    private HashMap<String, Word> thesarusMap = new HashMap<>();

    public ThesaurusMap(){
        populateMap();
    }

    private void populateMap(){
        InputStream stream = getClass().getResourceAsStream("en_thesaurus.jsonl");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        JSONParser jp = new JSONParser();
        try{
            String line;
            while((line=reader.readLine())!=null){
                JSONObject jo = (JSONObject) jp.parse(line);
                String word = (String)jo.get("word");
                String key = word;
                String pos = (String)jo.get("pos");
                JSONArray ja = (JSONArray)jo.get("synonyms");
                String[] synonyms = new String[ja.size()];
                for (int i = 0; i<synonyms.length; i++){
                    synonyms[i] = (String)ja.get(i);
                }

                /*
                Combine all synonyms for Homonyms to just make things easier for working with the hashmap and to
                provide a wee bit of comedy.
                 */
                if(thesarusMap.containsKey(word)){
                    thesarusMap.get(word).addSynonyms(synonyms);
                }else{
                    thesarusMap.put(key, new Word(word, pos, synonyms));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public String[] getSynonyms(String word){
        if (thesarusMap.containsKey(word)){
            return thesarusMap.get(word).getSynonyms();
        }else{
            return null;
        }
    }

    private class Word{
        private String word;
        private String pos;
        private String[] synonyms;

        public Word(String word, String pos, String[] synonyms){
            this.word = word;
            this.pos = pos;
            this.synonyms = synonyms;
        }

        /*
        This is used to add more synonyms to a existing word.
         */
        public void addSynonyms(String[] extraSynonyms){
            String[] newSynonymsArray = Arrays.copyOf(synonyms, synonyms.length + extraSynonyms.length);
            System.arraycopy(extraSynonyms,0, newSynonymsArray, synonyms.length, extraSynonyms.length);
            synonyms = newSynonymsArray;
        }

        public String getWord() {
            return word;
        }

        public String getPos() {
            return pos;
        }

        public String[] getSynonyms() {
            return synonyms;
        }

        @Override
        public String toString() {
            return "Word{" +
                    "word='" + word + '\'' +
                    ", pos='" + pos + '\'' +
                    ", synonyms=" + Arrays.toString(synonyms) +
                    '}';
        }
    }
}
