package Utility;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.ibm.wala.cast.js.ipa.callgraph.JSCallGraphUtil;
import com.ibm.wala.cast.js.test.JSCallGraphBuilderUtil;
import com.ibm.wala.cast.js.translator.CAstRhinoTranslatorFactory;
import com.ibm.wala.ipa.callgraph.CGNode;
import com.ibm.wala.ipa.callgraph.CallGraph;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;

public class WalaUtil
{

	public static boolean isClaimValid(String function, String[] changedFunctions, String file)
	{

		JSCallGraphUtil.setTranslatorFactory(new CAstRhinoTranslatorFactory());

		CallGraph cg = null;

		try
		{
			cg = JSCallGraphBuilderUtil.makeHTMLCG(new File(file).toURI().toURL());
		} catch (IllegalArgumentException | IOException | CancelException | WalaException e)
		{
			e.printStackTrace();
		}

		if (cg != null)
		{
			for (int i = 0; i < changedFunctions.length; i++)
			{
				if (changedFunctions[i].equals(function))
				{
					return false;
				}

				for (CGNode node : cg)
				{

					if (node.getMethod().getSignature().contains(changedFunctions[i]))
					{

						Iterator<CGNode> temp = cg.getPredNodes(node);
						while (temp.hasNext())
						{
							CGNode cur = temp.next();
							if (cur.getMethod().getSignature().contains(function))
							{

								return false;
							}

						}
					}
				}
			}
		} else
		{
			System.out.println("Callgraph was null; unable to verify claims");
		}
		return true;
	}
}
