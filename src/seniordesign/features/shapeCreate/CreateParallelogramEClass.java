package seniordesign.features.shapeCreate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;

import Utility.Util;

public class CreateParallelogramEClass extends AbstractCreateFeature
{
	String currentElementName = "Strategy";

	public CreateParallelogramEClass(IFeatureProvider fp)
	{
		super(fp, "Strategy (Parallelogram)", "Create Parallelogram");
	}

	@Override
	public boolean canCreate(ICreateContext context)
	{
		return context.getTargetContainer() instanceof Diagram;
	}

	@Override
	public Object[] create(ICreateContext context)
	{
		currentElementName = Util.askString("Element Name", "Element Name", currentElementName);

		if (currentElementName == null || currentElementName.trim().length() == 0)
			return EMPTY;

		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		getDiagram().eResource().getContents().add(eClass);
		eClass.setName(currentElementName);

		// set the eclass class name so we know it is for parallelogram
		eClass.setInstanceClassName("Parallelogram" + System.lineSeparator());

		addGraphicalRepresentation(context, eClass);
		//Util.writeNewShape(currentElementName, getDiagram());
		return new Object[]
		{ eClass };
	}

}
