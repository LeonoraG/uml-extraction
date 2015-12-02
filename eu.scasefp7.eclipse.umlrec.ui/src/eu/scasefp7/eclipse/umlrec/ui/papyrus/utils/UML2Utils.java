package eu.scasefp7.eclipse.umlrec.ui.papyrus.utils;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.uml2.uml.Model;

public class UML2Utils {

	public static Model loadModel(String modelUri) throws WrappedException
	{
		return loadModel(URI.createPlatformResourceURI(modelUri, false));
	}

	public static Model loadModel(URI modelUri) throws WrappedException
	{
		ResourceSet resSet = new ResourceSetImpl();
		Resource resource = resSet.getResource(modelUri, true);
		Model model = null;
		if(resource.getContents().size() != 0) 
			model = (Model) resource.getContents().get(0);
		return model;
	}
}
