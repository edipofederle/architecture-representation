package arquitetura.touml;

import arquitetura.representation.Aspect;
import java.util.Set;

import arquitetura.representation.Class;
import arquitetura.representation.Concern;
import arquitetura.representation.Element;
import arquitetura.representation.Interface;

public class StereotypeOperations{
	
	private static final String PATTERNS = "patterns";
	private static final String CONCERNS = "concerns";
        //Inicio - Thaina 12/14 - Aspecto
        private static final String ASPECT = "aspect";
        //Fim - Thaina 12/14 - Aspecto
	private ElementXmiGenerator elementXmiGenerator;

	public StereotypeOperations(DocumentManager documentManager) {
		this.elementXmiGenerator = new ElementXmiGenerator(documentManager, null);
	}
	
	public StereotypeOperations withStereotypes(Set<Concern> concerns, String id) {
		if(!concerns.isEmpty()){
			for(final Concern concern : concerns)
				elementXmiGenerator.generateConcern(concern.getName(), id, CONCERNS);
		}
		return this;
	}
	
	public StereotypeOperations withStereotype(Concern concern, String id) {
		elementXmiGenerator.generateConcern(concern.getName(), id, CONCERNS);
		return this;
	}

	public void withPatternsStereotype(Element klass) {
		if(klass instanceof Class){
			if(((Class) klass).getPatternsOperations() != null){
				for(String pattern : ((Class) klass).getPatternsOperations().getAllPatterns())
					elementXmiGenerator.generateConcern(pattern, klass.getId(), PATTERNS);
			}
		}
		if(klass instanceof Interface){
			if(((Interface) klass).getPatternsOperations() != null){
				for(String pattern : ((Interface) klass).getPatternsOperations().getAllPatterns())
					elementXmiGenerator.generateConcern(pattern, klass.getId(), PATTERNS);
			}
		}
	}
        //Inicio - Thaina 12/14 - Aspecto
        	public StereotypeOperations withStereotypesAspect(Set<Aspect> aspects, String id) {
		if(!aspects.isEmpty()){
		for(final Aspect aspect : aspects)
			elementXmiGenerator.generateAspect(aspect.getName(), id, ASPECT);
		}
		return this;
	}
        //Fim - Thaina 12/14 - Aspecto
        
	
}