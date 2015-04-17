package arquitetura.representation;

import arquitetura.exceptions.ConcernNotFoundException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 * @author edipofederle<edipofederle@gmail.com>
 *
 */
public abstract class Element implements Serializable {

    private static final long serialVersionUID = 4736685073967472613L;

    protected String id;
    private String name;
    private VariationPoint variationPoint;
    private Variant variant;
    private Set<Concern> concerns = new HashSet<Concern>();
    //Inicio - Thaina 12/14 - Aspecto
    private Set<Aspect> aspects = new HashSet<Aspect>();
    //Fim - Thaina 12/14 - Aspecto
    private Architecture architecture;
    private String typeElement;
    private String namespace;
    private boolean belongsToGeneralization;

    public Element(String name, Variant variant, String typeElement, String namespace, String id) {
        setId(id);
        setName(name);
        setVariant(variant);
        setTypeElement(typeElement);
        setNamespace(namespace);
    }

    public abstract Collection<Concern> getAllConcerns();

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    private void setId(String id) {
        this.id = id;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    private void setTypeElement(String typeElement) {
        this.typeElement = typeElement;
    }

    public String getTypeElement() {
        return this.typeElement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isVariationPoint() {
        return this.getVariationPoint() != null;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Retorna apenas os interesses pertencentes a este elemento.<br />
     *
     * @return List<{@link Concern}>
     */
    public Set<Concern> getOwnConcerns() {
        if (concerns == null || concerns.isEmpty()) {
            return Collections.emptySet();
        }
        return concerns;
    }

    public boolean containsConcern(Concern concern) {
        for (Concern conc : getOwnConcerns()) {
            if (conc.getName().equalsIgnoreCase(concern.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addConcerns(List<String> concernsNames) throws ConcernNotFoundException {
        for (String name : concernsNames) {
            addConcern(name);
        }
    }

    //Inicio - Thaina 12/14 - Aspecto
    public Set<Aspect> getAspects() {
        if (aspects == null || aspects.isEmpty()) {
            return Collections.emptySet();
        }
        return aspects;
    }

    public boolean isAspect() {
        for (Aspect aspect : getAspects()) {
            if (aspect.getName().equalsIgnoreCase("aspect")) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAspect(Aspect aspect) {
        for (Aspect a : getAspects()) {
            if (a.getName().equalsIgnoreCase(aspect.getName())) {
                return true;
            }
        }
        return false;
    }

    public void addAspects(List<String> aspectsNames) throws ConcernNotFoundException {
        for (String name : aspectsNames) {
            addAspect(name);
        }
    }
    //Fim - Thaina 12/14 - Aspecto

    /**
     * Adiciona um interesse a classe.<br/>
     *
     * Somente irá incluir o interesse se o mesmo estive presente no arquivo de
     * perfil que contêm os interesses</br>
     *
     *
     * @param concernName
     * @throws ConcernNotFoundException
     *
     */
    public void addConcern(String concernName) throws ConcernNotFoundException {
        Concern concern = ConcernHolder.INSTANCE.getOrCreateConcern(concernName);
        concerns.add(concern);
    }

    public void removeConcern(String concernName) {
        Concern concern = ConcernHolder.INSTANCE.getConcernByName(concernName);
        concerns.remove(concern);
    }

    //Inicio - Thaina 12/14 - Aspecto
    public void addAspect(String aspectName) throws ConcernNotFoundException{
        Aspect aspect = AspectHolder.INSTANCE.getOrCreateAspect(aspectName);
        aspects.add(aspect);

    }

    public void removeAspect(String aspectName) {
        Aspect aspect = AspectHolder.INSTANCE.getAspectByName(aspectName);
        aspects.remove(aspect);
    }
    //Fim - Thaina 12/14 - Aspecto

    /**
     * @return the namespace
     */
    public String getNamespace() {
        return namespace;
    }

    public Architecture getArchitecture() {
        return architecture;
    }

    /**
     * @return the variationPoint
     */
    public VariationPoint getVariationPoint() {
        return variationPoint;
    }

    /**
     * @param variationPoint the variationPoint to set
     */
    public void setVariationPoint(VariationPoint variationPoint) {
        this.variationPoint = variationPoint;
    }

    public boolean belongsToGeneralization() {
        return this.belongsToGeneralization;
    }

    public void setBelongsToGeneralization(boolean belongsToGeneralization) {
        this.belongsToGeneralization = belongsToGeneralization;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((namespace == null) ? 0 : namespace.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Element other = (Element) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (namespace == null) {
            if (other.namespace != null) {
                return false;
            }
        } else if (!namespace.equals(other.namespace)) {
            return false;
        }
        return true;
    }

}
