package seniordesign.features.customFeatures;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;

import Utility.Util;

public class RenameCustomFeature extends AbstractCustomFeature
{
	private boolean changes = false;

	public RenameCustomFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public String getName()
	{
		return "Rename";
	}

	@Override
	public String getDescription()
	{
		return "Rename this shape.";
	}

	@Override
	public boolean canExecute(ICustomContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		if (elements != null && elements.length == 1)
		{
			if (elements[0] instanceof EClass)
			{
				return true;
			}
		}
		return true;
	}

	@Override
	public void execute(ICustomContext context)
	{
		PictogramElement[] elements = context.getPictogramElements();
		EClass temp = (EClass) getBusinessObjectForPictogramElement(elements[0]);
		String oldName = temp.getName();
		String newName = Util.askString("Enter New Name", "New Name", temp.getName());
		Util.writeNewName(oldName, newName, getDiagram());
		changes = true;
		temp.setName(newName);		
		updatePictogramElement(elements[0]);
		
	}

	@Override
	public boolean hasDoneChanges()
	{
		return changes;
	}

}
