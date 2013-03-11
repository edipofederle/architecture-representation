package mestrado.arquitetura.factories;

import mestrado.arquitetura.exceptions.ModelIncompleteException;
import mestrado.arquitetura.exceptions.ModelNotFoundException;
import mestrado.arquitetura.exceptions.SMartyProfileNotAppliedToModelExcepetion;
import mestrado.arquitetura.helpers.test.TestHelper;

import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;

/**
 * 
 * @author edipofederle
 *
 */
public class Klass extends TestHelper {

		private static Classifier klasse;
		private static  Model model;
	
		String name;
		
		public static Klass create(){
			model = uml2Helper.createModel("arquitetura");
			klasse = uml2Helper.createClass(model, "classe1", false);
			return new Klass();
		}
		
		public Klass withName(String name){
			klasse.setName(name);
			return this;
		}
		
		public Klass withStereotypes(String ... stereotypes) throws ModelNotFoundException , ModelIncompleteException , SMartyProfileNotAppliedToModelExcepetion{
			Profile perfil = (Profile) givenAModel("smarty.profile"); // TODO Verificar isto.
			model.applyProfile(perfil);
			for (String sterotype : stereotypes)
				klasse.applyStereotype(perfil.getOwnedStereotype(sterotype));
			return this;
		}
		
		public Classifier build(){
			return klasse;
		}
	
}
