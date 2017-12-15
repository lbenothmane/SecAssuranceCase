package Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public class Configuration
{
	public String gitPath = "";

	public Configuration()
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		IWorkbenchPage activePage = window.getActivePage();

		IEditorPart activeEditor = activePage.getActiveEditor();
		IProject project = null;
		if (activeEditor != null)
		{
			IEditorInput input = activeEditor.getEditorInput();

			project = input.getAdapter(IProject.class);
			if (project == null)
			{
				IResource resource = input.getAdapter(IResource.class);
				if (resource != null)
				{
					project = resource.getProject();
				}
			}

		}
		

		File file = new File((project.getFile("/git-config.txt")).getLocationURI());
		try
		{
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				if (line.contains("git-path:"))
				{
					gitPath = line.substring(line.indexOf(":") + 1);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e)
		{
			System.out.println("No config file!!!");
			e.printStackTrace();
		}
	}

}
