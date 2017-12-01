package seniordesign.features.customFeatures;

import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.AbstractDrillDownFeature;
import org.eclipse.core.resources.*;

import Utility.Util;

public class DrillDownCustomFeature extends AbstractDrillDownFeature
{

	public DrillDownCustomFeature(IFeatureProvider fp)
	{
		super(fp);

	}

	@Override
	public String getName()
	{
		return "Open Linked Diagram";
	}

	@Override
	public String getDescription()
	{
		return "Open the diagram linked to this node";
	}

	@Override
	public boolean canExecute(ICustomContext context)
	{
		PictogramElement[] selectedElements = context.getPictogramElements();
		if (selectedElements != null && selectedElements.length == 1)
		{
			Object object = getBusinessObjectForPictogramElement(selectedElements[0]);
			if (object instanceof EClass)
			{
				return super.canExecute(context);
			}
		}
		return false;
	}

	@Override
	protected Collection<Diagram> getDiagrams()
	{
		Collection<Diagram> result = Collections.emptyList();
		Resource resource = getDiagram().eResource();
		URI uri = resource.getURI();
		URI uriTrimmed = uri.trimFragment();
		if (uriTrimmed.isPlatformResource())
		{
			String platformString = uriTrimmed.toPlatformString(true);
			IResource fileResource = ResourcesPlugin.getWorkspace().getRoot().findMember(platformString);
			if (fileResource != null)
			{
				IProject project = fileResource.getProject();
				result = Util.getDiagrams(project);
			}
		}
		return result;
	}
}
