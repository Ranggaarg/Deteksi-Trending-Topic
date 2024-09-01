/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.ui;

/**
 *
 * @author Dosen
 */
import java.util.*;

import java.util.ArrayList;
import data.ui.Ngrams;

public class generateBigram {

	public static void main(String[] args) {
		String text = "These are some words";
		ArrayList<String> words = Ngrams.sanitiseToWords(text);
		ArrayList<String> ngrams = Ngrams.ngrams(words, 3);
		
		System.out.println(ngrams.toString());
	}

 
}
