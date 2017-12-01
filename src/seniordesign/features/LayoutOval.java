package seniordesign.features;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

/**
 * This class also represents a circle layout 
 * The argument of whether a circle is a oval will not be tolorated :D
 */
public class LayoutOval extends AbstractLayoutFeature
{
	private static final int MIN_HEIGHT_OVAL = 50;
	private static final int MIN_WIDTH_OVAL = 100;
	private static final int MIN_HEIGHT_CIRCLE = 100;
	private static final int MIN_WIDTH_CIRCLE = 100;

	public LayoutOval(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context)
	{
		//this should only layout oval and circle objects
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if(object instanceof EClass && (((EClass)object).getInstanceClassName().equals("Oval") || ((EClass)object).getInstanceClassName().equals("Circle")))
			return true;
		
		return false;
	}

	@Override
	public boolean layout(ILayoutContext context)
	{
		boolean changed = false; //tells Graphiti if it needs to update the object or not
		
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGA = containerShape.getGraphicsAlgorithm();
		EClass eClass = (EClass) getBusinessObjectForPictogramElement(containerShape);
		
		if(eClass.getInstanceClassName().equals("Circle"))
		{
			if(containerGA.getHeight() < MIN_HEIGHT_CIRCLE)
			{
				containerGA.setHeight(MIN_HEIGHT_CIRCLE);
				changed = true;
			}
			
			if(containerGA.getWidth() < MIN_WIDTH_CIRCLE)
			{
				containerGA.setWidth(MIN_WIDTH_CIRCLE);
				changed = true;
			}
		}
		else
		{
			if(containerGA.getHeight() < MIN_HEIGHT_OVAL)
			{
				containerGA.setHeight(MIN_HEIGHT_OVAL);
				changed = true;
			}
			
			if(containerGA.getWidth() < MIN_WIDTH_OVAL)
			{
				containerGA.setWidth(MIN_WIDTH_OVAL);
				changed = true;
			}
		}
		
		//update the children with the new size
		int newWidth = containerGA.getWidth();
		for(Shape shape : containerShape.getChildren())
		{
			GraphicsAlgorithm graphicsAlgorthim = shape.getGraphicsAlgorithm();
			IGaService gaService = Graphiti.getGaService();
			gaService.setWidth(graphicsAlgorthim, newWidth);
		}
		
		return changed;
	}

}
