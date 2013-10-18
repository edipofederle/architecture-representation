package jmetal.metrics.concernDrivenMetrics.concernDiffusion;

import arquitetura.representation.Architecture;
import arquitetura.representation.Class;
import arquitetura.representation.Concern;

public class CDAClassResult extends ConcernDiffusionResult<Class> {

	public CDAClassResult(Concern concern, Architecture architecture) {
		super(concern, architecture);
	}

	@Override
	protected void loadElements(Architecture architecture) {
		for (Class cls : architecture.getAllClasses()) {
			// TODO incluir quando implementar o metodo em ConcernDiffusionResult
			// || classContaisConcernViaInterface(cls) ))
			if (classContainsConcern(cls) || (classContainsConcernViaMethod(cls))) 
				getElements().add(cls);
		}
	}
	
}