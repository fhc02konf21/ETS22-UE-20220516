package org.campus02;

import java.util.Comparator;

public class EinwohnerCamparator implements Comparator<Einwohner> {
    @Override
    public int compare(Einwohner e1, Einwohner e2) {
        // return <0: e1 wird vorangestellt
        // return >0: e1 wird hinten angereiht
        // return 0: beide gleich
        int res = Integer.compare(e2.getGeburtsjahr(), e1.getGeburtsjahr());
        if (res == 0) {
            res = e1.getName().compareTo(e2.getName());
        }
        return res;
    }
}
