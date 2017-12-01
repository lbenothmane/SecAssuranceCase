package seniordesign.features;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.datatypes.IDimension;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.impl.AbstractLayoutFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class LayoutRectangleEClass extends AbstractLayoutFeature
{	
	private static final int MIN_HEIGHT = 50;
	private static final int MIN_WIDTH = 100;
	
	public LayoutRectangleEClass(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public boolean canLayout(ILayoutContext context)
	{
		//This should only layout rectangle objects
		Object object = getBusinessObjectForPictogramElement(context.getPictogramElement());
		if(object instanceof EClass && ((EClass)object).getInstanceClassName().equals("Rectangle"))
			return true;
		
		return false;
	}

	@Override
	public boolean layout(ILayoutContext context)
	{
		boolean changed = false;
		ContainerShape containerShape = (ContainerShape) context.getPictogramElement();
		GraphicsAlgorithm containerGA = containerShape.getGraphicsAlgorithm();
		
		//see if height or width changed
		if(containerGA.getHeight() < MIN_HEIGHT)
		{
			containerGA.setHeight(MIN_HEIGHT);
			changed = true;
		}
		
		if(containerGA.getWidth() < MIN_WIDTH)
		{
			containerGA.setWidth(MIN_WIDTH);
			changed = true;
		}
		
		//we need to update the children
		int containerWidth = containerGA.getWidth();
		for(Shape shape : containerShape.getChildren())
		{
			GraphicsAlgorithm graphicsAlgorthim = shape.getGraphicsAlgorithm();
			IGaService gaService = Graphiti.getGaService();
			IDimension size = gaService.calculateSize(graphicsAlgorthim);
			
			if(containerWidth != size.getWidth())
			{
				//child is not updated
				if(graphicsAlgorthim instanceof Polyline)
				{
					Polyline polyline = (Polyline) graphicsAlgorthim;
					Point secondPoint = polyline.getPoints().get(1);
					Point newSecondPoint = gaService.createPoint(containerWidth, secondPoint.getY());
					polyline.getPoints().set(1, newSecondPoint); //here we have set the secondpoint further to extend the width
					changed = true;
				}
				else
				{
					//This will update children such as text
					gaService.setWidth(graphicsAlgorthim, containerWidth);
					changed = true;
				}
			}
			
		}
		
		return changed;
	}

}
