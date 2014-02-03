package arquitetura.touml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import arquitetura.helpers.UtilResources;

public class RelationshipsXMI {
	
//	<eAnnotations xmi:type="ecore:EAnnotation" xmi:id="_mEg_AImZEeOgtrpexJcSmg" source="Stereotype_Annotation">
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueMImbEeOgtrpexJcSmg" key="StereotypeWithQualifiedNameList" value=""/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueMYmbEeOgtrpexJcSmg" key="StereotypeList" value=",profile::Pattern1"/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueMombEeOgtrpexJcSmg" key="Stereotype_Presentation_Kind" value="HorizontalStereo"/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueM4mbEeOgtrpexJcSmg" key="PropStereoDisplay" value=""/>
//	<details xmi:type="ecore:EStringToStringMapEntry" xmi:id="_wVueNImbEeOgtrpexJcSmg" key="StereotypePropertyLocation" value="With brace"/>
//	</eAnnotations>

	public static void enableVisibleStereotypes(Document docNotation) {
		Element eAnnotations =docNotation.createElement("eAnnotations");
		
		eAnnotations.setAttribute("xmi:type", "core:EAnnotation");
		eAnnotations.setAttribute("xmi:id", UtilResources.getRandonUUID());
		eAnnotations.setAttribute("source", "Stereotype_Annotation");
		
		
	}

}
