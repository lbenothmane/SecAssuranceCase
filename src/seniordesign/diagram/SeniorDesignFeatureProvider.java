package seniordesign.diagram;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.ICopyFeature;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IDirectEditingFeature;
import org.eclipse.graphiti.features.ILayoutFeature;
import org.eclipse.graphiti.features.IPasteFeature;
import org.eclipse.graphiti.features.IResizeShapeFeature;
import org.eclipse.graphiti.features.IUpdateFeature;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.ICopyContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.context.ILayoutContext;
import org.eclipse.graphiti.features.context.IPasteContext;
import org.eclipse.graphiti.features.context.IResizeShapeContext;
import org.eclipse.graphiti.features.context.IUpdateContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.features.DefaultFeatureProvider;

import seniordesign.features.CopyFeature;
import seniordesign.features.LayoutDomainObjectFeature;
import seniordesign.features.LayoutOval;
import seniordesign.features.LayoutRectangleEClass;
import seniordesign.features.PasteFeature;
import seniordesign.features.ResizeFeature;
import seniordesign.features.customFeatures.AssociateDiagramCustomFeature;
import seniordesign.features.customFeatures.DrillDownCustomFeature;
import seniordesign.features.customFeatures.EditBodyCustomFeature;
import seniordesign.features.customFeatures.RenameCustomFeature;
import seniordesign.features.shapeCreate.CreateCircleEClass;
import seniordesign.features.shapeCreate.CreateOvalEClass;
import seniordesign.features.shapeCreate.CreateParallelogramEClass;
import seniordesign.features.shapeCreate.CreateRectangleEClass;
import seniordesign.features.shapeCreate.CreateReference;
import seniordesign.features.shapeDefinitions.AddCircleEClass;
import seniordesign.features.shapeDefinitions.AddConnectionFeature;
import seniordesign.features.shapeDefinitions.AddOvalEClass;
import seniordesign.features.shapeDefinitions.AddParallelogramEClass;
import seniordesign.features.shapeDefinitions.AddRectangleEClass;
import seniordesign.features.updateFeatures.UpdateShapeFeature;

public class SeniorDesignFeatureProvider extends DefaultFeatureProvider
{

	public SeniorDesignFeatureProvider(IDiagramTypeProvider dtp)
	{
		super(dtp);
	}

	@Override
	public ICreateFeature[] getCreateFeatures()
	{
		return new ICreateFeature[]
		{ new CreateCircleEClass(this), new CreateOvalEClass(this), new CreateRectangleEClass(this),
				new CreateParallelogramEClass(this) };
	}

	@Override
	public ICreateConnectionFeature[] getCreateConnectionFeatures()
	{
		return new ICreateConnectionFeature[]
		{ new CreateReference(this) };
	}

	@Override
	public IAddFeature getAddFeature(IAddContext context)
	{

		if (context instanceof IAddConnectionContext)
		{
			return new AddConnectionFeature(this);

		}

		else if (context.getNewObject() instanceof EClass)
		{
			EClass eClass = (EClass) context.getNewObject();
			String className = eClass.getInstanceClassName().substring(0, eClass.getInstanceTypeName().indexOf(System.lineSeparator()));
			if (className.equals("Parallelogram"))
				return new AddParallelogramEClass(this);
			else if (className.equals("Rectangle"))
				return new AddRectangleEClass(this);
			else if (className.equals("Circle"))
				return new AddCircleEClass(this);
			else if (className.equals("Oval"))
				return new AddOvalEClass(this);
		}

		return super.getAddFeature(context);
	}

	@Override
	public ICustomFeature[] getCustomFeatures(ICustomContext context)
	{
		return new ICustomFeature[]
		{ new RenameCustomFeature(this), new DrillDownCustomFeature(this), new AssociateDiagramCustomFeature(this) };
	}

	@Override
	public IUpdateFeature getUpdateFeature(IUpdateContext context)
	{
		PictogramElement pictogramElement = context.getPictogramElement();
		if (pictogramElement instanceof ContainerShape)
		{
			Object shape = getBusinessObjectForPictogramElement(pictogramElement);
			if (shape instanceof EClass)
			{
				return new UpdateShapeFeature(this);
			}
		}
		return super.getUpdateFeature(context);
	}

	@Override
	public IResizeShapeFeature getResizeShapeFeature(IResizeShapeContext context)
	{
		Object shape = getBusinessObjectForPictogramElement(context.getShape());
		if (shape instanceof EClass)
		{
			return new ResizeFeature(this);
		} else
		{
			return super.getResizeShapeFeature(context);
		}
	}

	@Override
	public IDirectEditingFeature getDirectEditingFeature(IDirectEditingContext context)
	{
		return new EditBodyCustomFeature(this);

	}

	@Override
	public ICopyFeature getCopyFeature(ICopyContext context)
	{
		return new CopyFeature(this);
	}

	@Override
	public IPasteFeature getPasteFeature(IPasteContext context)
	{
		return new PasteFeature(this);
	}

	@Override
	public ILayoutFeature getLayoutFeature(ILayoutContext context)
	{
		PictogramElement pictogramElement = context.getPictogramElement();
		Object object = getBusinessObjectForPictogramElement(pictogramElement);
		if (object instanceof EClass)
		{
			EClass eClass = (EClass) object;
			if (eClass.getInstanceClassName().equals("Rectangle"))
				return new LayoutRectangleEClass(this);
			if (eClass.getInstanceClassName().equals("Oval") || eClass.getInstanceClassName().equals("Circle"))
				return new LayoutOval(this);
		}

		// TODO: check for right domain object instances below
		if (context.getPictogramElement() instanceof ContainerShape /*
																	 * &&
																	 * getBusinessObjectForPictogramElement
																	 * (context.
																	 * getPictogramElement
																	 * ())
																	 * instanceof
																	 * <
																	 * DomainObject
																	 * >
																	 */)
		{
			return new LayoutDomainObjectFeature(this);
		}

		return super.getLayoutFeature(context);
	}
}
