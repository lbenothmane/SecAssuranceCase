package seniordesign.features.shapeDefinitions;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.MultiText;
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

public class AddOvalEClass extends AbstractAddFeature implements IAddFeature
{

	private static final IColorConstant TEXT_COLOR = IColorConstant.BLACK;
	private static final IColorConstant FOREGROUND_COLOR = ColorConstant.BLACK;
	private static final IColorConstant BACKGROUND_COLOR = ColorConstant.LIGHT_BLUE;
	private static final int LINE_WIDTH = 3;

	public AddOvalEClass(IFeatureProvider fp)
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

		EClass ovalEClass = (EClass) context.getNewObject();
		Diagram targetDiagram = (Diagram) context.getTargetContainer();

		// This will put the eclass into a rectangle???
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape = peCreateService.createContainerShape(targetDiagram, true);

		int width = 100;
		IGaService gaService = Graphiti.getGaService();

		Ellipse oval = gaService.createEllipse(containerShape);
		oval.setForeground(manageColor(FOREGROUND_COLOR));
		oval.setBackground(manageColor(BACKGROUND_COLOR));
		oval.setLineWidth(LINE_WIDTH);
		gaService.setLocationAndSize(oval, context.getX(), context.getY(), context.getWidth(), context.getWidth());

		gaService.setHeight(oval, 50);
		gaService.setWidth(oval, 100);

		if (ovalEClass.eResource() == null)
		{
			getDiagram().eResource().getContents().add(ovalEClass);
		}

		link(containerShape, ovalEClass);

		{
			Shape shape = peCreateService.createShape(containerShape, false);

			// create and set text graphics algorithm
			Text text = gaService.createText(shape, ovalEClass.getName());
			text.setForeground(manageColor(TEXT_COLOR));
			text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
			// vertical alignment has as default value "center"
			text.setFont(gaService.manageDefaultFont(getDiagram(), false, true));
			gaService.setLocationAndSize(text, 0, 0, width, 20);

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
			gaService.setLocationAndSize(body, 0, 25, width, 100);

			// create link and wire it
			link(shape, ovalEClass);
		}

		peCreateService.createChopboxAnchor(containerShape);
		layoutPictogramElement(containerShape);

		return containerShape;

	}
}
