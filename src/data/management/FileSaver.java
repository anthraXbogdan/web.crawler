package data.management;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileSaver {
    ArrayList<String> validURLs;
    ArrayList<String> validTitles;
    JFileChooser fileChooser;

    public FileSaver(ArrayList<String> validURLs, ArrayList<String> validTitles, JFileChooser fileChooser) throws IOException {
        this.validURLs = validURLs;
        this.validTitles = validTitles;
        this.fileChooser = fileChooser;
    }

    public void writeToFile() {
        BufferedWriter bw = null;

        try {
            FileWriter fw = new FileWriter(fileChooser.getSelectedFile());
            bw = new BufferedWriter(fw);

            for (int i = 0; i < validURLs.size(); i++) {
                bw.write("Link: " + validURLs.get(i) + "\n" + "Title: " + validTitles.get(i) + "\n\n");
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException ignored) {
        }
        finally
        {
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedWriter"+ex);
            }
        }
    }

}
