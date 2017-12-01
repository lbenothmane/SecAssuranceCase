package seniordesign.diagram;

import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.tb.ContextMenuEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextMenuEntry;

import seniordesign.features.customFeatures.OpenDataCustomFeature;

public class ToolProvider extends DefaultToolBehaviorProvider
{

	public ToolProvider(IDiagramTypeProvider diagramTypeProvider)
	{
		super(diagramTypeProvider);
	}

	@Override
	public ICustomFeature getDoubleClickFeature(IDoubleClickContext context)
	{
		ICustomFeature openDiagram = new OpenDataCustomFeature(getFeatureProvider());
		if (openDiagram.canExecute(context)){
			return openDiagram;
		}
		return super.getDoubleClickFeature(context);
	}

	@Override
	public IContextMenuEntry[] getContextMenu(ICustomContext context)
	{
		// create a sub-menu for all custom features
		ContextMenuEntry subMenu = new ContextMenuEntry(null, context);
		subMenu.setText("Features");
		subMenu.setDescription("Custom features");
		// display sub-menu hierarchical or flat
		subMenu.setSubmenu(true);

		// create a menu-entry in the sub-menu for each custom feature
		ICustomFeature[] customFeatures = getFeatureProvider().getCustomFeatures(context);
		for (int i = 0; i < customFeatures.length; i++)
		{
			ICustomFeature customFeature = customFeatures[i];
			if (customFeature.isAvailable(context))
			{
				ContextMenuEntry menuEntry = new ContextMenuEntry(customFeature, context);
				subMenu.add(menuEntry);
			}
		}

		IContextMenuEntry menuOptions[] = new IContextMenuEntry[]
		{ subMenu };
		return menuOptions;

	}

}
