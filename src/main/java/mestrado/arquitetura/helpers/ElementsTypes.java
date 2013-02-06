package mestrado.arquitetura.helpers;

import org.eclipse.uml2.uml.internal.impl.AssociationImpl;
import org.eclipse.uml2.uml.internal.impl.ClassImpl;
import org.eclipse.uml2.uml.internal.impl.CommentImpl;
import org.eclipse.uml2.uml.internal.impl.DependencyImpl;
import org.eclipse.uml2.uml.internal.impl.InterfaceImpl;
import org.eclipse.uml2.uml.internal.impl.PackageImpl;

public class ElementsTypes {

	public static final Class<ClassImpl> CLASS = ClassImpl.class;
	public static final Class<InterfaceImpl> INTERFACE = InterfaceImpl.class;
	public static final Class<AssociationImpl> ASSOCIATION = AssociationImpl.class;
	public static final Class<DependencyImpl> DEPENDENCY = DependencyImpl.class;
	public static final Class<PackageImpl> PACKAGE = PackageImpl.class;
	public static final Class<CommentImpl> COMMENT = CommentImpl.class;

}