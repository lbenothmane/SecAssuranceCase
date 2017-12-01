package seniordesign.features;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.impl.DefaultResizeShapeFeature;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.styles.Point;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;

public class ResizeFeature extends DefaultResizeShapeFeature
{

	public ResizeFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public boolean canResizeShape(IResizeShapeContext context)
	{
		return getBusinessObjectForPictogramElement(context.getPictogramElement()) instanceof EClass;

	}

	@Override
	public void resizeShape(IResizeShapeContext context)
	{
		EClass eClass = (EClass) getBusinessObjectForPictogramElement(context.getShape());
		Shape toResize = context.getShape();
		IGaService alg = Graphiti.getGaService();
		String className = eClass.getInstanceClassName().substring(0, eClass.getInstanceTypeName().indexOf(System.lineSeparator()));

		PictogramElement element = context.getPictogramElement();
		if (element instanceof ContainerShape)
		{
			ContainerShape cs = (ContainerShape) element;
			for (Shape shape : cs.getChildren())
			{
				if (shape.getGraphicsAlgorithm() instanceof MultiText)
				{
					MultiText text = (MultiText) shape.getGraphicsAlgorithm();
					text.setHeight(context.getHeight());
					text.setWidth(context.getWidth());

				}
			}
		}

		// System.out.println(toResize.getGraphicsAlgorithm().getClass());
		if (className.equals("Parallelogram"))
		{
			Polygon poly = (Polygon) toResize.getGraphicsAlgorithm();
			EList<Point> list = poly.getPoints();
			toResize.getGraphicsAlgorithm().setX(context.getX());
			toResize.getGraphicsAlgorithm().setY(context.getY());
			list.get(1).setX(context.getWidth() - 25);
			list.get(2).setY(context.getHeight());
			list.get(2).setX(context.getWidth());
			list.get(3).setY(context.getHeight());

		} else if (className.equals("Circle"))
		{
			// TODO: This still has an issue in resizing when going smaller that
			// needs to be looked into and fixed.
			if (context.getWidth() > context.getHeight())
			{
				alg.setSize(toResize.getGraphicsAlgorithm(), context.getWidth(), context.getWidth());

			} else
			{
				alg.setSize(toResize.getGraphicsAlgorithm(), context.getHeight(), context.getHeight());
			}
			toResize.getGraphicsAlgorithm().setX(context.getX());
			toResize.getGraphicsAlgorithm().setY(context.getY());
		} else
		{
			alg.setSize(toResize.getGraphicsAlgorithm(), context.getWidth(), context.getHeight());
			toResize.getGraphicsAlgorithm().setX(context.getX());
			toResize.getGraphicsAlgorithm().setY(context.getY());
		}
		layoutPictogramElement(toResize);
	}
}
