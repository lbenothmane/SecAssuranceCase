package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class Main
{
	
	public static void main(String args[])
	{
		File file = new File("");
		System.out.println(file.getAbsolutePath());
		String pathToGitDir = file.getAbsolutePath() + "/test";
		try
		{
			FileRepositoryBuilder builder = new FileRepositoryBuilder();
			Repository repository = builder.setGitDir(new File(file.getAbsolutePath() + "/test/.git")).readEnvironment().build();
			Git git = new Git(repository);

			/*for (RevCommit commit : git.log().call()) {
				System.out.println(commit.getAuthorIdent().getName());
				System.out.println(commit.getAuthorIdent().getWhen());
				System.out.println(commit.getFullMessage());
			}*/
			//Iterable<RevCommit> iterable = git.log().call();
			
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
				EditList edits = df.toFileHeader(entry).toEditList();
				System.out.println(edits);
				//System.out.println(edits.get(0).getBeginB() + " " + edits.get(0).getEndB());
				fileChanges.add(new FileChange(entry.getNewPath(), edits, pathToGitDir));
			}

			for(FileChange fc : fileChanges) 
			{
				System.out.println(fc.getFilePath() + fc.getFileName());
				for(Function function : fc.getModifiedFunctions()) 
				{
					System.out.println(function.getName() + " " + function.getStartingLineNumber() + " " + function.getEndingLineNumber());
				}
			}
			
			
			
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} /*catch (NoHeadException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
