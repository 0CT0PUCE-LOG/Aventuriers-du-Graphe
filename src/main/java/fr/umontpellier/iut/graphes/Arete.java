package fr.umontpellier.iut.graphes;

import fr.umontpellier.iut.rails.Route;

import java.util.Objects;

/**
 * Classe modélisant les arêtes. Pour simplifier, vous pouvez supposer le prérequis que i!=j
 */
public record Arete(int i, int j, Route route) {
    public Arete(int i, int j) {
        this(i, j, null);
    }

    public boolean incidenteA(int v) {
        return i == v || j == v;
    }

    public int getAutreSommet(int v) {
        return v == i ? j : i;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result += prime * i;
        result += prime * j;
        result = prime * result + ((route == null) ? 0 : route.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Arete other = (Arete) obj;
        if (route == null) {
            if (other.route != null)
                return false;
        } else if (!route.equals(other.route))
            return false;
        if (( (i == other.i) && (j == other.j) ) || ( (j == other.i) && (i == other.j) ))
            return true;    
        return false;
    }

    

}