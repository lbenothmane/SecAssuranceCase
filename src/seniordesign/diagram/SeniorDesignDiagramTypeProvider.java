package seniordesign.diagram;

import org.eclipse.graphiti.dt.AbstractDiagramTypeProvider;
import org.eclipse.graphiti.tb.IToolBehaviorProvider;

import seniordesign.diagram.ToolProvider;

public class SeniorDesignDiagramTypeProvider extends AbstractDiagramTypeProvider
{

	private IToolBehaviorProvider[] toolProviders;

	public SeniorDesignDiagramTypeProvider()
	{
		super();
		setFeatureProvider(new SeniorDesignFeatureProvider(this));
	}

	@Override
	public IToolBehaviorProvider[] getAvailableToolBehaviorProviders()
	{
		if (toolProviders == null)
		{
			toolProviders = new IToolBehaviorProvider[]
			{ new ToolProvider(this) };
		}

		return toolProviders;
	}
	@Override 
	public boolean isAutoUpdateAtStartup(){
		return true;
	}
	@Override 
	public boolean isAutoUpdateAtRuntime(){
		return true;
	}
	@Override 
	public boolean isAutoUpdateAtReset(){
		return true;
	}
}
