package eu.scasefp7.eclipse.umlrec.papyrus.handlers;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.progress.IProgressService;


import eu.scasefp7.eclipse.umlrec.papyrus.PapyrusGenerator;
import eu.scasefp7.eclipse.umlrec.papyrus.utils.DialogUtils;
import eu.scasefp7.eclipse.umlrec.papyrus.modelmanagers.DefaultPapyrusModelManager;

/**
 * A command handler for converting generated xmi files to Papyrus compatible files
 * @author mkoutli, tsirelis
 *
 */

public class ConvertToPapyrusHandler extends AbstractHandler{
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		ISelectionService service = window.getSelectionService();
		IStructuredSelection structured = (IStructuredSelection) service.getSelection();
		IFile file = null;		
		String filePath = event.getParameter("filePath");
		
		//When handler is called from wizard
		if(filePath != null){
			Path path = new Path(filePath);
			file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		}
		else if(structured.getFirstElement() instanceof IFile)
			file = (IFile) structured.getFirstElement();
		else
			return null;
		IProgressService progressService = PlatformUI.getWorkbench().getProgressService();

		PapyrusGenerator pg = new PapyrusGenerator(file, DefaultPapyrusModelManager.class);


		try {
			progressService.runInUI(PlatformUI.getWorkbench().getProgressService(), new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					try {
						pg.run(monitor);
					} catch (Exception e) {
						DialogUtils.errorMsgb("Papyrus Model generation Error", "Error occured during the generation process.", e);
						monitor.done();
					}
				}
			}, ResourcesPlugin.getWorkspace().getRoot()); // ResourcesPlugin.getWorkspace().getRoot()
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
