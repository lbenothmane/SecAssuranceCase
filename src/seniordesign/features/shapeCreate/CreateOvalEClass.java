package seniordesign.features.shapeCreate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateContext;
import org.eclipse.graphiti.features.impl.AbstractCreateFeature;
import org.eclipse.graphiti.mm.pictograms.Diagram;

import Utility.Util;

public class CreateOvalEClass extends AbstractCreateFeature implements ICreateFeature
{
	String currentElementName = "Rationale";

	public CreateOvalEClass(IFeatureProvider fp)
	{
		super(fp, "Rationale (Oval)", "Creates a new oval");
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

		eClass.setInstanceClassName("Oval" + System.lineSeparator());

		addGraphicalRepresentation(context, eClass);
		//Util.writeNewShape(currentElementName, getDiagram());
		return new Object[]
		{ eClass };
	}
}
