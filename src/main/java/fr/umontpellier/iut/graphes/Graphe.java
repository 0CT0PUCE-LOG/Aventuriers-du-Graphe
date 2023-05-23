package fr.umontpellier.iut.graphes;

import java.lang.reflect.Array;
import java.util.*;

import javax.swing.event.ListDataListener;

import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.RouteMaritime;

/**
 * (Multi) Graphe non-orienté pondéré. Le poids de chaque arête correspond à la longueur de la route correspondante.
 * Pour une paire de sommets fixée {i,j}, il est possible d'avoir plusieurs arêtes
 * d'extrémités i et j et de longueur identique, du moment que leurs routes sont différentes.
 * Par exemple, il est possible d'avoir les deux arêtes suivantes dans le graphe :
 * Arete a1 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * et
 * Arete a2 = new Arete(i,j,new RouteTerrestre(villes.get("Lima"), villes.get("Valparaiso"), Couleur.GRIS, 2))
 * Dans cet exemple (issus du jeu), a1 et a2 sont deux arêtes différentes, même si leurs routes sont très similaires
 * (seul l'attribut nom est différent).
 */
public class Graphe {

    /**
     * Liste d'incidences :
     * mapAretes.get(1) donne l'ensemble d'arêtes incidentes au sommet dont l'identifiant est 1
     * Si mapAretes.get(u) contient l'arête {u,v} alors, mapAretes.get(v) contient aussi cette arête
     */
    private Map<Integer, HashSet<Arete>> mapAretes;


    /**
     * Construit un graphe à n sommets 0..n-1 sans arêtes
     */
    public Graphe(int n) {
        this();
        for (int i = 0; i < n; i++) {
            this.mapAretes.put(i, new HashSet<>());
        }
    }

    /**
     * Construit un graphe vide
     */
    public Graphe() {
        this.mapAretes = new HashMap<>();
    }

    public Graphe(Graphe g) {
        this();
        for(int i : g.mapAretes.keySet()){
            mapAretes.put(i, new HashSet<>(g.mapAretes.get(i)));
        }
    }

    /**
     * Construit un graphe à partir d'une collection d'arêtes.
     *
     * @param aretes la collection d'arêtes
     */
    public Graphe(Collection<Arete> aretes) {
        this();
        for (Arete a : aretes) {
            if(!existeArete(a)){
                this.ajouterArete(a);
            }
        }
    }

    /**
     * À partir d'un graphe donné, construit un sous-graphe induit
     * par un ensemble de sommets, sans modifier le graphe donné
     *
     * @param graphe le graphe à partir duquel on construit le sous-graphe
     * @param X      l'ensemble de sommets qui définissent le sous-graphe
     *               prérequis : X inclus dans V()
     */
    public Graphe(Graphe graphe, Set<Integer> X) {
        this();
        for(Integer i : X){
            for(Arete a : graphe.mapAretes.get(i)){
                if(X.contains(a.i()) && X.contains(a.j())){
                    ajouterArete(a);
                }
            }
        }
    }

    /**
     * @return l'ensemble de sommets du graphe
     */
    public Set<Integer> ensembleSommets() {
        return mapAretes.keySet();
    }

    /**
     * @return l'ordre du graphe (le nombre de sommets)
     */
    public int nbSommets() {
        return mapAretes.size();
    }

    /**
     * @return le nombre d'arêtes du graphe (ne pas oublier que this est un multigraphe : si plusieurs arêtes sont présentes entre un même coupe de sommets {i,j}, il faut
     * toutes les compter)
     */
    public int nbAretes() {
        int compteur = 0;
        for(Integer m : mapAretes.keySet()){
            compteur+= mapAretes.get(m).size();
        }
        return compteur/2;
    }


    public boolean contientSommet(Integer v) {
        return mapAretes.containsKey(v);
    }

    /**
     * Ajoute un sommet au graphe s'il n'est pas déjà présent
     *
     * @param v le sommet à ajouter
     */
    public void ajouterSommet(Integer v) {
        if(!contientSommet(v)){
            mapAretes.put(v, new HashSet<>());
        }
    }

    /**
     * Ajoute une arête au graphe si elle n'est pas déjà présente
     *
     * @param a l'arête à ajouter. Si les 2 sommets {i,j} de a ne sont pas dans l'ensemble,
     *          alors les sommets sont automatiquement ajoutés à l'ensemble de sommets du graphe
     */
    public void ajouterArete(Arete a) {
        ajouterSommet(a.i());
        ajouterSommet(a.j());
        if(!existeArete(a)){
            mapAretes.get(a.i()).add(a);
            mapAretes.get(a.j()).add(a);
        }
    }

    /**
     * Supprime une arête du graphe si elle est présente, sinon ne fait rien
     *
     * @param a arête à supprimer
     *
     */
    public void supprimerArete(Arete a) {
        if(existeArete(a)){
            mapAretes.get(a.i()).remove(a);
            mapAretes.get(a.j()).remove(a);
        }
    }

    /**
     * @param a l'arête dont on veut tester l'existence
     * @return true si a est présente dans le graphe
     */
    public boolean existeArete(Arete a) {
        if(contientSommet(a.i())){
            return mapAretes.get(a.i()).contains(a);
        }
        else{
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Integer v : mapAretes.keySet()) {
            sb.append("sommet").append(v).append(" : ").append(mapAretes.get(v)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Retourne l'ensemble des sommets voisins d'un sommet donné.
     * Si le sommet n'existe pas, l'ensemble retourné est vide.
     *
     * @param v l'identifiant du sommet dont on veut le voisinage
     */
    public Set<Integer> getVoisins(int v) {
        Set<Integer> voisins = new HashSet<>();
        for (Arete a : this.mapAretes.get(v)) {
            if (a.i() == v) {
                voisins.add(a.j());
            } else {
                voisins.add(a.i());
            }
        }
        return voisins;
    }

    public Map<Integer, Arete> getVoisinsAvecArete(int v) {
        HashMap<Integer, Arete> voisins = new HashMap<>();
        for (Arete a : this.mapAretes.get(v)) {
            if (a.i() == v) {
                voisins.put(a.j(), a);
            } else {
                voisins.put(a.i(), a );
            }
        }
        return voisins;
    }




    /**
     * Supprime un sommet du graphe, ainsi que toutes les arêtes incidentes à ce sommet
     *
     * @param v le sommet à supprimer
     */
    public void supprimerSommet(int v) {
        if(contientSommet(v)){
            for(Integer m : mapAretes.keySet()){
                mapAretes.get(m).removeIf(arete -> arete.incidenteA(v));
            }
            mapAretes.remove(v);
        }
    }

    public int degre(int v) {
        //TODO que faire si le sommet n'existe pas
        if(mapAretes.containsKey(v)){
            return mapAretes.get(v).size();
        }
        else{
            return 0;
        }
        
    }

    /**
     * @return le degré max, et Integer.MIN_VALUE si le graphe est vide
     */
    public int degreMax() {
        if (mapAretes.isEmpty()) {
            return Integer.MIN_VALUE;
        }
        int degreMax = Integer.MIN_VALUE;
        for (Integer sommet : mapAretes.keySet()) {
            int degreSommet = degre(sommet);
            if (degreSommet > degreMax) {
                degreMax = degreSommet;
            }
        }
        return degreMax;
    }


    public boolean estSimple() {
        for(int sommet : mapAretes.keySet()){
            HashMap<Integer, Integer> compteur = new HashMap<>();
            for(Arete a : mapAretes.get(sommet)){
                if(compteur.keySet().contains(a.getAutreSommet(sommet))){
                    return false;
                }
                else if(a.i() == a.j()){
                    return false;
                }
                else{
                    compteur.put(sommet, 1);
                }
            }
        }
        return true;
    }

    public boolean estSimpleBIS(){
        if(estAcyclique()){

        }
        else{
            return false;
        }
        for(Integer i : mapAretes.keySet()){
            HashMap<Integer, Integer> compteur = new HashMap<>();
            for(Arete a : mapAretes.get(i)){
                if(!compteur.keySet().contains(a.getAutreSommet(i))){
                    compteur.put(a.getAutreSommet(i),0);
                }
                else{
                    compteur.put(a.getAutreSommet(i), compteur.get(a.getAutreSommet(i))+1);
                    if(compteur.get(a.getAutreSommet(i)) > 1 ){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * @return ture ssi pour tous sommets i,j de this avec (i!=j), alors this contient une arête {i,j}
     *
     */
    public boolean estComplet() {
        return nbSommetDeDegre(nbSommets()-1) == nbSommets();
    }

    /**
     * @return true ssi this est une chaîne. Attention, être une chaîne
     * implique en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs de la chaîne. On considère que le graphe vide est une chaîne.
     */
    public boolean estUneChaine() {
        if(degreMax()>2){
            return false;
        }
        else{
            if(nbSommetDeDegre(1)!=2 && nbSommets()>=2){
                return false;
            }
            else if(!estConnexe()){
                return false;
            }
            else{
                return true;
            }
        }
    }

    private int nbSommetDeDegre(int n){
        int compteur=0;
        for(Integer i : mapAretes.keySet()){
            if(degre(i)==n){
                compteur++;
            }
        }
        return compteur;
    }


    /**
     * @return true ssi this est un cycle. Attention, être un cycle implique
     * en particulier que l'on a une seule arête (et pas plusieurs en parallèle) entre
     * les sommets successifs du cycle.
     * On considère que dans le cas où G n'a que 2 sommets {i,j}, et 2 arêtes parallèles {i,j}, alors G n'est PAS un cycle.
     * On considère que le graphe vide est un cycle.
     */
    public boolean estUnCycle() {
        if(nbSommetDeDegre(2) == nbSommets() && estConnexe()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean estConnexe(){
        if(nbSommets()>=2){
            return estConnexe( (int) mapAretes.keySet().toArray()[0], new ArrayList<>());
        }
        else{
            return true;
        }
    }

    private boolean estConnexe(int sommetCourant, ArrayList<Integer> dejaVu){
        ArrayList<Integer> voisins = new ArrayList<>(getVoisins(sommetCourant));
        dejaVu.add(sommetCourant);
        if(dejaVu.containsAll(mapAretes.keySet())){
            return true;
        }
        else if(dejaVu.containsAll(voisins)){
            return false;
        }
        else{
            boolean result = false;
            int i=0;

            while(i<voisins.size()&& result==false){
                if(!dejaVu.contains(voisins.get(i))){
                    result = result || estConnexe(voisins.get(i), dejaVu);
                }
                i++;
            }
            return result;
        }
    }

    public boolean estAcyclique(){
        Set<Set<Integer>> ensembleConnexes = getEnsembleClassesConnexite();
        boolean result = true;
        Graphe tempo;
        for(Set<Integer> groupe : ensembleConnexes){
            if(groupe.size()>2){
                tempo = new Graphe(this, groupe);
                result = tempo.estAcyclique( (int) mapAretes.keySet().toArray()[0], new ArrayList<>(), new Graphe(this) );
                if(result == false){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean estAcyclique(int sommetCourant, ArrayList<Integer> dejaVu, Graphe copieGraphe){
        ArrayList<Integer> voisins = new ArrayList<>(copieGraphe.getVoisins(sommetCourant));
        if(dejaVu.contains(sommetCourant)){
            dejaVu.add(sommetCourant);
            return false;
        }
        else if(voisins.isEmpty()){
            dejaVu.add(sommetCourant);
            return true;
        }

        boolean result = true;
        int i=0;
        dejaVu.add(sommetCourant);
        while(i<voisins.size() && result==true){
            copieGraphe.supprimerArete(new Arete(sommetCourant, voisins.get(i), null));
            result = estAcyclique(voisins.get(i), dejaVu, copieGraphe);
            i++;
        }
        return result;
    }

    public boolean estUnArbre(){
        return estConnexe() && nbAretes() == nbSommets()-1;
    }

    public boolean estUneForet() {
        Set<Set<Integer>> ensembleConnexes = getEnsembleClassesConnexite();
        for(Set<Integer> classeConnexe : ensembleConnexes){
            if(!(new Graphe(this, classeConnexe).estUnArbre())){
                return false;
            }
        }
        return true;
    }

    public Set<Integer> getClasseConnexite(int v) {
        return getClasseConnexite(v, new HashSet<Integer>());
    }

    private Set<Integer> getClasseConnexite(int sommetCourant, Set<Integer> dejaVu){
        ArrayList<Integer> voisins = new ArrayList<>(getVoisins(sommetCourant));
        dejaVu.add(sommetCourant);
        if(dejaVu.containsAll(voisins)){
            return dejaVu;
        }
        else{
            for(int i : voisins){
                if(!dejaVu.contains(i)){
                    getClasseConnexite(i, dejaVu);
                }
            }
            return dejaVu;
        }
    }

    public Set<Set<Integer>> getEnsembleClassesConnexite() {
        ArrayList<Integer> listeSommet = new ArrayList<>(mapAretes.keySet());
        Set<Set<Integer>> ensemble = new HashSet<>();
        Set<Integer> tempo = new HashSet<>();
        while(!listeSommet.isEmpty()){
            tempo = getClasseConnexite(listeSommet.get(0));
            listeSommet.removeAll(tempo);
            ensemble.add(tempo);
        }
        return ensemble;
    }

    /**
     * @return true si et seulement si l'arête passée en paramètre est un isthme dans le graphe.
     */
    public boolean estUnIsthme(Arete a) {
        Graphe copieGraphe = new Graphe(this);
        copieGraphe.supprimerArete(a);
        return !copieGraphe.getClasseConnexite(a.i()).contains(a.j());
    }

    public boolean sontAdjacents(int i, int j) {
        ArrayList<Integer> voisins = new ArrayList<>(getVoisins(i));
        return voisins.contains(j);
    }

    /**
     * Fusionne les deux sommets passés en paramètre.
     * Toutes les arêtes reliant i à j doivent être supprimées (pas de création de boucle).
     * L'entier correspondant au sommet nouvellement créé sera le min{i,j}. Le voisinage du nouveau sommet
     * est l'union des voisinages des deux sommets fusionnés.
     * Si un des sommets n'est pas présent dans le graphe, alors cette fonction ne fait rien.
     */
    public void fusionnerSommets(int i, int j) {
        if(contientSommet(i) && contientSommet(j)){
            int min = Math.min(i, j);
            int max = Math.max(i, j);
            ArrayList<Integer> voisins = new ArrayList<>(getVoisins(max));
            for(int v : voisins){
                if(v!=min){
                    supprimerArete(new Arete(max, v, null));
                    ajouterArete(new Arete(min, v, null));
                }
            }
            supprimerSommet(max);
        }
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe simple valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        List<Integer> sequence_copy = ordonnerSequence(sequence);
        int compteur = 0;
        int compteur2 = 0;
        for (Integer value : sequence_copy) {
            if (value % 2 != 0) {
                compteur++;
            }
        }
        if(compteur%2 == 0){
            int b = sequence_copy.get(sequence_copy.size()-1);
            if(b > sequence_copy.size()-1){
                return false;
            }
            else{
                sequence_copy.set(sequence_copy.size()-1, 0);
                int y = sequence_copy.size()-2;
                while(b>0){
                    if(sequence_copy.get(y) != 0){
                        sequence_copy.set(y, sequence_copy.get(y) - 1);
                        b--;
                        y--;
                    }
                    else{
                        return false;
                    }
                }
                for (Integer integer : sequence_copy) {
                    if (integer == 0) {
                        compteur2++;
                    }
                }
                if(compteur2 == sequence_copy.size()){
                    return true;
                }
            }
        }
        else{
            return false;
        }
        return sequenceEstGraphe(sequence_copy);
    }


    //version alternative pour les tests
    /*
    public static boolean sequenceEstGraphe2(List<Integer> sequence) {
        List<Integer> sequence_copy = ordonnerSequence(sequence);
        int compteur = 0;
        int compteur2 = 0;
        for(int i=0; i<sequence_copy.size(); i++){
            if( sequence_copy.get(i)%2!=0 ){
                compteur++;
            }
        }
        if(compteur%2 == 0){
            int b = sequence_copy.get(sequence_copy.size()-1);
            if(b > sequence_copy.size()-1){
                return false;
            }
            else{
                sequence_copy.set(sequence_copy.size()-1, 0);
                int y = sequence_copy.size()-2;
                while(b>0){
                    if(sequence_copy.get(y) != 0){
                        sequence_copy.set(y, sequence_copy.get(y) - 1);
                        b--;
                        y--;
                    }
                    else{
                        return false;
                    }
                }
                for (Integer integer : sequence_copy) {
                    if (integer == 0) {
                        compteur2++;
                    }
                }
                if(compteur2 == sequence_copy.size()){
                    return true;
                }
                else{
                    return sequenceEstGraphe(sequence_copy);
                }
            }
        }
        else{
            return false;
        }
    }

     */















    public static List<Integer> ordonnerSequence(List<Integer> sequence){
        Collections.sort(sequence);
        return sequence;
    }

    /**
     * @return true si les deux graphes passés en paramètre sont isomorphes.
     * pré-requis : les deux graphes sont des graphes simples.
     */
    public static boolean sontIsomorphes(Graphe g1, Graphe g2) {
        List<Integer> sequence1 = g1.getSequenceDegre();
        List<Integer> sequence2 = g2.getSequenceDegre();
        if(sequence1.equals(sequence2)){
            HashMap<Integer, List<Integer>> bijections = new HashMap<>();
            HashMap<Integer, Integer> compteurBijections = new HashMap<>();
            boolean fini = false;
            //-créations des différentes bijections possibles-
            for (Integer i : g1.mapAretes.keySet()) {
                bijections.put(i, new ArrayList<Integer>());
                for (Integer j : g2.mapAretes.keySet()) {
                    if(g1.degre(i) == g2.degre(j)){
                        bijections.get(i).add(j);
                    }
                }
                compteurBijections.put(i, 0);
            }
            //------------------------------------------------
            while(!fini){
                if(bijectionEstIsomorphe(g1, g2, bijections, compteurBijections)){
                    return true;
                }
                do{
                    fini = !incrementeCompteurBijection(bijections, compteurBijections);
                }while(!bijectionEstValide(bijections, compteurBijections) && !fini);

            }
            return false;
        }
        else{
            return false;
        }
    }

    private static boolean bijectionEstIsomorphe(Graphe g1, Graphe g2, Map<Integer, List<Integer>> bijection, Map<Integer, Integer> compteurBijection){
        Set<Integer> voisins = new HashSet<>();
        for (Integer i : g1.mapAretes.keySet()) {
            voisins = g1.getVoisins(i);
            for (Integer v : voisins) {
                if(!g2.existeArete(new Arete(bijection.get(i).get(compteurBijection.get(i)), bijection.get(v).get(compteurBijection.get(v)), null))){
                    return false;
                }
            }

        }
        return true;
    }

    private static boolean incrementeCompteurBijection(Map<Integer, List<Integer>> bijection, Map<Integer, Integer> compteurBijection){
        List<Integer> sommets = new ArrayList<>(bijection.keySet());
        for (int i = 0; i< sommets.size(); i++) {
            if(compteurBijection.get(sommets.get(i)) < bijection.get(sommets.get(i)).size()-1){
                compteurBijection.put(sommets.get(i), compteurBijection.get(sommets.get(i))+1);
                return true;
            }
            else if(i!= sommets.size()-1){
                for (int j = 0; j<= i; j++) {
                    compteurBijection.put(sommets.get(i), 0);
                }
            }
        }
        return false;
    }

    private static boolean bijectionEstValide(Map<Integer, List<Integer>> bijection, Map<Integer, Integer> compteurBijection){
        Set<Integer> dejaVu = new HashSet<>();
        for(Integer i : bijection.keySet()){
            if(dejaVu.contains(bijection.get(i).get(compteurBijection.get(i)))){
                return false;
            }
            else{
                dejaVu.add(bijection.get(i).get(compteurBijection.get(i)));
            }

        }
        return true;
    }

    public List<Integer> getSequenceDegre(){
        List<Integer> sequence = new ArrayList<>();
        for(Integer i : mapAretes.keySet()){
            sequence.add(degre(i));
        }
        return ordonnerSequence(sequence);
    }


    /**
     * Retourne un plus court chemin entre 2 sommets.
     *
     * @param depart  le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     * @return une liste d'entiers correspondant aux sommets du chemin dans l'ordre : l'élément en position 0 de la liste
     * est le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     */

    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere) {
        List<Integer> chemin = parcoursSansRepetitionRec(depart, arrivee, new ArrayList<>(), pondere);
        return chemin;
    }

    public Arete getArete(int i, int j){
        if(getVoisins(i).contains(j)){
            for (Arete a : mapAretes.get(i)) {
                if(a.incidenteA(j)){
                    return a;
                }
            }
        }
        return null;
    }

    private int ponderationChemin(List<Integer> chemin, boolean pondere){
        if(pondere){
            int result = 0;
            for(int i=0; i<chemin.size()-1; i++){
                result+= getArete(chemin.get(i), chemin.get(i+1)).route().getLongueur();
            }
            return result;
        }
        else{
            return chemin.size();
        }

    }

    private List<Integer> parcoursSansRepetitionRec(int sommetCourant, int arrivee, List<Integer> dejaVu, boolean pondere){
        ArrayList<Integer> voisins = new ArrayList<>(getVoisins(sommetCourant));
        dejaVu.add(sommetCourant);
        if(sommetCourant == arrivee){
            return new ArrayList<Integer>(Arrays.asList(sommetCourant));
        }
        else if(dejaVu.containsAll(voisins)){
            return new ArrayList<>();
        }
        else{
            int i=0;
            List<Integer> chemin = new ArrayList<>();
            int ponderationChemin = 0;
            while(i<voisins.size()){
                List<Integer> dejaVuLocal = new ArrayList<>(dejaVu);
                if(!dejaVuLocal.contains(voisins.get(i))){
                    List<Integer> propositionChemin = parcoursSansRepetitionRec(voisins.get(i), arrivee, dejaVuLocal, pondere);
                    propositionChemin.add(0,sommetCourant);
                    if(propositionChemin.contains(arrivee) && (ponderationChemin > ponderationChemin(propositionChemin, pondere) || chemin.isEmpty())){
                        chemin.clear();
                        chemin.addAll(propositionChemin);
                        ponderationChemin = ponderationChemin(propositionChemin, pondere);
                    }
                }
                i++;
            }
            return chemin;
        }
    }


    /**
     * Retourne un chemin entre 2 sommets sans répétition de sommets et sans dépasser
     * le nombre de bateaux et wagons disponibles. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @param depart    le sommet de départ
     * @param arrivee   le sommet d'arrivée
     * @param nbBateaux le nombre de bateaux disponibles
     * @param nbWagons  le nombre de wagons disponibles
     * @return une liste d'entiers correspondant aux sommets du chemin, où l'élément en position 0 de la liste
     * et le sommet de départ, et l'élément à la dernière position de la liste (taille de la liste - 1) est le somme d'arrivée.
     * Si le chemin n'existe pas, retourne une liste vide (initialisée avec 0 éléments).
     * Pré-requis le graphe `this` est un graphe avec des routes (les objets routes ne sont pas null).
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, int nbWagons, int nbBateaux) {
        return parcoursSansRepetitionRec(depart, arrivee, new ArrayList<>(), nbWagons, nbBateaux);
    }

    private List<Integer> parcoursSansRepetitionRec(int sommetCourant, int arrivee, List<Integer> dejaVu, int nbWagons, int nbBateaux){
        ArrayList<Integer> voisins = new ArrayList<>(getVoisins(sommetCourant));
        dejaVu.add(sommetCourant);
        if(sommetCourant == arrivee){
            return new ArrayList<Integer>(Arrays.asList(sommetCourant));
        }
        else if(dejaVu.containsAll(voisins)){
            return new ArrayList<>();
        }
        else{
            int i=0;
            List<Integer> chemin = new ArrayList<>();
            int nbPionChemin = Integer.MAX_VALUE;

            while(i<voisins.size()){
                List<Integer> dejaVuLocal = new ArrayList<>(dejaVu);
                if(!dejaVuLocal.contains(voisins.get(i))){
                    List<Integer> propositionChemin = parcoursSansRepetitionRec(voisins.get(i), arrivee, dejaVuLocal, nbWagons, nbBateaux);
                    propositionChemin.add(0,sommetCourant);
                    int[] coutPropositionChemin = coutPionsChemin(propositionChemin);
                    if(propositionChemin.contains(arrivee) && coutPropositionChemin[0]<= nbWagons && coutPropositionChemin[1]<= nbBateaux && coutPropositionChemin[0]+coutPropositionChemin[1]< nbPionChemin){
                        chemin.clear();
                        chemin.addAll(propositionChemin);
                        nbPionChemin = coutPropositionChemin[0] + coutPropositionChemin[1];
                    }
                }
                i++;
            }
            return chemin;
        }
    }

    public List<Integer> parcoursSansRepetition(List<Integer> listeSommets, int nbWagons, int nbBateaux) {
        List<Integer> chemin = new ArrayList<>();
        chemin.add(listeSommets.get(0));
        for(int i=0; i< listeSommets.size()-1; i++){
            List<Integer> dejaVu = new ArrayList<>(intersect(listeSommets, chemin));
            dejaVu.addAll(subTab(listeSommets, i+2, listeSommets.size()));
            List<Integer> portionchemin = parcoursSansRepetitionRec(listeSommets.get(i), listeSommets.get(i+1),dejaVu, nbWagons, nbBateaux);
            if(!portionchemin.contains(listeSommets.get(i+1))){
                return new ArrayList<>();
            }
            chemin.addAll(subTab(portionchemin,1, portionchemin.size()));
            int[] nbPions = new int[2];
            nbPions = coutPionsChemin(portionchemin);
            nbWagons -= nbPions[0];
            nbBateaux -= nbPions[1];

        }
        return chemin;
    }

    //indice 0 nbPionsWagon
    //indice 1 nbPionsBateau
    private int[] coutPionsChemin(List<Integer> chemin){
        int[] compteurs = new int[2];
        Route tempo;
        for(int i=0; i<chemin.size()-1; i++){
            tempo = getArete(chemin.get(i), chemin.get(i+1)).route();
            if(tempo instanceof RouteMaritime){
                compteurs[1]+= tempo.getLongueur();
            }
            else{
                compteurs[0]+= tempo.getLongueur();
            }
        }
        return compteurs;
    }




    /**
     * Retourne un chemin passant une et une seule fois par tous les sommets d'une liste donnée.
     * Les éléments de la liste en paramètres doivent apparaître dans le même ordre dans la liste de sortie.
     *
     * @param listeSommets la liste de sommets à visiter sans répétition ;
     *                     pré-requis : c'est une sous-liste de la liste retournée
     * @return une liste d'entiers correspondant aux sommets du chemin.
     * Si le chemin n'existe pas, retourne une liste vide.
     */
    public List<Integer> parcoursSansRepetition(List<Integer> listeSommets) {
        List<Integer> chemin = new ArrayList<>();
        chemin.add(listeSommets.get(0));
        for(int i=0; i< listeSommets.size()-1; i++){
            List<Integer> dejaVu = new ArrayList<>(intersect(listeSommets, chemin));
            dejaVu.addAll(subTab(listeSommets, i+2, listeSommets.size()));
            List<Integer> portionchemin = parcoursSansRepetitionRec(listeSommets.get(i), listeSommets.get(i+1),dejaVu, false);
            if(!portionchemin.contains(listeSommets.get(i+1))){
                return new ArrayList<>();
            }
            chemin.addAll(subTab(portionchemin,1, portionchemin.size()));
        }
        return chemin;
    }

    public List<Integer> parcoursSansRepetition(List<Integer> listeSommets, boolean ponderation) {
        List<Integer> chemin = new ArrayList<>();
        chemin.add(listeSommets.get(0));
        for(int i=0; i< listeSommets.size()-1; i++){
            List<Integer> dejaVu = new ArrayList<>(intersect(listeSommets, chemin));
            dejaVu.addAll(subTab(listeSommets, i+2, listeSommets.size()));
            List<Integer> portionchemin = parcoursSansRepetitionRec(listeSommets.get(i), listeSommets.get(i+1),dejaVu, ponderation);
            if(!portionchemin.contains(listeSommets.get(i+1))){
                return new ArrayList<>();
            }
            chemin.addAll(subTab(portionchemin,1, portionchemin.size()));
        }
        return chemin;
    }

    private List<Integer> intersect(List<Integer> a, List<Integer> b){
        List<Integer> result = new ArrayList<Integer>();
        for(int i : b){
            if(a.contains(i)){
                result.add(i);
            }
        }
        return result;
    }

    private List<Integer> subTab(List<Integer> tab, int debut, int fin){
        List<Integer> result = new ArrayList<Integer>();
        for(int i = debut; i<fin; i++){
            result.add(tab.get(i));
        }
        return result;
    }


    /**
     * Retourne un plus petit ensemble bloquant de routes entre deux villes. Cette fonction supposera que `this` est
     * bien un graphe issu du jeu avec des vraies routes (les objets routes ne sont pas null).
     * Dans cette fonction la couleur des routes n'est pas à prendre en compte.
     *
     * @return un ensemble de route.
     * Remarque : l'ensemble retourné doit être le plus petit en nombre de routes (et PAS en somme de leurs longueurs).
     * Remarque : il se peut qu'il y ait plusieurs ensemble de cardinalité minimum.
     * Un seul est à retourner (au choix).
     */
    public Set<Route> ensembleBloquant(int ville1, int ville2) {
        throw new RuntimeException("Méthode non implémentée");
    }


}