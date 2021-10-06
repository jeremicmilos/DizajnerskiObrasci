package modification;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import geometry.Circle;
import geometry.Line;
import geometry.Point;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DlgLineModify extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JButton btnConfirm;
	private JButton btnCancel;
	private JButton btnColor;
	private JTextField txtStartXcoordinate;
	private JTextField txtStartYcoordinate;
	private JTextField txtEndXcoordinate;
	private JTextField txtEndYcoordinate;
	private Color color = new Color(0, 0, 0);
	private boolean isConfirmed;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DlgLineModify dialog = new DlgLineModify();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DlgLineModify() {
		setBounds(100, 100, 450, 400);
		setTitle("Line modify");
		setResizable(false);
		setModal(true);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		JLabel lblStartXcoordinate = new JLabel("Start point X coordinate:");
		JLabel lblStartYcoordinate = new JLabel("Start point Y coordinate:");
		JLabel lblEndXcoordinate = new JLabel("End point X coordinate");
		JLabel lblEndYcoordinate = new JLabel("End point Y coordinate");
		btnColor = new JButton("Select color");
		btnColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				color = JColorChooser.showDialog(null, "Color", color);
				if(color != null)
				{
					btnColor.setBackground(color);
				}
			}
		});
		txtStartXcoordinate = new JTextField();
		txtStartXcoordinate.setColumns(10);
		txtStartYcoordinate = new JTextField();
		txtStartYcoordinate.setColumns(10);
		txtEndXcoordinate = new JTextField();
		txtEndXcoordinate.setColumns(10);
		txtEndYcoordinate = new JTextField();
		txtEndYcoordinate.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(39)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addComponent(btnColor, GroupLayout.PREFERRED_SIZE, 352, GroupLayout.PREFERRED_SIZE)
							.addContainerGap())
						.addGroup(gl_contentPanel.createSequentialGroup()
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblStartXcoordinate)
								.addComponent(lblEndYcoordinate)
								.addComponent(lblEndXcoordinate)
								.addComponent(lblStartYcoordinate))
							.addPreferredGap(ComponentPlacement.RELATED, 103, Short.MAX_VALUE)
							.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(txtStartXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtStartYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtEndXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtEndYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addContainerGap(85, Short.MAX_VALUE))))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addGap(52)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(txtStartXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblStartXcoordinate))
					.addGap(30)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStartYcoordinate)
						.addComponent(txtStartYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(34)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblEndXcoordinate)
						.addComponent(txtEndXcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(26)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblEndYcoordinate)
						.addComponent(txtEndYcoordinate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(51)
					.addComponent(btnColor, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(37, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				btnConfirm = new JButton("Confirm");
				btnConfirm.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							if(txtStartXcoordinate.getText().trim().equals("") || txtStartYcoordinate.getText().trim().equals("") ||
									txtEndXcoordinate.getText().trim().equals("") || txtEndYcoordinate.getText().trim().equals(""))
							{
								JOptionPane.showMessageDialog(null, "Values must be field!", "Error", JOptionPane.ERROR_MESSAGE, null);
								return;
							} else {
								isConfirmed = true;
								setVisible(false);
							}
							
							
							
						} catch (NumberFormatException e2) {
							JOptionPane.showMessageDialog(null, "Invalid data type!", "Error", JOptionPane.ERROR_MESSAGE, null);
						}
						
						
					}
				});
				btnConfirm.setActionCommand("OK");
				getRootPane().setDefaultButton(btnConfirm);
			}
			{
				btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCancel.setActionCommand("Cancel");
			}
			GroupLayout gl_buttonPane = new GroupLayout(buttonPane);
			gl_buttonPane.setHorizontalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(86)
						.addComponent(btnConfirm)
						.addGap(150)
						.addComponent(btnCancel)
						.addGap(88))
			);
			gl_buttonPane.setVerticalGroup(
				gl_buttonPane.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_buttonPane.createSequentialGroup()
						.addGap(5)
						.addGroup(gl_buttonPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnConfirm)
							.addComponent(btnCancel)))
			);
			buttonPane.setLayout(gl_buttonPane);
		}
	}

	public JTextField getTxtStartXcoordinate() {
		return txtStartXcoordinate;
	}

	public JTextField getTxtStartYcoordinate() {
		return txtStartYcoordinate;
	}

	public JTextField getTxtEndXcoordinate() {
		return txtEndXcoordinate;
	}

	public JTextField getTxtEndYcoordinate() {
		return txtEndYcoordinate;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}

	public JButton getBtnColor() {
		return btnColor;
	}

}
