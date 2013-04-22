package mestrado.arquitetura.parser;

import mestrado.arquitetura.exceptions.CustonTypeNotFound;


public interface Transformation {
	void useTransformation() throws CustonTypeNotFound;
}