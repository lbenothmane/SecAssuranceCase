package seniordesign.features;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractPasteFeature;

public class PasteFeature extends AbstractPasteFeature
{

	public PasteFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public void paste(IPasteContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		Diagram diagram = (Diagram) elements[0];
		Object[] toPaste = getFromClipboard();
		for (int i = 0; i < toPaste.length; i++)
		{
			AddContext ac = new AddContext();
			ac.setLocation(context.getX(), context.getY());
			ac.setTargetContainer(diagram);
			addGraphicalRepresentation(ac, toPaste[i]);
		}

	}

	@Override
	public boolean canPaste(IPasteContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		if (elements.length > 1 || !(elements[0] instanceof Diagram))
		{
			return false;
		}
		return true;
	}

}
