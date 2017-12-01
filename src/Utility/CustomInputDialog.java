package Utility;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CustomInputDialog extends InputDialog
{

	public CustomInputDialog(Shell parentShell, String dialogTitle, String dialogMessage, String initialValue,
			IInputValidator validator)
	{
		super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
	}

	@Override
	protected Control createDialogArea(Composite parent)
	{
		Control area = super.createDialogArea(parent);
		Text text = getText();
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		data.heightHint = convertHeightInCharsToPixels(8);
		text.setLayoutData(data);

		return area;
	}

	@Override
	protected int getInputTextStyle()
	{
		return SWT.MULTI | SWT.BORDER;
	}

}
