package org.campus02;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;

public class EinwohnerManager {

    // für Testzwecke
    public static void main(String[] args) throws DataFileException {
        ArrayList<Einwohner> loadedEinwohner = new EinwohnerManager().load();
        //System.out.println(loadedEinwohner.get(188));

        // default Comparator (in dem Fall Interface Comparable) wird angewendet
        Collections.sort(loadedEinwohner);
        for (Einwohner einwohner : loadedEinwohner) {
            System.out.println(einwohner);
        }
    }

    /**
     * lade die Einwohner aus der Datei data/testdata-einwohner.csv
     *
     * @return eine Liste von Einwohner
     */
    public ArrayList<Einwohner> load() throws DataFileException {
        String filePath = "data/testdata-einwohner.csv";

        ArrayList<Einwohner> einwohnerList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            br.readLine(); // erste zeile wird gelesen, wollen sie aber nicht verarbeiten

            String line;
            while ((line = br.readLine()) != null) {
                // teste einlesen
                //System.out.println("line = " + line);

                String[] einwohnerData = line.split(";");
                int id = Integer.parseInt(einwohnerData[0]);
                String name = einwohnerData[1];
                String bundesland = einwohnerData[2];
                int geburtsjahr = Integer.parseInt(einwohnerData[3]);

                Einwohner einwohner = new Einwohner(id, name, bundesland, geburtsjahr);
                // testausgabe einwohner
                //System.out.println("einwohner = " + einwohner);

                einwohnerList.add(einwohner);
            }

            // sortiere die Liste mit EinwohnerComparator
            // Collections.sort liefert nichts retour -> die liste selbst wird bearbeitet.
            Collections.sort(einwohnerList, new EinwohnerCamparator());
            return einwohnerList;
        } catch (Exception e) {
            throw new DataFileException("Error", e);
        }
    }
}
