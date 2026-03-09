package com.daw.cinemadaw.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.daw.cinemadaw.domain.cinema.New;


public class NewsService {

    ArrayList<New> newlist = new ArrayList<>();



    
    public ArrayList<New> GetNews() throws FileNotFoundException{
        // Llegir un fitxer de text línia a línia
        File f = new File("news.txt");
        if (f.exists()) {
                // llegir l'arxiu
                Scanner lectorFitxer = new Scanner(f);
                String linia;
                while(lectorFitxer.hasNextLine()) {
                    linia = lectorFitxer.nextLine();
                    String[] camps = linia.split(":");
                    New n = new New(camps[0], camps[1]);
                    newlist.add(n);
                }
                lectorFitxer.close();
                return newlist;
        }
        else {
                System.out.println("No existeix l'arxiu");
                return null;
        }
    }

}
