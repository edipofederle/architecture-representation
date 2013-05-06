package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;

/**
 * 
 * Interface com operações para Relacionamentos do tipo Dependency e Usage.
 * 
 * @author edipofederle
 *
 */
public interface Dependency {
	
	public Dependency createDependency(String name);
	public Dependency between(String idElement);
	public Dependency and(String idElement);
	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException;
	
}