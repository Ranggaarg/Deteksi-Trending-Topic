/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package data.ui;

import data.ui.lib.Database;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import data.ui.Ngrams;
import java.util.ArrayList;
import java.math.*;
import java.util.HashMap;
/**
 *
 * @author Dosen
 */
public class Form_Tweet extends javax.swing.JFrame {

    /**
     * Creates new form Form_Tweet
     */
    Database dbsetting;
    String driver, database, user,pass;
    public HashMap<String, Float> dfidftMap;
    public Form_Tweet() {//konstruktor
        initComponents();
        dbsetting = new Database();
        driver = dbsetting.SettingPanel("DBDriver");
        database = dbsetting.SettingPanel("DBDatabase");
        user = dbsetting.SettingPanel("DBUsername");
        pass = dbsetting.SettingPanel("DBPassword");
        this.setSize(1000, 700);
        tblModel = getDefaultTabelModel(); // Inisialisasi tabel model
        tabel.setModel(tblModel);
        Tabel(tabel, new int[]{120, 180, 120, 120, 700});

        dfidftMap = new HashMap<>(); // Inisialisasi HashMap untuk DF-IDFT
        setDefaultTable();
        
      
    }
    
    public void simpanTabel(String ngrams, float dfidft, int timeslots) {
        dfidftMap.put(ngrams, dfidft);

        try{
           Class.forName(driver);
           Connection kon = DriverManager.getConnection(database, user, pass);
           Statement stt = kon.createStatement();
           String SQL = "insert into tb_ngrams(n_grams, dfidft, timeslots) "
                   + "values ('"+ngrams+"','"+dfidft+"','"+timeslots+"')";
           System.out.println("SQL = "+SQL);
           stt.executeUpdate(SQL);
           stt.close();
           kon.close();
        } catch(Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    public void setDefaultTable() {
     int jumkata=0,dfi=0,dfi_j=0;
    try {
        Class.forName(driver);
        Connection kon = DriverManager.getConnection(database, user, pass);
        Statement stt = kon.createStatement();
        String SQL = "SELECT * FROM tb_tweet";
        System.out.println("Executing query: " + SQL);
        ResultSet res = stt.executeQuery(SQL);
       // "Created At","Time Slots","ID Tweets","Text","Bigrams"
        String[] data = new String[5];
        while (res.next()) {
            data[0] = res.getString("created_at");
            data[1] = res.getString("time_slots");
            data[2] = res.getString("id_tweet");
            data[3] = res.getString("text");//isi kalimat tweet
            //create bigrams
            System.out.println("kalimat= "+res.getString("text"));//get kalimat dari kolom ke 3
            ArrayList<String> words = Ngrams.sanitiseToWords(res.getString("text"));
            ArrayList<String> ngrams = Ngrams.ngrams(words, 2);//ekstrak bigrams
            data[4] = ngrams.toString();
            /// loop perbaris utk cek kemunculan n-grams pada timeslots 1///
            int i=0;
            while(i<ngrams.size()){
              //ini cetak n-grams
                System.out.println("n-grams dfi-j= "+ngrams.get(i));
               
                dfi_j= HitungKemunculanKata_timeslots1(ngrams.get(i).toString());//get n-grams
                System.out.println(ngrams.get(i)+" = "+dfi_j);
            
                System.out.println("n-grams dfi= "+ngrams.get(i));
                dfi= HitungKemunculanKata_timeslots2(ngrams.get(i).toString());//get n-grams
                System.out.println(ngrams.get(i)+" = "+dfi);
               
                float hasil_dfidft= (float) 0.0;
                hasil_dfidft = Hitung_DF_IDFT(dfi_j,dfi,2, (float) 1.0);
                simpanTabel(ngrams.get(i).toString(), hasil_dfidft, 1);
                System.out.println("nilai DF-IDFT kata "+ngrams.get(i)+"="+hasil_dfidft);
              i++;  
            } // end while
            tblModel.addRow(data);
           
           System.out.println("=========================================");

        }
        res.close();
        stt.close();
        kon.close();
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        e.printStackTrace(); // Print full stack trace for debugging
    }
}
    public float Hitung_DF_IDFT(float dfi_j, float dfi, float t, float boost){//cek rumus df-idft
        float hasil_df_idft = (float) 0.0;
        try{
            System.out.println("dfi= "+dfi);
            System.out.println("dfi-j= "+dfi_j);
            System.out.println("t= "+t);
            System.out.println("boost= "+boost);
            float A = (float)(dfi+1);
            float B = (float) Math.log10((dfi_j/t)+1);
             hasil_df_idft = (float) (A/(B+1))* boost;
           }catch(Exception eror){
            System.out.println(eror.getMessage());
        }
         return hasil_df_idft;//hasil akhir nilai dfidft atau bobot setiap bigrams
   }
    public int HitungKemunculanKata_timeslots1(String kata){
        int jum=0;
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, pass);
            Statement stt = kon.createStatement();
            String SQL = "select count(*) as frekuensi_kata from tb_tweet "
                    + "where text like '%"+kata+"%' and created_at between "
                    + "'2024-06-16 04:18:44' and '2024-06-16 04:18:46'";
            System.out.println("SQL dfi-j= "+SQL);
            ResultSet res = stt.executeQuery(SQL);
            if(res.next()){
                jum = Integer.parseInt(res.getString("frekuensi_kata"));//konversi String ke int
             }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } return jum;
    }
    public int HitungKemunculanKata_timeslots2(String kata){
        int jum=0;
        try{
            Class.forName(driver);
            Connection kon = DriverManager.getConnection(database, user, pass);
            Statement stt = kon.createStatement();
            String SQL = "select count(*) as frekuensi_kata from tb_tweet "
                    + "where text like '%"+kata+"%' and created_at between "
                    + "'2024-06-16 04:20:00' and '2024-06-16 04:21:00'";
            System.out.println("SQL dfi= "+SQL);
            ResultSet res = stt.executeQuery(SQL);
            if(res.next()){
                jum = Integer.parseInt(res.getString("frekuensi_kata"));//konversi String ke int
             }
        }catch(Exception e){
            System.out.println(e.getMessage());
        } return jum;
    }
    
    
    private void Tabel(javax.swing.JTable tb, int lebar[]){
        tb.setAutoResizeMode(tb.AUTO_RESIZE_OFF);
        int kolom = tb.getColumnCount();//dapatkan jumlah kolom
        for(int i=0;i<kolom;i++){
            javax.swing.table.TableColumn tbc = tb.getColumnModel().getColumn(i);
            tbc.setPreferredWidth(lebar[i]);//lebar per kolom
            tb.setRowHeight(17);//lebar per baris
        }
    }
    private javax.swing.table.DefaultTableModel getDefaultTabelModel(){
        return new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{"Created At","Time Slots","ID Tweets","Text","Bigrams"}
        ){
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false
            };
            public boolean isCellEditable(int rowIndex, int columnIndex){
                return canEdit[columnIndex];
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Created At", "Time Slots", "Id Tweets", "Text", "Bigrams", "Tf Idft"
            }
        ));
        jScrollPane1.setViewportView(tabel);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 130, 840, 190);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("PROGRAM DETEKSI TRENDING TOPIC (DF-IDFT)");
        jLabel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(140, 30, 470, 60);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form_Tweet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form_Tweet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form_Tweet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form_Tweet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form_Tweet().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabel;
    // End of variables declaration//GEN-END:variables
private javax.swing.table.DefaultTableModel tblModel = getDefaultTabelModel(); 
}
