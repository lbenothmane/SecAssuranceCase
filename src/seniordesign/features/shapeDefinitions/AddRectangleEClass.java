package seniordesign.features.shapeDefinitions;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.MultiText;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

public class AddRectangleEClass extends AbstractAddShapeFeature
{
	// set the colors
	private static final IColorConstant TEXT_COLOR = IColorConstant.BLACK;
	private static final IColorConstant FOREGROUND_COLOR = ColorConstant.BLACK;
	private static final IColorConstant BACKGROUND_COLOR = ColorConstant.LIGHT_BLUE;
	private static final int LINE_WIDTH = 3;

	public AddRectangleEClass(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context)
	{
		if (context.getNewObject() instanceof EClass)
		{
			if (context.getTargetContainer() instanceof Diagram)
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public PictogramElement add(IAddContext context)
	{
		EClass newRectangle = (EClass) context.getNewObject();
		Diagram targetDiagram = (Diagram) context.getTargetContainer();

		// This will put the eclass into a rectangle???
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

		int width = 100;
		int height = 50;
		IGaService gaService = Graphiti.getGaService();

		Rectangle rectangle;

		{
			// this creates the base of our rectangle shape
			rectangle = gaService.createRectangle(containerShape);
			rectangle.setForeground(manageColor(FOREGROUND_COLOR));
			rectangle.setBackground(manageColor(BACKGROUND_COLOR));
			rectangle.setLineWidth(LINE_WIDTH);
			gaService.setLocationAndSize(rectangle, context.getX(), context.getY(), width, height);

			if (newRectangle.eResource() == null)
			{
				getDiagram().eResource().getContents().add(newRectangle);
			}
			// create link and wire it
			link(containerShape, newRectangle);

		}

		// add title text
		{
			Shape shape = peCreateService.createShape(containerShape, false);
			Text text = gaService.createText(shape, newRectangle.getName());
			text.setForeground(manageColor(TEXT_COLOR));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			text.setFont(gaService.manageDefaultFont(getDiagram(), false, true));
			gaService.setLocationAndSize(text, 0, 0, width, 20);

		}
		// add line seperating title and body
		{
			Shape shape = peCreateService.createShape(containerShape, false);
			Polyline polyline = gaService.createPolyline(shape, new int[]
			{ 0, 20, width, 20 });
			polyline.setForeground(manageColor(FOREGROUND_COLOR));
			polyline.setLineWidth(LINE_WIDTH);
		}
		// add body text
		{
			Shape shape = peCreateService.createShape(containerShape, false);
			MultiText body = gaService.createMultiText(shape, "Body Text");
			body.setForeground(manageColor(TEXT_COLOR));
			body.setBackground(manageColor(BACKGROUND_COLOR));
			body.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			body.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
			body.setFont(gaService.manageDefaultFont(getDiagram(), false, true));
			gaService.setLocationAndSize(body, 0, 25, width, height - 25);
			
			link(shape, newRectangle);
		}

		peCreateService.createChopboxAnchor(containerShape);
		layoutPictogramElement(containerShape);

		return containerShape;
	}

}
