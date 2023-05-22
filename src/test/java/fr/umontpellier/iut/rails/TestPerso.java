package fr.umontpellier.iut.rails;

import fr.umontpellier.iut.rails.data.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPerso {
    
    private IOJeu jeu;
    private List<CarteTransport> piocheWagon;
    private List<CarteTransport> defausseWagon;
    private List<CarteTransport> piocheBateau;
    private List<CarteTransport> defausseBateau;
    private List<CarteTransport> cartesTransportVisibles;
    private List<Destination> pileDestinations;
    private List<Route> routes;
    private List<Ville> ports;
    private List<Joueur> joueurs;
    private Joueur joueur1;
    private List<CarteTransport> cartesJoueur1;
    private List<Route> routesJoueur1;
    private List<Ville> portsJoueur1;

    @BeforeAll
    static void staticInit() {
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @BeforeEach
    void setUp() {
        // réinitialisation des compteurs
        TestUtils.setAttribute(CarteTransport.class, "compteur", 1);
        TestUtils.setAttribute(Destination.class, "compteur", 1);
        TestUtils.setAttribute(Route.class, "compteur", 1);

        jeu = new IOJeu(new String[] { "Guybrush", "Largo", "LeChuck", "Elaine" });
        PilesCartesTransport pilesWagon = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesWagon");
        piocheWagon = (List<CarteTransport>) TestUtils.getAttribute(pilesWagon, "pilePioche");
        defausseWagon = (List<CarteTransport>) TestUtils.getAttribute(pilesWagon, "pileDefausse");
        PilesCartesTransport pilesBateau = (PilesCartesTransport) TestUtils.getAttribute(jeu,
                "pilesDeCartesBateau");
        piocheBateau = (List<CarteTransport>) TestUtils.getAttribute(pilesBateau, "pilePioche");
        defausseBateau = (List<CarteTransport>) TestUtils.getAttribute(pilesBateau, "pileDefausse");
        cartesTransportVisibles = (List<CarteTransport>) TestUtils.getAttribute(jeu,
                "cartesTransportVisibles");
        pileDestinations = (List<Destination>) TestUtils.getAttribute(jeu, "pileDestinations");
        routes = (List<Route>) TestUtils.getAttribute(jeu, "routesLibres");
        ports = (List<Ville>) TestUtils.getAttribute(jeu, "portsLibres");
        joueurs = (List<Joueur>) TestUtils.getAttribute(jeu, "joueurs");
        joueur1 = joueurs.get(0);
        cartesJoueur1 = (List<CarteTransport>) TestUtils.getAttribute(joueur1, "cartesTransport");
        routesJoueur1 = (List<Route>) TestUtils.getAttribute(joueur1, "routes");
        portsJoueur1 = (List<Ville>) TestUtils.getAttribute(joueur1, "ports");

        // initialisation des pions wagon et bateau du joueur 1
        TestUtils.setAttribute(joueur1, "nbPionsWagon", 20);
        TestUtils.setAttribute(joueur1, "nbPionsWagonEnReserve", 5);
        TestUtils.setAttribute(joueur1, "nbPionsBateau", 40);
        TestUtils.setAttribute(joueur1, "nbPionsBateauEnReserve", 10);

        // initialisation des cartes visibles
        cartesTransportVisibles.add(jeu.piocherCarteWagon());
        cartesTransportVisibles.add(jeu.piocherCarteWagon());
        cartesTransportVisibles.add(jeu.piocherCarteWagon());
        cartesTransportVisibles.add(jeu.piocherCarteBateau());
        cartesTransportVisibles.add(jeu.piocherCarteBateau());
        cartesTransportVisibles.add(jeu.piocherCarteBateau());
    }

    @Test
    void testRoutesPourCompleterDestination(){
        Destination d = jeu.piocherDestination();
        List<Route> r = (List) joueur1.routesPourCompleterDestination(d);
        System.out.println(r);

    }

}
