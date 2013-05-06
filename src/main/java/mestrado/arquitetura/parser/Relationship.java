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
public interface Relationship {
	
	public Relationship createRelation(String name);
	public Relationship between(String idElement);
	public Relationship and(String idElement);
	public String build() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException;
	
}