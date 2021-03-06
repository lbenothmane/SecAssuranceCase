package Utility;

//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
//import java.util.Scanner;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class Util
{
	public static String askString(String dialogTitle, String dialogMessage, String initialValue)
	{
		String output = null;
		Shell shell = getShell();
		InputDialog inputDialog = new InputDialog(shell, dialogTitle, dialogMessage, initialValue, null);

		int retDialog = inputDialog.open();
		if (retDialog == Window.OK)
		{
			output = inputDialog.getValue();
		}
		return output;
	}

	public static String askData(String title, String message, String initialVal)
	{
		Shell shell = getShell();
		CustomInputDialog text = new CustomInputDialog(shell, title, message, initialVal, null);

		int test = text.open();
		if (test == Window.OK)
		{
			return text.getValue();
		}

		return null;
	}

	private static Shell getShell()
	{
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}

	public static Collection<Diagram> getDiagrams(IProject p)
	{
		final List<IFile> files = getDiagramFiles(p);
		final List<Diagram> diagramList = new ArrayList<Diagram>();
		final ResourceSet rSet = new ResourceSetImpl();
		for (final IFile file : files)
		{
			final Diagram diagram = getDiagramFromFile(file, rSet);
			if (diagram != null)
			{
				diagramList.add(diagram);
			}
		}
		return diagramList;
	}

	private static List<IFile> getDiagramFiles(IContainer folder)
	{
		final List<IFile> ret = new ArrayList<IFile>();
		try
		{
			final IResource[] members = folder.members();
			for (final IResource resource : members)
			{
				if (resource instanceof IContainer)
				{
					ret.addAll(getDiagramFiles((IContainer) resource));
				} else if (resource instanceof IFile)
				{
					final IFile file = (IFile) resource;
					if (file.getName().endsWith(".diagram"))
					{
						ret.add(file);
					}
				}
			}
		} catch (final CoreException e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	private static Diagram getDiagramFromFile(IFile file, ResourceSet resourceSet)
	{
		final URI resourceURI = getFileURI(file, resourceSet);
		Resource resource;
		try
		{
			resource = resourceSet.getResource(resourceURI, true);
			if (resource != null)
			{
				final EList<EObject> contents = resource.getContents();
				for (final EObject object : contents)
				{
					if (object instanceof Diagram)
					{
						return (Diagram) object;
					}
				}
			}
		} catch (final WrappedException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private static URI getFileURI(IFile file, ResourceSet resourceSet)
	{
		final String pathName = file.getFullPath().toString();
		URI resourceURI = URI.createFileURI(pathName);
		resourceURI = resourceSet.getURIConverter().normalize(resourceURI);
		return resourceURI;
	}
}
