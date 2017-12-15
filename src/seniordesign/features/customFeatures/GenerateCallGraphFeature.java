package seniordesign.features.customFeatures;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import Utility.Configuration;
import Utility.FileChange;
import Utility.WalaUtil;

public class GenerateCallGraphFeature extends AbstractCustomFeature
{

	public GenerateCallGraphFeature(IFeatureProvider fp)
	{
		super(fp);

	}

	@Override
	public String getName()
	{
		return "Generate Callgraph";
	}

	@Override
	public String getDescription()
	{
		return "Generate Call Graph";
	}

	@Override
	public boolean canExecute(ICustomContext context)
	{
		return true;
	}

	public void execute(ICustomContext context)
	{
		Configuration config = new Configuration();
		File file = new File(config.gitPath + "\\.git");
		FileRepositoryBuilder builder = new FileRepositoryBuilder();
		try
		{
			
			Repository repository = builder.setGitDir(file).readEnvironment().build();
			Git git = new Git(repository);
			
			ObjectReader reader = git.getRepository().newObjectReader();
			CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
			ObjectId oldTree = git.getRepository().resolve("HEAD~1^{tree}"); // back x amount
			oldTreeIter.reset(reader, oldTree);
			CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
			ObjectId newTree = git.getRepository().resolve("HEAD^{tree}"); // latest
			newTreeIter.reset(reader, newTree);
			
			
			DiffFormatter df = new DiffFormatter(new ByteArrayOutputStream());
			df.setRepository(git.getRepository());
			List<DiffEntry> entries = df.scan(oldTreeIter, newTreeIter);
			ArrayList<FileChange> fileChanges = new ArrayList<FileChange>();
			
			for (DiffEntry entry : entries)
			{
				if(entry.getChangeType() == ChangeType.MODIFY) 
				{
					EditList edits = df.toFileHeader(entry).toEditList();
					//System.out.println(edits);
					//System.out.println(edits.get(0).getBeginB() + " " + edits.get(0).getEndB());
					fileChanges.add(new FileChange(entry.getNewPath(), edits, config.gitPath));
				}
			}
			
			for(FileChange fc : fileChanges) 
			{
				String codeFile = fc.getFullPath();
				System.out.println(codeFile);
				String changedMethods[] = new String[1];
				changedMethods = fc.getModifiedFunctionNames().toArray(changedMethods);
				EList<EObject> list = this.getDiagram().eContents();
				ArrayList<PictogramElement> pes = new ArrayList<PictogramElement>();
				for (int i = 0; i < list.size(); i++)
				{
					if (list.get(i) instanceof PictogramElement)
					{

						pes.add((PictogramElement) list.get(i));
					}
				}

				for (int i = 0; i < pes.size(); i++)
				{
					Object bo = getBusinessObjectForPictogramElement(pes.get(i));
					if ((bo instanceof EClass))
					{
						EClass obj = (EClass) bo;
						String oldName = obj.getInstanceClassName();

						// The claim's entry function
						String entryFunc = oldName.substring(oldName.indexOf(System.lineSeparator())).trim();

						boolean valid = WalaUtil.isClaimValid(entryFunc, changedMethods, codeFile);
						if (!(valid))
						{
							pes.get(i).getGraphicsAlgorithm().setBackground(manageColor(ColorConstant.RED));
						}
					}

				}
			}
		} catch (IOException e)
		{
			System.out.println("ISSUE OPENING UP GIT");
			e.printStackTrace();
		}
		
/*		String dir = System.getProperty("user.dir") + "/jsResources/";
		String htmlfile = dir + "testing.html";

		String changedMethods[] =
		{ "buzz" };
		EList<EObject> list = this.getDiagram().eContents();
		ArrayList<PictogramElement> pes = new ArrayList<PictogramElement>();
		for (int i = 0; i < list.size(); i++)
		{
			if (list.get(i) instanceof PictogramElement)
			{

				pes.add((PictogramElement) list.get(i));
			}
		}

		for (int i = 0; i < pes.size(); i++)
		{
			Object bo = getBusinessObjectForPictogramElement(pes.get(i));
			if ((bo instanceof EClass))
			{
				EClass obj = (EClass) bo;
				String oldName = obj.getInstanceClassName();

				// The claim's entry function
				String entryFunc = oldName.substring(oldName.indexOf(System.lineSeparator())).trim();

				boolean valid = WalaUtil.isClaimValid(entryFunc, changedMethods, htmlfile);
				if (!(valid))
				{
					pes.get(i).getGraphicsAlgorithm().setBackground(manageColor(ColorConstant.RED));
				}
			}

		}*/
	}
}
