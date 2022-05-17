package org.campus02;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class EinwohnerHandler implements Runnable {

    private Socket client;
    private EinwohnerManager em;

    public EinwohnerHandler(Socket client) {
        this.client = client;
        this.em = new EinwohnerManager();
    }

    /**
     * die eigentliche Logik des Servers
     */
    public void start() throws DataFileException {
        ArrayList<Einwohner> einwohnerList = em.load();

        // listen an einwohner daten könnten auch hier aufgebaut werden.
        /*
        ArrayList<Einwohner> wienerInnen = new ArrayList<>();
        ArrayList<Einwohner> steirerInnen = new ArrayList<>();

        for (Einwohner einwohner : einwohnerList) {
            if (einwohner.getBundesland().equalsIgnoreCase("wien")) {
                wienerInnen.add(einwohner);
            } else if (einwohner.getBundesland().equalsIgnoreCase("steiermark")) {
                steirerInnen.add(einwohner);
            }
        }*/

        System.out.println("warte auf kommando von client");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

            String cmd;

            while ((cmd = br.readLine()) != null) {
                System.out.println("cmd = " + cmd);
                String[] cmds = cmd.split(" ");

                // mehrere Möglichkeiten die Logik abzufragen und umzusetzen
                if (cmds[0].equals("GET")) {
                    System.out.println("bearbeite anfrage");
                    // wenn cmds.length == 2 -> dann will der client einwohner eines bestimmten bundeslandes
                    if (cmds.length == 2) {
                        if (cmds[1].equalsIgnoreCase("wien")) {
                            searchBundesland(cmds[1], einwohnerList, bw);
                            bw.write("Einwohner Ende");
                            bw.newLine();
                            bw.flush();
                        } else if (cmds[1].equalsIgnoreCase("steiermark")) {
                            // finde die steirer
                        }
                    } else {
                        searchByGeburtsjahr(cmds[1], einwohnerList, bw);
                        bw.write("Einwohner Ende");
                        bw.newLine();
                        bw.flush();
                    }
                } else if (cmds[0].equalsIgnoreCase("exit")) {
                    client.close();
                } else {
                    bw.write("wrong input");
                    bw.newLine();
                    bw.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void searchByGeburtsjahr(String geburtsjahr, ArrayList<Einwohner> einwohnerList, BufferedWriter bw) throws IOException {
        int gj = Integer.parseInt(geburtsjahr);

        // duplikat erzeugen, sonst würde standardsortierung überschrieben werden
        // oder leere Liste erzeugen und befüllen -> vor Rückgabe die neue Liste sortieren
        ArrayList<Einwohner> duplicateEW = new ArrayList<>(einwohnerList);
        Collections.sort(duplicateEW);
        for (Einwohner einwohner : duplicateEW) {
            if (einwohner.getGeburtsjahr() == gj) {
                bw.write(einwohner.toString());
                bw.newLine();
            }
        }
    }

    private void searchBundesland(String bundesland, ArrayList<Einwohner> einwohnerList, BufferedWriter bw) throws IOException {
        for (Einwohner einwohner : einwohnerList) {
            // equalsIgnorCase -->
            // Wien == wien ==> true
            // wien == WIEN ==> true

            // equals
            // Wien == wien ==> false
            if (einwohner.getBundesland().equalsIgnoreCase(bundesland)) {
                //einwohners.add(einwohner);
                bw.write(einwohner.toString());
                bw.newLine();
            }
        }
    }

    @Override
    public void run() {
        try {
            start();
        } catch (DataFileException e) {
            e.printStackTrace();
        }
    }
}
