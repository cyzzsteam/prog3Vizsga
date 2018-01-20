/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;


/**
 * Egy eredményt ír le
 * @author varga
 */
public class Eredmeny implements Comparable<Eredmeny> {
    private int pont;
    private long datum;


    public Eredmeny(int pont, long datum) {
        this.pont = pont;
        this.datum = datum;
    }


    

    public int getPont() {
        return pont;
    }

    public long getDatum() {
        return datum;
    }

    @Override
    public String toString() {
        return  pont + " pont, " + new java.sql.Date(datum);
    }

    @Override
    public int compareTo(Eredmeny o) {
        return o.pont-this.pont;
    }
    
}
