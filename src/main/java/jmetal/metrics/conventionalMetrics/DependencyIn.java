package jmetal.metrics.conventionalMetrics;
import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.AbstractionRelationship;
import arquitetura.representation.relationship.Relationship;

public class DependencyIn {

	/**
	 * @param args
	 */
	private Architecture architecture;
	private int results;
	
public DependencyIn(Architecture architecture) {

		this.architecture = architecture;
		this.results = 0;
		int depIn = 0;
		
	//EDIPO - Mudei para packge
	//TODO - Refatorar
	for (Package component : this.architecture.getAllPackages()) {
		List<Relationship> relationships = new ArrayList<Relationship> (architecture.getAllRelationships());
		for (Relationship relationship : relationships) {
				if (relationship instanceof AbstractionRelationship){
					AbstractionRelationship dependency = (AbstractionRelationship) relationship;
					if (dependency.getSupplier().equals(component)) depIn++;
				}
		}
		this.results += depIn; // somatorio de DepIn da arquitetura como um todo
		depIn= 0;
	}
}

	public int getResults() {
		return results;
	}
	
}
