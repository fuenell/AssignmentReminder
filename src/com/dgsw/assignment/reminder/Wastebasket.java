package com.dgsw.assignment.reminder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Wastebasket implements WindowListener {

	// ������ ������
	public static JFrame jfWastebasket;

	public static JLabel jlVersion;

	// �� ���� ��ư
	public static JButton jbColor[] = new JButton[5];
	// ȸ��, ��ȫ��, �����, �Ķ���, ������
	public static int colorList[][] = { { 202, 203, 198 }, { 254, 184, 184 }, { 253, 228, 159 }, { 174, 217, 207 },
			{ 57, 51, 63 } };

	// ���ΰ�ħ ��ư
	public static JButton jbWasteRefresh;
	// ������ �ݱ� ��ư
	public static JButton jbWasteDispose;

	// ������ ����Ʈ ����
	public static JTable jtWastebasket;
	public static String[][] listWastebasket;
	public static String[][] backUpWastebasket;
	public static JScrollPane scrollWastebasket;
	public static String header[] = { "����", "����", "�ؾ��� ��", "����", "����" };
	public static DefaultTableModel modelWastebasket;

	// ������ ������ ������Ʈ �߰�
	public Wastebasket() {

		// ������ ������ ����
		jfWastebasket = new JFrame("������");
		jfWastebasket.getContentPane().setLayout(null);
		jfWastebasket.setSize(332, 312);
		jfWastebasket.setUndecorated(true);
		jfWastebasket.getContentPane().setBackground(AssignmentReminder.frameColor);
		jfWastebasket.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 662, 0);
		jfWastebasket.setIconImage(new ImageIcon(this.getClass().getResource("/image/list.png")).getImage());
		jfWastebasket.addWindowListener(this);

		// ���� ��
		jlVersion = new JLabel("<html>" + "Assignment<br>Reminder " + AssignmentReminder.VERSION + "</html>");
		jlVersion.setFont(new Font("Tmon��Ҹ� Black", Font.PLAIN, 10));
		jlVersion.setHorizontalAlignment(SwingConstants.CENTER);
		jlVersion.setBounds(1, 2, 83, 34);
		jfWastebasket.getContentPane().add(jlVersion);

		// �� ���� ��ư
		for (int i = 0; i < 5; i++) {
			jbColor[i] = new JButton(new ImageIcon(this.getClass().getResource("/image/Color0" + (i + 1) + ".png")));
			jbColor[i].setBounds(i * 30 + 90, 5, 26, 25);
			jbColor[i].setBorderPainted(false);
			jbColor[i].setFocusPainted(false);
			jbColor[i].setContentAreaFilled(false);
			jbColor[i].addMouseListener(new MouseListener() {
				// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
				@Override
				public void mouseReleased(MouseEvent e) {
					// MouseEvent������ 27
					String[] jbList = e.getSource().toString().split(",");
					int num = (Integer.parseInt(jbList[1]) - 90) / 30;
					jbColor[num]
							.setIcon(new ImageIcon(this.getClass().getResource("/image/Color0" + (num + 1) + ".png")));
					// �� ���� �� ����
					AssignmentReminder.frameColor = new Color(colorList[num][0], colorList[num][1], colorList[num][2]);
					// ����â �� ����, ���ΰ�ħ
					AssignmentReminder.jfMain.getContentPane().setBackground(AssignmentReminder.frameColor);
					AssignmentReminder.jfMain.revalidate();
					AssignmentReminder.jfMain.repaint();
					// ������ �� ����, ���ΰ�ħ
					jfWastebasket.getContentPane().setBackground(AssignmentReminder.frameColor);
					jfWastebasket.revalidate();
					jfWastebasket.repaint();
					// �� ���� �����ϱ� (�ؽ�Ʈ �ƿ�ǲ)
					try {
						PrintWriter pw = new PrintWriter(new FileWriter("C:/AssignmentReminder/Setting.txt"));
						pw.println(colorList[num][0] + ", " + colorList[num][1] + ", " + colorList[num][2]);
						pw.close();
					} catch (Exception e2) {
					}
				}

				// ������ �� �̹��� ����
				@Override
				public void mousePressed(MouseEvent e) {
					String[] jbList = e.getSource().toString().split(",");
					int num = (Integer.parseInt(jbList[1]) - 90) / 30;
					jbColor[num]
							.setIcon(new ImageIcon(this.getClass().getResource("/image/Color0" + (num + 1) + "P.png")));
				}

				@Override
				public void mouseClicked(MouseEvent e) {
				}

				// ���콺 �÷��� �� �̹��� ����
				@Override
				public void mouseEntered(MouseEvent e) {
				}

				// ������ �� �̹��� ����
				@Override
				public void mouseExited(MouseEvent e) {
				}
			});
			jfWastebasket.getContentPane().add(jbColor[i]);
		}

		// ���ΰ�ħ ��ư
		jbWasteRefresh = new JButton(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
		jbWasteRefresh.setFont(new Font("����", Font.PLAIN, 10));
		jbWasteRefresh.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbWasteRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
				// ���� ����Ʈ ���ΰ�ħ
				jfWastebasket.remove(scrollWastebasket);
				wastebasketList();
			}

			// ������ �� �̹��� ����
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				jbWasteRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/RefreshP.png")));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		jbWasteRefresh.setBounds(242, 1, 44, 35);
		jbWasteRefresh.setBorderPainted(false);
		jbWasteRefresh.setFocusPainted(false);
		jbWasteRefresh.setContentAreaFilled(false);
		jfWastebasket.getContentPane().add(jbWasteRefresh);

		// �ݱ� ��ư
		jbWasteDispose = new JButton(new ImageIcon(this.getClass().getResource("/image/WasteDispose.png")));
		jbWasteDispose.setFont(new Font("����", Font.PLAIN, 10));
		jbWasteDispose.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbWasteRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/WasteDispose.png")));
				AssignmentReminder.isWastebasket = false;
				jfWastebasket.dispose();
			}

			// ������ �� �̹��� ����
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				jbWasteDispose.setIcon(new ImageIcon(this.getClass().getResource("/image/WasteDisposeP.png")));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}
		});
		jbWasteDispose.setBounds(287, 1, 44, 35);
		jbWasteDispose.setBorderPainted(false);
		jbWasteDispose.setFocusPainted(false);
		jbWasteDispose.setContentAreaFilled(false);
		jfWastebasket.getContentPane().add(jbWasteDispose);

		wastebasketList();

		jfWastebasket.setVisible(true);
	}

	// ������ ����Ʈ ��� �Լ�
	static public void wastebasketList() {
		int cntWastebasket = 0;
		try {
			// �ؽ�Ʈ�� listWastebasket[][]�� �ű�
			try {
				String tmpWastebasket[][] = new String[1000][5];
				BufferedReader br = new BufferedReader(new FileReader("C:/AssignmentReminder/WastebasketList.txt"));
				String line = br.readLine();
				// cntWastebasket �� �ڷᰳ��
				for (int i = 0; line != null; i++, cntWastebasket++) {
					tmpWastebasket[i] = line.split(AssignmentReminder.DIVISION);
					line = br.readLine();
				}
				// ���� cntWastebasket ũ�⸸ŭ ���ο� �迭 ����
				listWastebasket = new String[cntWastebasket][5];
				backUpWastebasket = new String[cntWastebasket][5];
				for (int j = 0; j < cntWastebasket; j++) {
					listWastebasket[j] = tmpWastebasket[j].clone();
				}
				br.close();
			} catch (Exception e) {
			}

			// listWastebasket[][0] ����
			for (int i = 0; i < listWastebasket.length; i++) {
				for (int j = 1; j < listWastebasket.length - i; j++) {
					try {
						if (Integer.parseInt(listWastebasket[j - 1][0]) > Integer.parseInt(listWastebasket[j][0])) {
							String tmp[] = listWastebasket[j - 1].clone();
							listWastebasket[j - 1] = listWastebasket[j].clone();
							listWastebasket[j] = tmp.clone();
						}
					} // ���ڷ� ��ȯ���� ���� �� �ڷ� ����
					catch (Exception e) {
						// �� �� ���ڰų� �տ� �͸� ���ڸ� �� �ٲ�
						try {
							Integer.parseInt(listWastebasket[j][0]);
						} catch (Exception e2) {
							continue;
						}
						if (listWastebasket[j - 1][0].equals("")) {
							String tmp[] = listWastebasket[j - 1].clone();
							listWastebasket[j - 1] = listWastebasket[j].clone();
							listWastebasket[j] = tmp.clone();
						}
					}
				}
			}

			// ��¥�� D-day ǥ������� ����
			for (int i = 0; i < listWastebasket.length; i++) {
				try {
					backUpWastebasket[i] = listWastebasket[i].clone();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
					String date = formatter.format(new Date());
					Date beginDate;
					beginDate = formatter.parse(date);
					Date endDate = formatter.parse(listWastebasket[i][0]);
					long diff = endDate.getTime() - beginDate.getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					if (diffDays == 0) {
						listWastebasket[i][0] = "D-day";
					} else if (diffDays > 0) {
						listWastebasket[i][0] = "D-" + diffDays;
					} else {
						listWastebasket[i][0] = "D+" + diffDays * -1;
					}
				} // ��¥�� ���ڰ� �ƴϸ� '-' �� ǥ��
				catch (Exception e) {
					listWastebasket[i][0] = "-";
				}
			}

		} catch (Exception e) {
		}

		// ����� ����Ʈ�� �𵨿� ����
		modelWastebasket = new DefaultTableModel(listWastebasket, header) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int a, int b) {
				return false;
			}
		};

		// ���̺� ����
		jtWastebasket = new JTable(modelWastebasket);
		jtWastebasket.getTableHeader().setReorderingAllowed(false);
		jtWastebasket.getTableHeader().setResizingAllowed(false);

		// ���� ��¥
		jtWastebasket.getColumnModel().getColumn(0).setMaxWidth(49);
		jtWastebasket.getColumnModel().getColumn(0).setMinWidth(49);
		jtWastebasket.getColumnModel().getColumn(0).setWidth(49);
		// ����
		jtWastebasket.getColumnModel().getColumn(1).setMaxWidth(55);
		jtWastebasket.getColumnModel().getColumn(1).setMinWidth(55);
		jtWastebasket.getColumnModel().getColumn(1).setWidth(55);
		// ����
		jtWastebasket.getColumnModel().getColumn(3).setMaxWidth(30);
		jtWastebasket.getColumnModel().getColumn(3).setMinWidth(30);
		jtWastebasket.getColumnModel().getColumn(3).setWidth(30);
		// ����
		jtWastebasket.getColumnModel().getColumn(4).setMaxWidth(30);
		jtWastebasket.getColumnModel().getColumn(4).setMinWidth(30);
		jtWastebasket.getColumnModel().getColumn(4).setWidth(30);

		jtWastebasket.getTableHeader().setPreferredSize(new Dimension(0, 23));
		jtWastebasket.setRowHeight(23);

		jtWastebasket.getTableHeader().setFont(new Font("���õ������ Bold", Font.PLAIN, 15));
		jtWastebasket.setFont(new Font("���õ������ Bold", Font.PLAIN, 15));

		// ����,���� ��ư
		jtWastebasket.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent mouseevent) {
				// ���� �߿��� ����, ���� �Ұ���
				if (AssignmentReminder.isChanging) {
					return;
				}
				// 3�� ° ������ ����
				if (jtWastebasket.getSelectedColumn() == 3) {
					try {
						PrintWriter pw = new PrintWriter("C:/AssignmentReminder/WastebasketList.txt");
						for (int i = 0; i < listWastebasket.length; i++) {
							// ���� �� �迭�̸� AssignmentList.txt�� �߰��ϰ� continue
							if (i == jtWastebasket.getSelectedRow()) {
								PrintWriter wbpw = new PrintWriter(
										new FileWriter("C:/AssignmentReminder/AssignmentList.txt", true));
								wbpw.println(
										backUpWastebasket[i][0] + AssignmentReminder.DIVISION + backUpWastebasket[i][1]
												+ AssignmentReminder.DIVISION + backUpWastebasket[i][2]
												+ AssignmentReminder.DIVISION + AssignmentReminder.PUSH
												+ AssignmentReminder.DIVISION + AssignmentReminder.Del);
								wbpw.close();
								continue;
							}
							String data = backUpWastebasket[i][0] + AssignmentReminder.DIVISION
									+ backUpWastebasket[i][1] + AssignmentReminder.DIVISION + backUpWastebasket[i][2]
									+ AssignmentReminder.DIVISION + AssignmentReminder.PUSH
									+ AssignmentReminder.DIVISION + AssignmentReminder.Del;
							pw.println(data);
						}
						pw.close();
						jfWastebasket.remove(scrollWastebasket);
						wastebasketList();
					} catch (Exception e) {
					}
				} // 4�� ° ������ ����
				else if (jtWastebasket.getSelectedColumn() == 4) {
					try {
						PrintWriter pw = new PrintWriter("C:/AssignmentReminder/WastebasketList.txt");
						for (int i = 0; i < listWastebasket.length; i++) {
							if (i == jtWastebasket.getSelectedRow()) {
								continue;
							}
							String data = backUpWastebasket[i][0] + AssignmentReminder.DIVISION
									+ backUpWastebasket[i][1] + AssignmentReminder.DIVISION + backUpWastebasket[i][2]
									+ AssignmentReminder.DIVISION + AssignmentReminder.PUSH
									+ AssignmentReminder.DIVISION + AssignmentReminder.Del;
							pw.println(data);

						}
						pw.close();
						jfWastebasket.remove(scrollWastebasket);
						wastebasketList();
					} catch (Exception e) {
					}
				}
				AssignmentReminder.jfMain.remove(AssignmentReminder.scroll);
				AssignmentReminder.assignmentList();
			}

			@Override
			public void mousePressed(MouseEvent mouseevent) {
			}

			@Override
			public void mouseExited(MouseEvent mouseevent) {
			}

			@Override
			public void mouseEntered(MouseEvent mouseevent) {
			}

			@Override
			public void mouseClicked(MouseEvent mouseevent) {
			}
		});
		jtWastebasket.repaint();

		// ��� ����
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = jtWastebasket.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}

		// ��ũ�� ����
		scrollWastebasket = new JScrollPane(jtWastebasket);
		if (cntWastebasket >= 11) {
			scrollWastebasket.setBounds(1, 36, 329, 276);
		} else {
			scrollWastebasket.setBounds(1, 36, 329, cntWastebasket * 23 + 23);
		}
		scrollWastebasket.setOpaque(true);
		scrollWastebasket.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		jfWastebasket.getContentPane().add(scrollWastebasket);

		jfWastebasket.revalidate();
		jfWastebasket.repaint();
	}

	public static void main(String[] args) {
		new Wastebasket();
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	// â ������ is ���� false��
	@Override
	public void windowClosing(WindowEvent e) {
		AssignmentReminder.isWastebasket = false;
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
}
