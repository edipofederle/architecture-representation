/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arquitetura.representation;

import arquitetura.exceptions.ConcernNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author thaina
 */
public enum AspectHolder {

    INSTANCE;

    private final HashMap<String, Aspect> aspects = new HashMap<String, Aspect>();

    /**
     * Esta lista é carregada a partir do arquivo de concerns indica no arquivo
     * de configuração.<br/>
     *
     * Ela serve para sabermos quais concern são passiveis de manipulação.<Br />
     *
     * Ex: Ao adicionar um concern em uma classe, o mesmo deve estar presente
     * nesta lista.
     *
     */
    private List<Aspect> allowedAspects = new ArrayList<Aspect>();

    /**
     * Procura concern por nome.
     *
     * Se o concern não estiver no arquivo de profile é lançada uma Exception.
     *
     * @param name
     * @return
     * @throws ConcernNotFoundException
     */
    public synchronized Aspect getOrCreateAspect(String name) throws ConcernNotFoundException {
        Aspect aspect = allowedAspectContains(name.toLowerCase());
        if (aspects.containsValue(aspect)) {
            return aspect;
        }
        if (aspect != null) {
            aspects.put(name.toLowerCase(), aspect);
            return aspect;
        }
        throw new ConcernNotFoundException("Aspect " + name + " cannot be found");
    }

    public synchronized HashMap<String, Aspect> getAspects() {
        return aspects;
    }

    private synchronized Aspect allowedAspectContains(String aspectName) {
        for (Aspect aspect : allowedAspects) {
            if (aspect.getName().equalsIgnoreCase(aspectName)) {
                return aspect;
            }
        }
        return null;
    }

    public synchronized Aspect getAspectByName(String aspectName) {
        return aspects.get(aspectName);
    }

    public synchronized List<Aspect> allowedAspects() {
        return allowedAspects;
    }

    public void clear() {
        aspects.clear();
    }

}
