package seniordesign.features.updateFeatures;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.IReason;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.impl.AbstractUpdateFeature;
import org.eclipse.graphiti.features.impl.Reason;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;

public class UpdateShapeFeature extends AbstractUpdateFeature
{

	public UpdateShapeFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public boolean canUpdate(IUpdateContext context)
	{

		Object shape = getBusinessObjectForPictogramElement(context.getPictogramElement());
		return (shape instanceof EClass);
	}

	@Override
	public IReason updateNeeded(IUpdateContext context)
	{

		String pictogramName = null;
		String businessName = null;

		PictogramElement element = context.getPictogramElement();
		if (element instanceof ContainerShape)
		{
			ContainerShape cs = (ContainerShape) element;
			for (Shape shape : cs.getChildren())
			{
				if (shape.getGraphicsAlgorithm() instanceof Text)
				{
					Text text = (Text) shape.getGraphicsAlgorithm();
					pictogramName = text.getValue();
				}
			}
		}

		Object bo = getBusinessObjectForPictogramElement(element);
		if (bo instanceof EClass)
		{
			EClass eClass = (EClass) bo;
			businessName = eClass.getName();
		}
		boolean updateNameNeeded = ((pictogramName == null && businessName != null) || (pictogramName != null && !pictogramName
				.equals(businessName)));
		if (updateNameNeeded)
		{
			return Reason.createTrueReason("Name is out of date");
		} else
		{
			return Reason.createFalseReason();
		}
	}

	@Override
	public boolean update(IUpdateContext context)
	{
		String businessName = null;
		PictogramElement element = context.getPictogramElement();
		EClass temp = (EClass) getBusinessObjectForPictogramElement(element);
		if (temp instanceof EClass)
		{

			businessName = temp.getName();
		}
		if (element instanceof ContainerShape)
		{
			ContainerShape cs = (ContainerShape) element;
			for (Shape shape : cs.getChildren())
			{
				if (shape.getGraphicsAlgorithm() instanceof Text)
				{
					Text text = (Text) shape.getGraphicsAlgorithm();
					text.setValue(businessName);
					return true;
				}
			}
		}
		return false;
	}
}
