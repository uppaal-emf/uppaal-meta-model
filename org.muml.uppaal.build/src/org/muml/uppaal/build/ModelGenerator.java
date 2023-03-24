package org.muml.uppaal.build;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.codegen.ecore.generator.Generator;
import org.eclipse.emf.codegen.ecore.generator.GeneratorAdapterFactory;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.codegen.ecore.genmodel.GenModelPackage;
import org.eclipse.emf.codegen.ecore.genmodel.generator.GenBaseGeneratorAdapter;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.mwe.utils.GenModelHelper;
import org.eclipse.emf.mwe2.runtime.Mandatory;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowComponent;
import org.eclipse.emf.mwe2.runtime.workflow.IWorkflowContext;
import org.eclipse.ocl.examples.build.utilities.ResourceUtils;
import org.eclipse.emf.codegen.ecore.genmodel.util.GenModelUtil;
import org.eclipse.ocl.examples.codegen.oclinecore.OCLinEcoreGeneratorAdapterFactory;
import org.eclipse.ocl.xtext.essentialocl.EssentialOCLStandaloneSetup;

import com.google.common.base.Optional;


public class ModelGenerator implements IWorkflowComponent {
	static {
		EcorePackage.eINSTANCE.getEFactoryInstance();
		GenModelPackage.eINSTANCE.getEFactoryInstance();
		
		EssentialOCLStandaloneSetup.doSetup();
		OCLinEcoreGeneratorAdapterFactory.install();
	}
	
	private static Logger log = Logger.getLogger(ModelGenerator.class);
	private static BasicMonitor logMonitor = new BasicMonitor() {
		public void subTask(String name) {
			log.info(name);
		}
	};
	
	private String genModel;
	private ResourceSet resourceSet;
	
	// loaded pre-invoke
	private Resource genModelResource;
	private List<Diagnostic> diagnostics;
	
	public ModelGenerator() {
		this.diagnostics = new LinkedList<>();
	}
	
	@Mandatory
	public void setGenModel(String genModel) {
		this.genModel = genModel;
	}
	
	public void setResourceSet(ResourceSet resourceSet) {
		this.resourceSet = resourceSet;
		// TODO: Registrations
	}
	
	public String getGenModel() {
		return this.genModel;
	}
	
	public ResourceSet getResourceSet() {
		return this.resourceSet;
	}

	@Override
	public void preInvoke() {
		diagnostics.clear();
		
		ResourceUtils.checkResourceSet(resourceSet);
		
		URI genModelURI = URI.createURI(getGenModel(), true);		
		genModelResource = resourceSet.getResource(genModelURI, true);
	}

	@Override
	public void invoke(IWorkflowContext ctx) {		
		//OCLGenModelUtil.initializeGeneratorAdapterFactoryRegistry();
		
		final GenModel genModel = GenModelUtil.getGenModel(genModelResource);
		genModel.setCanGenerate(true);
		genModel.reconcile();
		
		String modelDirectory = genModel.getModelDirectory();
		log.error("Directory: " +  genModel.getModelDirectory());
		
		URI locationURI = URI.createPlatformResourceURI(modelDirectory, true);
		ResourceSet resourceSet = genModel.eResource().getResourceSet();
		URIConverter uriConverter = resourceSet != null ? resourceSet.getURIConverter() : URIConverter.INSTANCE;
		URI normalizedURI = uriConverter.normalize(locationURI);
		
		log.error("FileString:" + normalizedURI.toFileString());
		
		final GenModelHelper helper = new GenModelHelper();
		helper.registerGenModel(genModel);
		
		final Generator generator = new Generator(GeneratorAdapterFactory.Descriptor.Registry.INSTANCE);
		generator.setInput(genModel);
		generator.requestInitialize();
				
		//generator.getAdapterFactoryDescriptorRegistry().
		log.info("Generating model code");
		diagnostics.add(generator.generate(genModel, GenBaseGeneratorAdapter.MODEL_PROJECT_TYPE, logMonitor));
		diagnostics.add(generator.generate(genModel, GenBaseGeneratorAdapter.EDIT_PROJECT_TYPE, logMonitor));
		diagnostics.add(generator.generate(genModel, GenBaseGeneratorAdapter.EDITOR_PROJECT_TYPE, logMonitor));	
	}

	@Override
	public void postInvoke() {
		for (Diagnostic d : diagnostics) {
			switch (d.getSeverity()) {
				case Diagnostic.ERROR:
					logDiagnostic(Level.ERROR, d);
					
					break;
				case Diagnostic.WARNING:
					log.warn(d);
					break;
				case Diagnostic.INFO:
					log.info(d);
					break;
				case Diagnostic.OK:
					log.debug(d);
					break;
				case Diagnostic.CANCEL:
					log.error(d);
					break;
			}
		}
	}
	
	private void logDiagnostic(Level priority, Diagnostic diagnostic) {
		log.log(priority, diagnostic.getMessage());
		Throwable throwable = diagnostic.getException();
		if(throwable != null) {
			log.log(priority, throwable.getMessage());
			throwable.printStackTrace();
		}
		
		for(Diagnostic child : diagnostic.getChildren()) {
			logDiagnostic(priority, child);
		}
	}
}
