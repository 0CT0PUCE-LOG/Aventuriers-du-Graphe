package fr.umontpellier.iut.graphes;

import java.lang.reflect.Array;
import java.util.*;

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
        return mapAretes.get(v).size();
    }

    /**
     *
     * @return le degré max, et Integer.Min_VALUE si le graphe est vide
     */
    public int degreMax(){
        int degreMax = Integer.MIN_VALUE;
        for(Integer i : mapAretes.keySet()){
            if(degre(i) > degreMax){
                degreMax = degre(i);
            }
        }
        if(degreMax==Integer.MIN_VALUE){
            return 0;
        }
        else{
            return degreMax;
        }
    }

    public boolean estSimple(){
        for(Integer i : mapAretes.keySet()){
            for(Arete a : mapAretes.get(i)){
                if(a.i()!=i && a.j()!=i){
                    return false;
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
        //TODO peut être opti avec la fonction nbSommetDeDegre(int v) en une seule ligne
        for(Integer i : mapAretes.keySet()){
            for(Integer j : mapAretes.keySet()){
                if(i!=j && !existeArete(new Arete(i,j))){
                    return false;
                }
            }
        }
        return true;
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
        return estConnexe() && estAcyclique();
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
        throw new RuntimeException("Méthode non implémentée");
    }

    public boolean sontAdjacents(int i, int j) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Fusionne les deux sommets passés en paramètre.
     * Toutes les arêtes reliant i à j doivent être supprimées (pas de création de boucle).
     * L'entier correspondant au sommet nouvellement créé sera le min{i,j}. Le voisinage du nouveau sommet
     * est l'union des voisinages des deux sommets fusionnés.
     * Si un des sommets n'est pas présent dans le graphe, alors cette fonction ne fait rien.
     */
    public void fusionnerSommets(int i, int j) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe simple valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sequenceEstGraphe(List<Integer> sequence) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * @return true si et seulement si la séquence d'entiers passée en paramètre correspond à un graphe valide.
     * La pondération des arêtes devrait être ignorée.
     */
    public static boolean sontIsomorphes(Graphe g1, Graphe g2) {
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un plus court chemin entre 2 sommets suivant l'algorithme de Dijkstra.
     * Les poids des arêtes sont les longueurs des routes correspondantes.
     * @param depart le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     *
     */
    public List<Integer> algoDikstra(int depart, int arrivee, boolean pondere){
        throw new RuntimeException("Méthode non implémentée");
    }

    /**
     * Retourne un plus court chemin entre 2 sommets sans répétition de sommets
     * @param depart le sommet de départ
     * @param arrivee le sommet d'arrivée
     * @param pondere true si les arêtes sont pondérées (pas les longueurs des routes correspondantes dans le jeu)
     *                false si toutes les arêtes ont un poids de 1 (utile lorsque les routes associées sont complètement omises)
     */
    public List<Integer> parcoursSansRepetition(int depart, int arrivee, boolean pondere) {
        throw new RuntimeException("Méthode non implémentée");
    }
}