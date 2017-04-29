package mee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.DirectoryChooser;

public class SearchController  {

    @FXML
    private JFXButton search;

    @FXML
    private JFXTextField txt1;

    @FXML
    private JFXButton browse;
    
    @FXML
    private JFXTextArea resultTextArea;
    
    DirectoryChooser dirchose = new DirectoryChooser();
    File file;
    
    public String fileName;
    public String fileSize;
    String Contents ;
    
File [] list_of_file;
    ArrayList<String> tokenn = new ArrayList<String>();
    ArrayList<String> Docs = new ArrayList<String>();
    HashMap <String ,document>index = new HashMap<String ,document>();
    
    // Exit Button Action
    public void exitButtonAction (ActionEvent event){
        System.exit(0);
    }
    
    @FXML
    public void browesfile(ActionEvent e) throws  Exception{
        
    	list_of_file = dirchose.showDialog(null).listFiles();
                 //list_of_file =   dirchose.setInitialDirectory(new File(" D:\\Faculty\\3 ( IS)\\Second-Term\\Informaton Retrive [Dr.Rashed Khalil]\\Section").listFiles());
                    
                        for (File fi : list_of_file){
                            Scanner s = new Scanner(fi);
                             
                            while (s.hasNext()){
                            
                            Contents = s.next();
                            StringTokenizer token = new StringTokenizer(Contents , "   ,%$&");
                            
                            while(token.hasMoreTokens()){
                                tokenn.add(token.nextToken());
                                Docs.add(fi.getName());
                           }
                        
                            }
                        }
                            
                        
                    //Deleting stop words from the array list
                           for (int i =0; i< tokenn.size();i++){
                            String word = tokenn.get(i);
                            boolean flag = true;
                        for (int j=0;j< StopWords.stopwords.length;j++){
                        if (word.equalsIgnoreCase(StopWords.stopwords[j]))
                        {
                            Docs.remove(i);
                            tokenn.remove(i);
                        }

                        }
                        
             }
                        
                    Doc_stammer(tokenn);
                    getindex();
    }
    public void Doc_stammer(ArrayList a) {
        char[] w = new char[501];
        PorterStemmer s = new PorterStemmer();

        for (int i = 0; i < a.size(); i++) {
            try {
                int var = 0;
                int ch = a.get(i).toString().charAt(var);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        var++;
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500) {
                            j++;
                        }
                        if (var == a.get(i).toString().length()) {
                            for (int c = 0; c < j; c++) {
                                s.add(w[c]);
                            }
                            s.stem();
                            a.set(i , s.toString());
                          //  portred.add(s.toString());
                            break;
                        } else {
                            ch = a.get(i).toString().charAt(var);
                        }
                    }
                }
                if (ch < 0) {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("stammer error");
            }
        }
    }                        

    	   
    	   
       public void searchButtonAction (ActionEvent event) throws  IOException{
    	 resultTextArea.clear();
    	   String Text = txt1.getText().toString();
                          ArrayList s =new ArrayList() ;
                          s.add(Text);
   	     String query = Quiry_stammer(s);
                        searchWord(query);
    	      	   
    	   }
       
        public  void searchWord(String query) {
        try {
          document result =(document) index.get(query);
          
           /* ArrayList<String> r1 = (ArrayList<String>) result.doc.keySet();
             ArrayList<String> r2 = (ArrayList<String>) result.doc.values(); */
           
       Collection c1  = result.doc.keySet(); //return keys 
            Collection c2 = result.doc.values(); //return values
        
            Iterator t1 = c1.iterator() ;
            Iterator t2 = c2.iterator() ;
                  
            while (t1.hasNext()){
                
           System.out.println( "Key of Term" +   t1.next().toString() + " \t \t   >> is  " + t2.next());
            }
            
        }
        catch(Exception e){
           
            System.out.println("No Result Found");
           
            }
            
        }
    
        
    public void getindex(){
        int idf = 1 ;
    for(int i= 0 ; i <tokenn.size(); i ++ ){
        document d = new document();
        for (int j = i ; j <tokenn.size(); j++){
            int w = 1;
        if (tokenn.get(i).equals(tokenn.get(j))){
        d.doc.put(Docs.get(j), 5);
        
        
         // idf didn't Compelet Yet 
         //  i made it static for runing only
     //   int idf=list_of_file.length-Docs.get(i).size();
             d.doc.put(Docs.get(i), w*idf);
              // System.out.print(Docs.get(i) +  " -> " +" "+w+""+idf+" "+ w*idf + "   ");
        
        w++;
        }
        
    
        }
        index.put(tokenn.get(i), d);
    }
    
    
    }
  
     public String Quiry_stammer(ArrayList a) {
        char[] w = new char[501];
        PorterStemmer s = new PorterStemmer();

        for (int i = 0; i < a.size(); i++) {
            try {
                int var = 0;
                int ch = a.get(i).toString().charAt(var);
                if (Character.isLetter((char) ch)) {
                    int j = 0;
                    while (true) {
                        var++;
                        ch = Character.toLowerCase((char) ch);
                        w[j] = (char) ch;
                        if (j < 500) {
                            j++;
                        }
                        if (var == a.get(i).toString().length()) {
                            for (int c = 0; c < j; c++) {
                                s.add(w[c]);
                            }
                            s.stem();

                            break;
                        } else {
                            ch = a.get(i).toString().charAt(var);
                        }
                    }
                }
                if (ch < 0) {
                    break;
                }
            } catch (Exception ex) {
                System.out.println("stammer error");
            }
        }
        return s.toString();
    }
  }
 
    
    	
  

    
  