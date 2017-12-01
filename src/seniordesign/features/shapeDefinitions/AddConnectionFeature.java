package seniordesign.features.shapeDefinitions;

import org.eclipse.graphiti.features.IAddFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddConnectionContext;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.IColorConstant;

public class AddConnectionFeature extends AbstractAddFeature implements IAddFeature
{

	public AddConnectionFeature(IFeatureProvider fp)
	{
		super(fp);
	}

	@Override
	public boolean canAdd(IAddContext context)
	{
		// TODO: check for right domain object instance below
		return context instanceof IAddConnectionContext /*
														 * &&
														 * context.getNewObject
														 * () instanceof
														 * DomainObjectConnection
														 */;
	}

	@Override
	public PictogramElement add(IAddContext context)
	{
		IAddConnectionContext addConContext = (IAddConnectionContext) context;
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		IGaService gaService = Graphiti.getGaService();

		Connection connection = peCreateService.createFreeFormConnection(getDiagram());
		connection.setStart(addConContext.getSourceAnchor());
		connection.setEnd(addConContext.getTargetAnchor());

		Polyline polyline = gaService.createPlainPolyline(connection);
		polyline.setForeground(manageColor(IColorConstant.BLACK));

		ConnectionDecorator arrow = peCreateService.createConnectionDecorator(connection, false, 1.0, true);

		Polyline arrowShape = gaService.createPolyline(arrow, new int[]
		{ -15, 10, 0, 0, -15, -10 });
		arrowShape.setForeground(manageColor(IColorConstant.BLACK));
		return connection;
	}

}
