/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package data.ui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dosen
 */
class GenerateBigrams2{
  public static List<String> ngrams(int n, String str) {
    List<String> ngrams = new ArrayList<String>();
    for (int i = 0; i < str.length() - n + 1; i++)
        // Add the substring or size n
        ngrams.add(str.substring(i, i + n));
        // In each iteration, the window moves one step forward
        // Hence, each n-gram is added to the list

    return ngrams;
  }

  public static void main(String args[]) {
      String s = "pemilu jokowi presiden";
      List<String> ngrams = ngrams(2, s);
      for (String ngram : ngrams){
        System.out.println(ngram);
      }
  }
}
