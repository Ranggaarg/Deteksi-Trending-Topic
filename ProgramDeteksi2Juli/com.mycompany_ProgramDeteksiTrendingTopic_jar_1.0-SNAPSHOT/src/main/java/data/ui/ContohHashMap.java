/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package data.ui;

/**
 *
 * @author Dosen
 */
import java.util.HashMap;

public class ContohHashMap {
  public static void main(String[] args) {
    // Create a HashMap object called capitalCities
    HashMap<String, String> capitalCities = new HashMap<String, String>();

    // Add keys and values (Country, City)
    capitalCities.put("England", "London");
    capitalCities.put("Germany", "Berlin");
    capitalCities.put("England", "London");
    capitalCities.put("USA", "Washington DC");
    capitalCities.remove("England");
    
    System.out.println(capitalCities.get("USA"));
  }
}
