package seniordesign.features.customFeatures;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class EditBodyCustomFeature extends AbstractDirectEditingFeature
{

	public EditBodyCustomFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public int getEditingType()
	{
		return TYPE_TEXT;
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context)
	{
		PictogramElement element = context.getPictogramElement();
		Object businessObject = getBusinessObjectForPictogramElement(element);
		if (businessObject instanceof EClass && context.getGraphicsAlgorithm() instanceof MultiText)
		{
			return true;
		}
		return false;
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context)
	{
		return null;
	}

	@Override
	public String getInitialValue(IDirectEditingContext context)
	{
		GraphicsAlgorithm temp = context.getGraphicsAlgorithm();
		MultiText toEdit = (MultiText) temp;
		return toEdit.getValue();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context)
	{
		GraphicsAlgorithm temp = context.getGraphicsAlgorithm();
		MultiText toEdit = (MultiText) temp;
		toEdit.setValue(value);
	}

}
