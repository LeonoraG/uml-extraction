package eu.scasefp7.eclipse.umlrec.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;

/**
 * Project aware handler for receiving the current project.
 * 
 * @author themis
 */
public abstract class ProjectAwareHandler extends AbstractHandler {

	/**
	 * Returns the project that the selected file(s) belong to.
	 * 
	 * @param selectionList the selected file(s).
	 * @return the project that the selected file(s) belong to.
	 */
	protected IProject getProjectOfSelectionList(List<Object> selectionList) {
		IProject project = null;
		for (Object object : selectionList) {
			IFile file = (IFile) Platform.getAdapterManager().getAdapter(object, IFile.class);
			IProject theproject = null;
			if (file != null) {
				theproject = file.getProject();
			} else {
				theproject = (IProject) Platform.getAdapterManager().getAdapter(object, IProject.class);
			}
			if (theproject != null) {
				if (project == null) {
					project = theproject;
				} else {
					if (!project.equals(theproject)) {
						return null;
					}
				}
			}
		}
		return project;
	}

	/**
	 * Finds the files of a container recursively.
	 * 
	 * @param container a workspace container (e.g. project or folder).
	 * @param files a list of files that is populated.
	 * @param extension the extension of the files that are to be retrieved.
	 */
	private void processContainer(IContainer container, ArrayList<IFile> files, String extension) {
		try {
			IResource[] members = container.members();
			for (IResource member : members) {
				if (member instanceof IContainer) {
					processContainer((IContainer) member, files, extension);
				} else if (member instanceof IFile) {
					if (extension.equals("") || ((IFile) member).getName().endsWith("." + extension))
						files.add((IFile) member);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the files of the given project.
	 * 
	 * @param project the project of which the files are returned.
	 * @param extension the extension of the returned files.
	 * @return a list of the files of the project with the given extension.
	 */
	protected ArrayList<IFile> getFilesOfProject(IProject project, String extension) {
		ArrayList<IFile> files = new ArrayList<IFile>();
		processContainer(project, files, extension);
		return files;
	}
}
