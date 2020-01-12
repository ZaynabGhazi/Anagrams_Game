/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    //added:
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String,ArrayList<String>> lettersToWord = new HashMap<>();
    private HashMap<Integer,ArrayList<String>> sizeToWords = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private int start=0;
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            //fill hashMap:
            String alphaWord = sortLetters(word);
            if (lettersToWord.containsKey(alphaWord)){
                lettersToWord.get(alphaWord).add(word);
            }
            else{
                ArrayList<String> val = new ArrayList<>();
                val.add(word);
                lettersToWord.put(alphaWord,val);
            }
            //fill wordlength hashMap:
            if (sizeToWords.containsKey(word.length())){
                sizeToWords.get(word.length()).add(word);
            }
            else{
                ArrayList<String> sizedWords= new ArrayList<>();
                sizedWords.add(word);
                sizeToWords.put(word.length(),sizedWords);
            }

        }
    }

    public boolean isGoodWord(String word, String base) {
        boolean state = true;
        if (!wordSet.contains(word)) state = false;
        if (word.contains(base)) state = false;
        return state;
    }

    public List<String> getAnagrams(String targetWord) {
        String trgtAlpha = sortLetters(targetWord);
        return lettersToWord.get(trgtAlpha);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String[] alphabetArr = {"a","b","c","d","e","f","g","h","e","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        for (int i=0; i< alphabetArr.length;i++){
            String newWord = word;
            newWord += alphabetArr[i];
            String alphaNewWord = sortLetters(newWord);
            if (lettersToWord.get(alphaNewWord) != null) result.addAll(lettersToWord.get(alphaNewWord));
        }
        return result;
    }
    //Two-letter mode:
    public List<String> getAnagramsWithTwoMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String[] alphabetArr = {"a","b","c","d","e","f","g","h","e","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
        for (int i=0; i< alphabetArr.length;i++){
            String newWord = word;
            newWord += alphabetArr[i];
            for(int j=0; j< alphabetArr.length;j++){
                String nnewWord= newWord;
                nnewWord+= alphabetArr[j];
                String alphaNewWord = sortLetters(nnewWord);
                if (lettersToWord.get(alphaNewWord) != null) result.addAll(lettersToWord.get(alphaNewWord));
            }
        }
        return result;
    }
    public String pickGoodStarterWord() {
        boolean found = false;
        String answer="";
        while (!found && wordLength < MAX_WORD_LENGTH) {
            //int start = 0;
            while ( start < sizeToWords.get(wordLength).size() && getAnagrams(sizeToWords.get(wordLength).get(start)).size() < MIN_NUM_ANAGRAMS ) {
                start++;
            }
            if (start < sizeToWords.get(wordLength).size()) {
                answer+= sizeToWords.get(wordLength).get(start);
                start++;
                found = true;
            }
            else {wordLength = wordLength + 1;
            start =0;
            }
        }
        return answer;
    }
    //helper-function for getAnagrams()
    private String sortLetters(String initial){
        char[] letters = initial.toUpperCase().toCharArray();
        String result = "";
        for (int i=0; i< initial.length()-1; i++){
            for (int j=i+1; j< initial.length(); j++){
                if (letters[i] > letters[j]){
                    char tempc = letters[i];
                    letters[i]= letters[j];
                    letters[j]=tempc;

                }
            }
        }
        for (int i=0; i< letters.length; i++){
            result += letters[i];
        }
        return result;
    }
}
