package jmetal.metrics.conventionalMetrics;
import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Architecture;
import arquitetura.representation.Package;
import arquitetura.representation.relationship.DependencyRelationship;
import arquitetura.representation.relationship.Relationship;

public class DependencyOut {

	private int results;
	
  public DependencyOut(Architecture architecture) {

		this.results = 0;
		int depOut = 0;
		
	// TODO Refatorar - metodos recuperar relacionamento
	for (Package component : architecture.getAllPackages()) {
		List<Relationship> relationships = new ArrayList<Relationship> (architecture.getAllRelationships());
		for (Relationship relationship : relationships) {
				if (relationship instanceof DependencyRelationship){
					DependencyRelationship dependency = (DependencyRelationship) relationship;
					if (dependency.getClient().equals(component)) depOut++;
				}
		}
		this.results += depOut; // somatorio de DepOut da arquitetura como um todo
		depOut= 0;
	}
  }

	public int getResults() {
		return results;
	}

}
