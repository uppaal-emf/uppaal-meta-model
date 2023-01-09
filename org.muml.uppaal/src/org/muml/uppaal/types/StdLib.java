package org.muml.uppaal.types;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class StdLib {
	private static final URI STDLIB_URI = URI.createPlatformResourceURI("/org.muml.uppaal/model/sdtlib.xmi", false);
	private static final Library INSTANCE = getLibrary();
	
	public static final PredefinedType INT = getType(BuiltInType.INT.getLiteral());
	public static final PredefinedType BOOL = getType(BuiltInType.BOOL.getLiteral());
	public static final PredefinedType VOID = getType(BuiltInType.VOID.getLiteral());
	public static final PredefinedType CHAN = getType(BuiltInType.CHAN.getLiteral());
	public static final PredefinedType CLOCK = getType(BuiltInType.CLOCK.getLiteral());
	
	public static final Iterable<PredefinedType> ALL_TYPES = List.of(BOOL, INT, BOOL, VOID, CHAN, CLOCK);
	
	private static PredefinedType getType(final String name) {
		for (PredefinedType type : INSTANCE.getTypes()) {
			if (name.equals(type.getName())) {
				return type;
			}
		}
		
		throw new RuntimeException("Cannot find predefined type for name '" + name + "'");
	}

	private static Library getLibrary() {
		TypesPackage.eINSTANCE.eClass();
		
		ResourceSet resources = new ResourceSetImpl();
		Resource resource = resources.getResource(STDLIB_URI, true);
		
		return (Library) resource.getContents().get(0);
	}
}
