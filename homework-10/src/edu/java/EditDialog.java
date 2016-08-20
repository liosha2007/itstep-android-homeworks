package edu.java;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class EditDialog extends JDialog {

	public interface IEditListener {

		void actionPerformed(Map<String, String> newValues, boolean editMode);

	}
	private final JPanel contentPanel = new JPanel();
	private Map<JLabel, JTextField> controls = new HashMap<JLabel, JTextField>();
	protected IEditListener editCallback = null;
	private boolean editMode = true;
	/**
	 * Create the dialog.
	 */
	public EditDialog(JFrame parent, String title, Map<String, String> data, IEditListener editCallback, boolean editMode) {
		super(parent, title, true);
		this.editCallback = editCallback;
		this.editMode = editMode;
	    if (parent != null) {
	      Dimension parentSize = parent.getSize(); 
	      Point p = parent.getLocation(); 
	      setLocation(p.x + parentSize.width / 4, p.y + parentSize.height / 4);
	    }
		setBounds(100, 100, 269, 460);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel scrollPane = new JPanel();
			scrollPane.setLayout(new BoxLayout(scrollPane, BoxLayout.Y_AXIS));
			contentPanel.add(new JScrollPane(scrollPane), BorderLayout.CENTER);
			{
				for (String key : data.keySet()) {
					JPanel panel = new JPanel(new GridLayout(1, 2));
					String value = data.get(key);

					JLabel lblNewLabel = new JLabel(key);
					panel.add(lblNewLabel);

					JTextField textField = new JTextField();
					if (value != null) {
						textField.setText(value);
					}
					
					panel.add(textField);

					scrollPane.add(panel);
					controls.put(lblNewLabel, textField);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btn_done = new JButton("Done");
				btn_done.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Map<String, String> newValues = new HashMap<String, String>();
						for (Map.Entry<JLabel, JTextField> entry : controls.entrySet()){
							newValues.put(entry.getKey().getText(), entry.getValue().getText());
						}
						if (EditDialog.this.editCallback != null) {
							EditDialog.this.editCallback.actionPerformed(newValues, EditDialog.this.editMode);
						}
						EditDialog.this.dispose();
					}
				});
				btn_done.setActionCommand("OK");
				buttonPane.add(btn_done);
				getRootPane().setDefaultButton(btn_done);
			}
			{
				JButton btn_cancel = new JButton("Cancel");
				btn_cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						EditDialog.this.dispose();
					}
				});
				btn_cancel.setActionCommand("Cancel");
				buttonPane.add(btn_cancel);
			}
		}
	}

}
