# Thesaurusizer
Program inspired by OrionSuperman's ThesaurizeThisBot Reddit bot that replaces words in a post to synonyms.

### About
This programs purpose in life is to take a bunch of words and replace them with their synonyms. It is not very useful but it is some silly entertainment.
It features a simple UI with a text input area and a text output area. There is also a slider to add a random chance element to which words are transformed for the output.
The program reads from a locally stored JSONlines files representing a thesaurus. It adds each line into a hashmap and combines homonyms together for more randomness.
The outpus don't really make sense most of the time but that is how it is.

### Technologies
This program is written in Java and uses the Java core libraries for UI and collections. 

The only library used in this project is the json-simple library for Java found here:
* https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1

The offline thesaurus can be found here:
* https://github.com/zaibacu/thesaurus

### Installation
Make sure you have maven installed. Instructions and downloads found here:
* https://maven.apache.org/install.html

(Download and extract) or clone this repository to your device. Navigate to the folder in command prompt or terminal and run the following command.

-- **mvn install**

Navigate to the user_name\\.m2\repository\groupId\Thesaurusizer folderPath to it can be found near end of command's output.

You can then run the Thesaurusizer snapshot jar file.

Have fun!
