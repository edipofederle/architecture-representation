package mestrado.arquitetura.base;

import org.eclipse.emf.ecore.resource.ResourceSet;

public abstract class Base {
	
	static InitializeResources resources;
	
	public Base(){
		if (resources == null)
			resources = InitializeResources.getInstance();
	}
	
	public static ResourceSet getResources(){
		return resources.getResources();
	}
	
	
	

}