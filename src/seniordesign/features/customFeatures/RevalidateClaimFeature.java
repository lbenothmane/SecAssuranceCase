package seniordesign.features.customFeatures;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.ColorConstant;

public class RevalidateClaimFeature extends AbstractCustomFeature
{

	public RevalidateClaimFeature(IFeatureProvider fp)
	{
		super(fp);

	}

	@Override
	public String getName()
	{
		return "Revalidate Claim";
	}

	@Override
	public String getDescription()
	{
		return "Mark a claim as valid";
	}

	@Override
	public boolean canExecute(ICustomContext context)
	{
		boolean ret = false;
		PictogramElement[] pes = context.getPictogramElements();
		if (pes != null && pes.length >= 1)
		{
			ret = true;
			for (PictogramElement pe : pes)
			{
				Object bo = getBusinessObjectForPictogramElement(pe);
				if (!(bo instanceof EClass))
				{
					ret = false;
				}
			}
		}
		return ret;
	}

	public void execute(ICustomContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		for (int i = 0; i < elements.length; i++)
		{
			elements[0].getGraphicsAlgorithm().setBackground(manageColor(ColorConstant.LIGHT_BLUE));
		}
	}
}
