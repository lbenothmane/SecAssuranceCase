package seniordesign.features.customFeatures;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

public class AssociateDiagramCustomFeature extends AbstractCustomFeature
{

	public AssociateDiagramCustomFeature(IFeatureProvider fp)
	{
		super(fp);

	}

	@Override
	public String getName()
	{
		return "Associate Diagram";
	}

	@Override
	public String getDescription()
	{
		return "Associate this diagram with another";
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
		PictogramElement[] pes = context.getPictogramElements();
		EClass eClasses[] = new EClass[pes.length];
		for (int i = 0; i < eClasses.length; i++)
		{
			eClasses[i] = (EClass) getBusinessObjectForPictogramElement(pes[i]);
		}
		link(getDiagram(), eClasses);
	}
}
