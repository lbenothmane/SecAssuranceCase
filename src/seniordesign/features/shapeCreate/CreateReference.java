package seniordesign.features.shapeCreate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICreateConnectionContext;
import org.eclipse.graphiti.features.context.impl.AddConnectionContext;
import org.eclipse.graphiti.features.impl.AbstractCreateConnectionFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.Connection;

/**
 * This class is what allows any object created using a EClass to connect to each other
 *
 */
public class CreateReference extends AbstractCreateConnectionFeature
{
	public CreateReference(IFeatureProvider fp)
	{
		super(fp, "Connection", "Create Reference");
	}

	@Override
	public boolean canCreate(ICreateConnectionContext context)
	{
		EClass source = getEClass(context.getSourceAnchor());
		EClass target = getEClass(context.getTargetAnchor());
		
		if(source != null && target != null && source != target)
		{
			return true;
		}
		return false;
	}

	@Override
	public Connection create(ICreateConnectionContext context)
	{
		Connection newConnection = null;
		
		//get EClasses that will get connected
		EClass source = getEClass(context.getSourceAnchor());
		EClass target = getEClass(context.getTargetAnchor());
		
		if(source != null && target != null)
		{
			//create the reference
			EReference eRef = createEReference(source, target);
			
			//use it to create the connection
			AddConnectionContext addContext = new AddConnectionContext(context.getSourceAnchor(), context.getTargetAnchor());
			addContext.setNewObject(eRef);
			newConnection = (Connection) getFeatureProvider().addIfPossible(addContext);
		}
		
		return newConnection;
	}

	@Override
	public boolean canStartConnection(ICreateConnectionContext context)
	{
		if(getEClass(context.getSourceAnchor()) != null)
		{
			return true;
		}
		return false;
	}
	
	//returns the EClass associated with the anchor
	private EClass getEClass(Anchor anchor)
	{
		if (anchor != null)
		{
			Object object = getBusinessObjectForPictogramElement(anchor.getParent());
			if (object instanceof EClass)
			{
				return (EClass) object;
			}
		}
		return null;
	}
	
	//makes an ERefernce between two EClasses (Should work if we extend EClass) TODO: If my idea is right, put this in a utility class
	private EReference createEReference(EClass source, EClass target)
	{
		EReference eReference = EcoreFactory.eINSTANCE.createEReference();
		eReference.setName("new reference");
		eReference.setEType(target);
		eReference.setLowerBound(0);
		eReference.setUpperBound(1);
		source.getEStructuralFeatures().add(eReference);
		return eReference;
	}
	
}
