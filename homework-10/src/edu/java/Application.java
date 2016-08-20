package edu.java;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import edu.java.EditDialog.IEditListener;

import java.awt.Font;

public class Application implements IEditListener {

	private JFrame frame;
	private JTextField txf_ip;
	private JLabel lblUser;
	private JTextField txf_user;
	private JLabel lblPassword;
	private JPasswordField txf_password;
	
	private Connection connection = null;
	private Statement statement = null;
	private JLabel lbl_status;
	private JTable tbl_content;
	private JButton btn_connect;
	private JTextField txf_database;
	private JList lst_tables;
	private String selectedTable;
	private JButton btn_edit;
	private JButton btn_add;
	private JButton btn_del;
	private int selectedRow;
	private List<Map<String, String>> tableData = null;
	private JTextField txf_key;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 680, 349);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblIp = new JLabel("IP: ");
		lblIp.setBounds(10, 11, 28, 14);
		frame.getContentPane().add(lblIp);
		
		txf_ip = new JTextField();
		txf_ip.setText("10.3.0.5");
		txf_ip.setBounds(31, 6, 109, 25);
		frame.getContentPane().add(txf_ip);
		txf_ip.setColumns(10);
		
		lblUser = new JLabel("User: ");
		lblUser.setBounds(181, 11, 29, 14);
		frame.getContentPane().add(lblUser);
		
		txf_user = new JTextField();
		txf_user.setText("java-103");
		txf_user.setColumns(10);
		txf_user.setBounds(214, 6, 109, 25);
		frame.getContentPane().add(txf_user);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(156, 41, 59, 14);
		frame.getContentPane().add(lblPassword);
		
		txf_password = new JPasswordField();
		txf_password.setText("java-103");
		txf_password.setColumns(10);
		txf_password.setBounds(214, 36, 109, 25);
		frame.getContentPane().add(txf_password);
		
		btn_connect = new JButton("Connect");
		btn_connect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
					btn_connect.setEnabled(false);
					new Thread(new Runnable() {
						
						public void run() {
							try {
								lbl_status.setText("Connecting...");
								String ip = txf_ip.getText(),
										username = txf_user.getText(),
										password = new String(txf_password.getPassword());
								if (ip == null || ip.isEmpty()) {
									txf_ip.setText(ip = "192.168.0.100");
									txf_user.setText(username = "liosha");
									password = "612287";
								}
								connection = DriverManager.getConnection(
										"jdbc:sqlserver://" + ip + ";databaseName=" + txf_database.getText() + ";user=" + username + ";password=" + password + ";"
								);
								showTables();
								lbl_status.setText("Connected to database!");
							} catch (Exception e) {
								try {
									if (connection != null && !connection.isClosed()) {
										connection.close();
									}
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								lbl_status.setText("Can't open connection!");
								btn_connect.setEnabled(true);
								e.printStackTrace();
								return;
							}
						}


					}).run();
				} catch (SQLException e) {
					lbl_status.setText("Can't load driver!");
					btn_connect.setEnabled(true);
					e.printStackTrace();
					return;
				}
			}
		});
		btn_connect.setBounds(333, 6, 144, 24);
		frame.getContentPane().add(btn_connect);
		
		lbl_status = new JLabel("Status OK");
		lbl_status.setBounds(14, 293, 309, 14);
		frame.getContentPane().add(lbl_status);
		
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(10, 66, 516, 212);
		frame.getContentPane().add(scrollPane);
		
		tbl_content = new JTable();
		tbl_content.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tbl_content.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(tbl_content);
		tbl_content.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
	        public void valueChanged(ListSelectionEvent event) {
	            Application.this.onRowSelected(event.getFirstIndex());
	        }
	    });
		
		lst_tables = new JList();
		lst_tables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		lst_tables.setBounds(536, 6, 128, 272);
		frame.getContentPane().add(lst_tables);
		
		JLabel lblDb = new JLabel("DB: ");
		lblDb.setBounds(10, 41, 28, 14);
		frame.getContentPane().add(lblDb);
		
		txf_database = new JTextField();
		txf_database.setText("firstdb");
		txf_database.setColumns(10);
		txf_database.setBounds(31, 36, 109, 25);
		frame.getContentPane().add(txf_database);
		
		this.btn_add = new JButton("Add");
		btn_add.setFont(new Font("Tahoma", Font.BOLD, 11));
		btn_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.onAddClicked();
			}
		});
		btn_add.setEnabled(false);
		btn_add.setBounds(326, 289, 68, 23);
		frame.getContentPane().add(btn_add);
		
		this.btn_del = new JButton("Del");
		btn_del.setFont(new Font("Tahoma", Font.BOLD, 11));
		btn_del.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (selectedRow >= 0) {
					Application.this.onDelClicked(selectedRow);
				}
			}
		});
		btn_del.setEnabled(false);
		btn_del.setBounds(393, 289, 68, 23);
		frame.getContentPane().add(btn_del);
		
		this.btn_edit = new JButton("Edit");
		btn_edit.setFont(new Font("Tahoma", Font.BOLD, 11));
		btn_edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.this.onEditClicked();
			}
		});
		btn_edit.setEnabled(false);
		btn_edit.setBounds(458, 289, 68, 23);
		frame.getContentPane().add(btn_edit);
		
		JLabel lblKeyColumn = new JLabel("Key column:");
		lblKeyColumn.setBounds(536, 293, 68, 14);
		frame.getContentPane().add(lblKeyColumn);
		
		txf_key = new JTextField();
		txf_key.setText("id");
		txf_key.setBounds(615, 290, 49, 20);
		frame.getContentPane().add(txf_key);
		txf_key.setColumns(10);
	}
	
	protected void onRowSelected(int selectedRow) {
		if (selectedRow >= 0) {
			this.selectedRow = selectedRow;
			btn_del.setEnabled(true);
			btn_edit.setEnabled(true);
		} else {
			this.selectedRow = -1;
			btn_del.setEnabled(false);
			btn_edit.setEnabled(false);
		}
		
	}

	protected void onEditClicked() {
		System.out.println("onEditClicked " + selectedRow);
		if (selectedRow != -1) {
			EditDialog dialog = new EditDialog(this.frame, "Edit row of table " + selectedTable, tableData.get(selectedRow), this, true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		}
	}

	protected void onDelClicked(int selectedRow) {
		System.out.println("onDelClicked " + selectedRow);
		if (tableData != null && !tableData.isEmpty()) {
			Map<String, String> row = tableData.get(selectedRow);
			try {
				String keyColumn = getKeyColumn();
				String rowId = row.get(keyColumn);
				statement = connection.createStatement();
				try {
					statement.execute("DELETE FROM " + selectedTable + " where " + keyColumn + "=" + rowId);
					lbl_status.setText("Row was deleted!");
					onDataChanged();
				} catch (Exception e) {
					connection.close();
					throw e;
				} finally {
					statement.close();
				}
			} catch (Exception e) {
				lbl_status.setText("Can't delete row!");
				btn_connect.setEnabled(true);
				e.printStackTrace();
			}
		}
	}

	private void onDataChanged() {
		onTableSelected(selectedTable);
	}

	protected void onAddClicked() {
		System.out.println("onAddClicked");
		if (selectedTable != null && !selectedTable.isEmpty()) {
			try {
				String keyColumn = getKeyColumn();
				statement = connection.createStatement();
				Map<String, String> data = new HashMap<String, String>();
				try {
					final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + selectedTable);
					ResultSetMetaData metaData = resultSet.getMetaData();
					int columnCount = metaData.getColumnCount();
					for (int n = 0; n < columnCount; n++) {
						String fieldName = metaData.getColumnName(n + 1);
						if (keyColumn.equals(fieldName)) {
							continue;
						}
						data.put(fieldName, null);
					}

					EditDialog dialog = new EditDialog(this.frame, "Add row to table " + selectedTable, data, this, false);
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);

				} finally {
					statement.close();
				}
			} catch (Exception e) {
				lbl_status.setText("Can't get table info!");
				btn_connect.setEnabled(true);
				e.printStackTrace();

			} 
		}

		onDataChanged();
	}

	private void onTableSelected(String source) {
		System.out.println("Table selected: " + source);
		this.selectedRow = -1;
		try {
			statement = connection.createStatement();
			try {
				final ResultSet resultSet = statement.executeQuery("SELECT * FROM " + source);
				try {
				List<Map<String, String>> data = new ArrayList<Map<String,String>>();
				while (resultSet.next()) {
					ResultSetMetaData metaData = resultSet.getMetaData();
					int columnCount = metaData.getColumnCount();
					HashMap<String,String> hashMap = new HashMap<String, String>();
					for (int n = 1; n <= columnCount; n++) {
						hashMap.put(metaData.getColumnName(n), null);
					}
					Set<String> keySet = hashMap.keySet();
					for (String key : keySet) {
						hashMap.put(key, resultSet.getString(key));
					}
					data.add(hashMap);
				}
				showData(data);
				btn_add.setEnabled(true);
				this.selectedTable = source;
				lbl_status.setText("Data loaded!");
				} catch (Exception e) {
					connection.close();
					throw e;
				}
			} finally {
				statement.close();
			}
		} catch (Exception e) {
			lbl_status.setText("Can't load tables!");
			btn_connect.setEnabled(true);
			e.printStackTrace();

		}
	}

	private void showData(final List<Map<String, String>> data) {
		
		if (!data.isEmpty()) {
			this.tableData = data;
			Map<String, String> map = data.get(0);
			String[] headers = new String[map.size()];
			map.keySet().toArray(headers);
			DefaultTableModel model = new DefaultTableModel(headers, data.size()) {

				public Object getValueAt(int row, int col) {
					Map<String, String> map = data.get(row);
					String[] keys = new String[map.size()];
					map.keySet().toArray(keys);
					final String key = keys[col], value = map.get(key);
					return new TableEntry(key, value);
				}

				public int getRowCount() {
					return data.size();
				}

				public int getColumnCount() {
					return data.get(0).size();
				}

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			tbl_content.setModel(model);
		}
	}

	private void showTables() {
		try {
			statement = connection.createStatement();
			try {
				final ResultSet resultSet = statement.executeQuery("SELECT TABLE_NAME FROM " + txf_database.getText()
						+ ".INFORMATION_SCHEMA.Tables WHERE TABLE_TYPE = 'BASE TABLE'");
				DefaultListModel<String> model = new DefaultListModel<String>();
				try {
					while (resultSet.next()) {
						String tableName = resultSet.getString("TABLE_NAME");
						model.addElement(tableName);
					}
				} finally {
					resultSet.close();
				}
				lst_tables.setModel(model);
				lst_tables.addListSelectionListener(new ListSelectionListener() {
					
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()){
							onTableSelected((String) lst_tables.getSelectedValue()); 
						}
					}
				});
				lbl_status.setText("Tables loaded!");
			} finally {
				statement.close();
			}
		} catch (Exception e) {
			lbl_status.setText("Can't load tables!");
			btn_connect.setEnabled(true);
			e.printStackTrace();

		}
	}

	public void actionPerformed(Map<String, String> newValues, boolean editMode) {
		try {
			statement = connection.createStatement();
			try {
				StringBuilder builder = new StringBuilder();
				String keyColumn = getKeyColumn();
				if (editMode) {
					builder.append("UPDATE ").append(selectedTable).append(" SET ");
					String keyValue = newValues.remove(keyColumn);
					for (Map.Entry<String, String> entry : newValues.entrySet()) {
						builder.append(entry.getKey()).append("=");
						String value = entry.getValue();
						builder.append(sqlValue(value)).append(", ");
					}
					trimLastComma(builder).append(" WHERE ").append(keyColumn).append("=").append(keyValue);
				} else {
					builder.append("INSERT INTO ").append(selectedTable).append(" (");
					StringBuilder values = new StringBuilder();
					for (Map.Entry<String, String> entry : newValues.entrySet()) {
						builder.append(entry.getKey()).append(", ");
						values.append(sqlValue(entry.getValue())).append(", ");
					}
					trimLastComma(builder).append(") VALUES (").append(trimLastComma(values)).append(")");
				}
				System.out.println(builder.toString());
				statement.execute(builder.toString());
				onDataChanged();
			} finally {
				statement.close();
			}
		} catch (Exception e) {
			lbl_status.setText("Can't add row!");
			btn_connect.setEnabled(true);
			e.printStackTrace();

		}
	}

	private String getKeyColumn() throws Exception {
		String keyColumn = txf_key.getText();
		if (keyColumn == null || keyColumn.isEmpty()) {
			lbl_status.setText("Key column must be filled!");
			throw new Exception();
		}
		return keyColumn;
	}

	private StringBuilder trimLastComma(StringBuilder builder) {
		int lastIndexOf = builder.lastIndexOf(",");
		return lastIndexOf > -1 ? builder.delete(lastIndexOf, lastIndexOf + 1) : builder;
	}

	private String sqlValue(String value) {
		if (value.matches("-?\\d+(\\.\\d+)?")) {
			return value;
		} else {
			return "'" + value + "'";
		}
	}
}
