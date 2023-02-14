package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws IOException{

        //store list of files in directory
        File f = new File(args[0]);
        File[] files = f.listFiles();

        //variables
        Path p;
        File curFile;

        InputStream is;
        InputStreamReader isr;
        BufferedReader br;

        String line;
        String editedLine;
        Map<String, Map<String,Integer>> outerWordFreq = new HashMap<>();
        Integer countIter;

        //iterate through files in directory
        for (int i = 0; i < files.length; i++) {
            //System.out.println(files[i].getName());
            p = Paths.get(args[0]+"\\" + files[i].getName());
            curFile = p.toFile();

            //start new file input stream
            is = new FileInputStream(curFile);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr); 

            //establish counter to acknowledge first word in file
            countIter = 0;
            String lastWord = "";
            while ((line = br.readLine()) != null){
                //Replace all punctuations
                editedLine = line.replaceAll("[^a-zA-Z0-9 ]", "");
                //split into words
                String[] words = editedLine.split(" ");

                //Iterate all the words in the line
                for (String w: words){
                    //remove any spaces
                    String currWord = w.trim().toLowerCase();
                    
                    //recognise for empty string
                    if (currWord.length()<=0){
                        continue;
                    }

                    //first word of the file -> record last word as the current word and continue iteration
                    if (countIter == 0){
                        lastWord = currWord;
                        countIter++;
                        continue;
                    }else{
                        countIter++;
                        //check if outer Hashmap contains word
                        if (!outerWordFreq.containsKey(lastWord)){
                            //if word is not in map, initalize the current word with frequency of 1
                            outerWordFreq.put(lastWord,new HashMap<String, Integer>(){
                                {
                                put(currWord,1);
                                }
                            });
                        } else{
                            //if word is in map, then increment the count
                            Map<String, Integer> innerMap = outerWordFreq.get(lastWord);
                            //check if current word is in map and increment count accordingly
                            if(!innerMap.containsKey(currWord)){
                                innerMap.put(currWord,1);
                            }else{
                                int curCount = innerMap.get(currWord);
                                innerMap.put(currWord,curCount+1);
                                outerWordFreq.put(lastWord,innerMap);
                            }
                        }
                        lastWord = currWord;
                    }
                    
                    
                }
            }

            br.close();
            isr.close();
            is.close();

        }

        //Get a list of all the words to print out
        //outer hashmap set
        Set<String> words = outerWordFreq.keySet();
        for (String w: words){
            Map<String, Integer> innerMap = outerWordFreq.get(w);
            Set<String> innerWords = innerMap.keySet();
            //print out the outer words
            System.out.printf("%s \n",w);
            double dsum = 0.0;
            //get the sum of the occurrence of the next words first
            for (String iw: innerWords){
                int count = innerMap.get(iw);
                dsum+= count;
            }
            //calculate probability using sum calculated above and then print
            for (String iw: innerWords){
                double dcount = (double)innerMap.get(iw);
                System.out.printf("    %s %f\n",iw, dcount/dsum);
            }
        }
    }
}