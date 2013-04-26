package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;
import mestrado.arquitetura.exceptions.InvalidMultiplictyForAssociationException;
import mestrado.arquitetura.exceptions.NodeNotFound;


public interface Transformation {
	void useTransformation() throws CustonTypeNotFound, NodeNotFound, InvalidMultiplictyForAssociationException;
}