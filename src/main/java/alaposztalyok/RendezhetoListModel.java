/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package alaposztalyok;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractListModel;

/**
 * Saját, rendezhető listamodell.
 * Bármely Comparable típusra alkalmazható.
 * @param <T> 
 */
public class RendezhetoListModel<T extends Comparable<T>> 
                       extends AbstractListModel {
 
    // Ha listában tároljuk a modell elemeit, akkor használhatjuk
    // a lista kényelmes szolgáltatásait,
    // de meg kell írnunk a tüzelő metódusokat.
    
    private List<T> modell = new ArrayList<>();
    
    // Rendezett-e a lista
    private boolean rendezett = false;
    
   // Kötelező implementálni a köv. két metódust (generálható):
    @Override
    public T getElementAt(int index) {
        return modell.get(index);
    }
 
    @Override
    public int getSize() {
        return modell.size();
    } 
    
    /**
     * Ha utólag akarjuk rendezni a modellt, akkor ezt 
     * a metódust kell hívni, de csak akkor hajtódik végre, 
     * ha még nincs rendezve.
     * (Látható, hogy használható a lista-rendezés.)
     * A rendezés után jelzi, hogy megváltozott a tartalma,
     * ezt a hozzárendelt JList kezeli majd.
     */
    public void sort() {
        if (!rendezett) {
            Collections.sort(modell);
            this.fireContentsChanged(this, 0, modell.size() - 1);
        }
    }
 
     /**
     * a modell adott indexű helyére szúr be egy elemet,
     * és jelzi, hogy ez meg is történt.
     * @param elem
     * @param index 
     */
    public void addElement(T elem, int index) {
        modell.add(index, elem);
        this.fireIntervalAdded(this, index, index);
    }
    
    /**
     * a modell végére szúr be egy elemet, 
     * és jelzi, hogy ez meg is történt.
     * @param elem 
     */
    public void addElement(T elem) {
        this.addElement(elem, modell.size());        
        this.fireIntervalAdded(elem, modell.size()-1, modell.size()-1);
    }
 
    /**
     * A rendezésre figyelve szúrja be az adott elemet.
     * 
     * Ha a rendezve értéke false, akkor a modell végére teszi az új elemet,
     * és jelzi, hogy a modell nem rendezett.
     * 
     * Ha a rendezve értéke true, akkor már eleve rendezetten rakja be az elemet.
     * Ha az eddig berakottakat elemek nem rendezetten szerepelnek a modellben,
     * akkor előbb ezeket is rendezi, és az ennek megfelelő helyre rakja az újat.
     * A hely megkereséséhez a bináris rendezést használja.
     * 
     * @param element
     * @param rendezve 
     */
    public void addElement(T element, boolean rendezve) {
        // Ha nem rendezve kérjük, akkor az új elemet a modell végére teszi.
        if (!rendezve) {
            this.addElement(element);
            rendezett = false;            
        } 
        // Ha rendezve kérjük, akkor
        else {
            // ha a modell eddig nem volt rendezve, akkor most rendezi
            if (!rendezett) {
                sort();
            }
            // Megkeresi az új elem bináris rendezésnek megfelelő helyét.
            int index = Collections.binarySearch(modell, element);  
            
            // A Collections.binarySearch() metódus a bináris keresés alapján 
            // megkeresi egy rendezett listában az adott elem indexét.
            // Ha a lista nincs rendezve, akkor a hívás eredménye definiálatlan.
            // Az index megadja a keresett elem indexét, ha létezik ilyen elem a listában,
            // egyébként: (-(beszurasiPont) - 1). 
            // A beszurasiPont az a hely, ahova be lehetne szúrni az elemet. 
            // Pontosabb részleteket ld. help.
         
            if (index < 0)
                addElement(element, -index - 1);
            else
                addElement(element, index);
            rendezett = true;
        }
    } 

    /**
     * Lekérdezhető, hogy rendezett-e a modell
     * @return true, ha rendezett, false, ha nem
     */
    public boolean isRendezett() {
        return rendezett;
    }

    /**
     * Beállítható, hogy rendezettnek tekintjük-e a modellt.
     * @param rendezett 
     */
    public void setRendezett(boolean rendezett) {
        this.rendezett = rendezett;
    }   
    
    public boolean contains(T element){
        return modell.contains(element);    
    }
    
    public void removeElement(T element){
        int index = modell.indexOf(element); 
        modell.remove(element);
        this.fireIntervalRemoved(element, index, index);
    }
}
