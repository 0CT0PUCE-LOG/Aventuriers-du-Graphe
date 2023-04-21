package fr.umontpellier.iut.graphes;


import fr.umontpellier.iut.rails.Route;
import fr.umontpellier.iut.rails.RouteMaritime;
import fr.umontpellier.iut.rails.data.Couleur;
import fr.umontpellier.iut.rails.data.Ville;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrapheTest {
    private Graphe  graphe;

    @BeforeEach
    void setUp() {
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(0, 3));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(8, 42));
        graphe = new Graphe(aretes);
    }

    @Test
    void testNbAretes() {
        assertEquals(5, graphe.nbAretes());
    }


    @Test
    void testContientSommet() {
        assertTrue(graphe.contientSommet(0));
        assertTrue(graphe.contientSommet(1));
        assertTrue(graphe.contientSommet(2));
        assertTrue(graphe.contientSommet(3));
        assertTrue(graphe.contientSommet(8));
        assertTrue(graphe.contientSommet(42));
        assertFalse(graphe.contientSommet(7));
    }

    @Test
    void testAjouterSommet() {
        int nbSommets = graphe.nbSommets();
        graphe.ajouterSommet(59);
        assertTrue(graphe.contientSommet(59));
        assertEquals(nbSommets + 1, graphe.nbSommets());
        graphe.ajouterSommet(59);
        assertEquals(nbSommets + 1, graphe.nbSommets());
    }

    @Test
    void testAjouterArete() {
        int nbAretes = graphe.nbAretes();
        graphe.ajouterArete(new Arete(0, 3));
        assertEquals(nbAretes, graphe.nbAretes());
        graphe.ajouterArete(new Arete(9, 439, null));
        assertEquals(nbAretes + 1, graphe.nbAretes());
        graphe.ajouterArete(new Arete(0, 3, new RouteMaritime(new Ville("Athina", true), new Ville("Marseille", true), Couleur.ROUGE, 2) {
        }));
        assertEquals(nbAretes + 2, graphe.nbAretes());
    }

    @Test
    void testSupprimerArete() {
        int nbAretes = graphe.nbAretes();
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3));
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerArete(new Arete(0, 3, null));
        assertEquals(nbAretes - 1, graphe.nbAretes());
    }

    @Test
    void testSupprimerSommet() {
        int nbSommets = graphe.nbSommets();
        int nbAretes = graphe.nbAretes();
        graphe.supprimerSommet(42);
        assertEquals(nbSommets - 1, graphe.nbSommets());
        assertEquals(nbAretes - 1, graphe.nbAretes());
        graphe.supprimerSommet(2);
        assertEquals(nbSommets - 2, graphe.nbSommets());
        assertEquals(nbAretes - 3, graphe.nbAretes());
    }

    @Test
    void testExisteArete() {
        assertTrue(graphe.existeArete(new Arete(0, 1)));
        assertTrue(graphe.existeArete(new Arete(0, 3)));
        assertTrue(graphe.existeArete(new Arete(1, 2)));
        assertTrue(graphe.existeArete(new Arete(2, 3)));
        assertTrue(graphe.existeArete(new Arete(8, 42)));
        assertFalse(graphe.existeArete(new Arete(0, 2)));
        assertFalse(graphe.existeArete(new Arete(0, 4)));
        assertFalse(graphe.existeArete(new Arete(1, 3)));
        assertFalse(graphe.existeArete(new Arete(2, 4)));
        assertFalse(graphe.existeArete(new Arete(8, 43)));
    }

    @Test
    void testEstConnexe1(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(3, 4));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estConnexe());
    }

    @Test
    void testEstUneChaine1(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(3, 4));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estUneChaine());
    }

    @Test
    void testEstUneChaine2(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(1, 4));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estUneChaine());
    }

    @Test
    void testEstUneChaine3(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estUneChaine());
    }

    @Test
    void testEstUneChaine4(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estUneChaine());
    }

    @Test
    void testEstUneChaine5(){
        Graphe graphe2 = new Graphe(1);
        assertTrue(graphe2.estUneChaine());
    }

    @Test
    void testEstUneChaine6(){
        Graphe graphe2 = new Graphe(2);
        assertFalse(graphe2.estUneChaine());
    }

    @Test
    void testEstUneChaine7(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 3));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));
        aretes.add(new Arete(6, 4));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estUneChaine());
    }

    @Test
    void testEstUnCycle1(){
        Graphe graphe2 = new Graphe(0);
        assertTrue(graphe2.estUnCycle());
    }

    @Test
    void testEstUnCycle2(){
        Graphe graphe2 = new Graphe(1);
        assertFalse(graphe2.estUnCycle());
    }

    @Test
    void testEstUnCycle3(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 0));
        Graphe graphe2 = new Graphe(aretes);
        assertEquals(graphe2.nbAretes(), 1);
        assertEquals(graphe2.nbSommets(), 2);
        assertFalse(graphe2.estUnCycle());
    }

    @Test
    void testEstUnCycle4(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 0));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estUnCycle());
    }

    @Test
    void testEstUnCycle5(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 0));
        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 3));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estUnCycle());
    }

    @Test
    void testEstAcyclique1(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(1, 4));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estAcyclique());
    }

    @Test
    void testEstAcyclique2(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 3));
        aretes.add(new Arete(1, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 0));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estAcyclique());
    }

    @Test
    void testEstAcyclique3(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 0));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estAcyclique());
    }

    @Test
    void testEstAcyclique4(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estAcyclique());
    }    

    @Test
    void testGetClasseConnexite(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 0));

        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(Arrays.asList(0,1,2).containsAll(graphe2.getClasseConnexite(0)) && graphe2.getClasseConnexite(0).containsAll(Arrays.asList(0,1,2)));
        assertTrue(Arrays.asList(3,4,5,6).containsAll(graphe2.getClasseConnexite(5)) && graphe2.getClasseConnexite(5).containsAll(Arrays.asList(3,4,5,6)));
    }

    @Test
    void testGetEnsembleClasseConnexite(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(2, 0));

        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));
        Graphe graphe2 = new Graphe(aretes);
        assertEquals(graphe2.getEnsembleClassesConnexite().size(), 2);
        assertTrue(graphe2.getEnsembleClassesConnexite().contains(new HashSet<Integer>(Arrays.asList(0,1,2))));
        assertTrue(graphe2.getEnsembleClassesConnexite().contains(new HashSet<Integer>(Arrays.asList(3,4,5,6))));
    }

    @Test
    void testEstUnArbre(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));

        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estUnArbre());
    }    

    @Test
    void testEstUneForet1(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));

        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));
        Graphe graphe2 = new Graphe(aretes);
        assertTrue(graphe2.estUneForet());
    }
    @Test
    void testEstUneForet2(){
        List<Arete> aretes = new ArrayList<>();
        aretes.add(new Arete(0, 1));
        aretes.add(new Arete(1, 2));
        aretes.add(new Arete(0, 2));

        aretes.add(new Arete(3, 4));
        aretes.add(new Arete(4, 5));
        aretes.add(new Arete(5, 6));
        Graphe graphe2 = new Graphe(aretes);
        assertFalse(graphe2.estUneForet());
    }

    @Disabled
    @Test
    void testGetVoisins() {
        /*
        Set<Integer> voisins0 = graphe.getVoisins(0);
        assertEquals(2, voisins0.size());
        assertTrue(voisins0.contains(1));
        assertTrue(voisins0.contains(3));
        List<Integer> voisins1 = graphe.getVoisins(1);
        assertEquals(2, voisins1.size());
        assertTrue(voisins1.contains(0));
        assertTrue(voisins1.contains(2));
        List<Integer> voisins2 = graphe.getVoisins(2);
        assertEquals(2, voisins2.size());
        assertTrue(voisins2.contains(1));
        assertTrue(voisins2.contains(3));
        List<Integer> voisins3 = graphe.getVoisins(3);
        assertEquals(2, voisins3.size());
        assertTrue(voisins3.contains(0));
        assertTrue(voisins3.contains(2));
        List<Integer> voisins8 = graphe.getVoisins(8);
        assertEquals(1, voisins8.size());
        assertTrue(voisins8.contains(42));
        List<Integer> voisins42 = graphe.getVoisins(42);
        assertEquals(1, voisins42.size());
        assertTrue(voisins42.contains(8));

         */
    }

    @Test
    void testEnsembleSommets(){
        /*
        List<Integer> sommets = graphe.ensembleSommets();
        assertEquals(5, sommets.size());
        assertTrue(sommets.contains(0));
        assertTrue(sommets.contains(1));
        assertTrue(sommets.contains(2));
        assertTrue(sommets.contains(3));
        assertTrue(sommets.contains(8));
        assertTrue(sommets.contains(42));

         */
    }
}