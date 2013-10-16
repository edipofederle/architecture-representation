package jmetal.metrics.conventionalMetrics;
import java.util.ArrayList;
import java.util.List;

import arquitetura.representation.Architecture;
import arquitetura.representation.Interface;
import arquitetura.representation.Package;


public class MeanDepComponents {

	/**
	 * @param args
	 */
	private Architecture architecture;
	private double results;
	
	public MeanDepComponents(Architecture architecture) {
		
		this.architecture = architecture;
		this.results = 0;
		List<Package> depComponents = new ArrayList<Package>();
		int totalComponents = this.architecture.getAllPackages().size();
		int totalDependencies =0;
		
		for (Package component : architecture.getAllPackages()) {
			depComponents.clear();
			for (Interface itf: component.getRequiredInterfaces()){
				for (Package c: itf.getImplementors()){
			    	if (!(depComponents.contains(c))) depComponents.add(c);
			    }
			    
			}
			totalDependencies += depComponents.size();
		    
		}
		if (totalComponents != 0 ){
			this.results = totalDependencies / totalComponents;
		}
	}

	public double getResults() {
		return results;
	}
	
}
