package seniordesign.features;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractCopyFeature;

public class CopyFeature extends AbstractCopyFeature
{

	public CopyFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public void copy(ICopyContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		Object[] objects = new Object[elements.length];
		for (int i = 0; i < objects.length; i++)
		{
			objects[i] = (EClass) getBusinessObjectForPictogramElement(elements[i]);
		}
		putToClipboard(objects);
	}

	@Override
	public boolean canCopy(ICopyContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		if (elements == null || elements.length < 1)
		{
			return false;
		} else
		{
			for (int i = 0; i < elements.length; i++)
			{
				if (!(getBusinessObjectForPictogramElement(elements[i]) instanceof EClass))
				{
					return false;
				}
			}
			return true;
		}
	}
}
