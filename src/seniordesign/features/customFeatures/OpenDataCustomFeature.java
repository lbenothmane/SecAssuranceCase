package seniordesign.features.customFeatures;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import Utility.Util;

public class OpenDataCustomFeature extends AbstractCustomFeature
{

	public OpenDataCustomFeature(IFeatureProvider fp)
	{
		super(fp);

	}

	@Override
	public boolean canExecute(ICustomContext context)
	{

		PictogramElement[] elements = context.getPictogramElements();
		Object obj = getBusinessObjectForPictogramElement(elements[0]);
		if (elements.length > 1 || !(obj instanceof EClass))
		{
			return false;
		}

		return true;
	}

	@Override
	public void execute(ICustomContext context)
	{
		PictogramElement element = context.getPictogramElements()[0];
		// Already know it's an EClass, since we check in canExecute
		EClass obj = (EClass) getBusinessObjectForPictogramElement(element);
		String oldName = obj.getInstanceClassName();
		String oldClassName = oldName.substring(0, oldName.indexOf((System.lineSeparator())));
		String getData = Util.askData(obj.getName(), "Enter Data:",
				oldName.substring(oldName.indexOf(System.lineSeparator())).trim());
		obj.setInstanceClassName( oldClassName + System.lineSeparator() + getData);

		//Util.writeData(obj.getName(), getData, getDiagram());
	}

}
