package com.dgsw.assignment.reminder;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class AssignmentReminder implements WindowListener {

	// ���� ����
	public static final String VERSION = "V1.2";

	// ���� ���
	public static final String DIVISION = "��٤ؤݤܤۤ�";

	// ���� ��ư ���
	public static final String PUSH = "��";
	public static final String Del = "��";

	// ���� â
	public static JFrame jfMain;
	public static Color frameColor = new Color(174, 217, 207);
	// �ؽ�Ʈ
	public static JTextField jtDate;
	public static JTextField jtSubject;
	public static JTextField jtContent;

	// ��ư
	public static JButton jbClassroom;
	public static JButton jbBand;
	public static JButton jbDispose;
	public static JButton jbLastWeek;
	public static JButton jbNextWeek;
	public static JButton jbWastebasket;
	public static JButton jbRefresh;
	public static JButton jbAdd;

	// ���� ���� ��ư
	public static JButton jbWeek[] = new JButton[7];

	// ���� ���ڿ�
	public static String strWeek[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	// ���� ����Ʈ
	public static JTable jtAssignment;
	public static String[][] listAssignment;
	public static String[][] backUpAssignment;
	public static JScrollPane scroll;
	public static String header[] = { "����", "����", "�ؾ��� ��", "����", "�Ϸ�" };
	public static DefaultTableModel modelAssignment;

	// ��¥
	public static Calendar cal = Calendar.getInstance();
	public static Calendar calToday = Calendar.getInstance();

	// ���α׷��� ����������
	public static DatagramSocket isRun;

	// ����â ���� �ִ���
	public static boolean isMain = false;

	// ������ ���� �ִ���
	public static boolean isWastebasket = false;

	// ���� �� ����
	public static boolean isChanging = false;
	public static int tmpChanging;

	// ������� �޼ҵ�

	// (������)���� ������ ������Ʈ �߰�
	public AssignmentReminder() {

		// ������ ���ϰ�� ����
		String path = "C:/AssignmentReminder";
		// ���� ��ü ����
		File file = new File(path);
		// !ǥ�� �ٿ��־� ������ �������� �ʴ� ����� ������ �ɾ���
		if (!file.exists()) {
			// ���丮 ���� �޼���
			file.mkdirs();
		}

		// â ����
		isMain = true;
		// �� ���� �ҷ�����
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/AssignmentReminder/Setting.txt"));
			String strColor[] = br.readLine().split(", ");
			frameColor = new Color(Integer.parseInt(strColor[0]), Integer.parseInt(strColor[1]),
					Integer.parseInt(strColor[2]));
			br.close();
		} catch (Exception e) {
			try {
				// BufferedWriter bw = new BufferedWriter(new
				// FileWriter("C:/AssignmentReminder/Setting.txt"));
				// bw.write("174, 217, 207");
				// bw.newLine();
				// bw.close();

				PrintWriter pw = new PrintWriter(new FileWriter("C:/AssignmentReminder/Setting.txt"));
				pw.println("174, 217, 207");
				pw.close();
			} catch (Exception e2) {
			}
		}
		// ���� ������ ����
		jfMain = new JFrame("���� �����δ�");
		jfMain.getContentPane().setLayout(null);
		jfMain.setUndecorated(true);
		jfMain.getContentPane().setBackground(frameColor);
		jfMain.setSize(331, 373);
		jfMain.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 331, 0);
		jfMain.setIconImage(new ImageIcon(this.getClass().getResource("/image/list.png")).getImage());
		jfMain.addWindowListener(this);

		// ��� ���� �ؿ��� ��ư ���

		// Ŭ������ �ٷΰ��� ��ư
		jbClassroom = new JButton(new ImageIcon(this.getClass().getResource("/image/Classroom.png")));
		jbClassroom.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				jbClassroom.setIcon(new ImageIcon(this.getClass().getResource("/image/Classroom.png")));
				try {
					Desktop.getDesktop().browse(new URI("https://classroom.google.com/h"));
				} catch (Exception ex) {
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				jbClassroom.setIcon(new ImageIcon(this.getClass().getResource("/image/ClassroomP.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		jbClassroom.setBounds(1, 1, 74, 35);
		jbClassroom.setBorderPainted(false);
		jbClassroom.setFocusPainted(false);
		jbClassroom.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbClassroom);

		// ��� �ٷΰ��� ��ư
		jbBand = new JButton(new ImageIcon(this.getClass().getResource("/image/Band.png")));
		jbBand.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				jbBand.setIcon(new ImageIcon(this.getClass().getResource("/image/Band.png")));
				try {
					Desktop.getDesktop().browse(new URI("https://band.us/"));
				} catch (Exception ex) {
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				jbBand.setIcon(new ImageIcon(this.getClass().getResource("/image/BandP.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		jbBand.setBounds(76, 1, 74, 35);
		jbBand.setBorderPainted(false);
		jbBand.setFocusPainted(false);
		jbBand.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbBand);

		// ������ ��ư
		jbWastebasket = new JButton(new ImageIcon(this.getClass().getResource("/image/Wastebasket.png")));
		jbWastebasket.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbWastebasket.setIcon(new ImageIcon(this.getClass().getResource("/image/Wastebasket.png")));
				// ���� ����Ʈ ���ΰ�ħ
				if (!isWastebasket) {
					isWastebasket = true;
					new Wastebasket();
				}
			}

			// ������ �� �̹��� ����
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				jbWastebasket.setIcon(new ImageIcon(this.getClass().getResource("/image/WastebasketP.png")));
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

		jbWastebasket.setBounds(151, 1, 88, 35);
		jbWastebasket.setBorderPainted(false);
		jbWastebasket.setFocusPainted(false);
		jbWastebasket.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbWastebasket);

		// ���ΰ�ħ ��ư
		jbRefresh = new JButton(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
		jbRefresh.setFont(new Font("����", Font.PLAIN, 10));
		jbRefresh.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
				// ���� ����Ʈ ���ΰ�ħ
				jfMain.remove(scroll);
				assignmentList();
			}

			// ������ �� �̹��� ����
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				jbRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/RefreshP.png")));
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
		jbRefresh.setBounds(240, 1, 44, 35);
		jbRefresh.setBorderPainted(false);
		jbRefresh.setFocusPainted(false);
		jbRefresh.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbRefresh);

		// �ݱ� ��ư
		jbDispose = new JButton(new ImageIcon(this.getClass().getResource("/image/Dispose.png")));
		jbDispose.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				jbDispose.setIcon(new ImageIcon(this.getClass().getResource("/image/Dispose.png")));
				if (isWastebasket) {
					isWastebasket = false;
					Wastebasket.jfWastebasket.dispose();
				}
				isMain = false;
				jfMain.dispose();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				jbDispose.setIcon(new ImageIcon(this.getClass().getResource("/image/DisposeP.png")));
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		jbDispose.setBounds(286, 1, 44, 35);
		jbDispose.setBorderPainted(false);
		jbDispose.setFocusPainted(false);
		jbDispose.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbDispose);

		// ���� �߰� ��ư
		jbAdd = new JButton(new ImageIcon(this.getClass().getResource("/image/Add.png")));
		jbAdd.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + ���� �߰� �޼ҵ� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				addAssignment();
			}

			// ������ �� �̹��� ���� ���� ���̸� ���� �̹��� ���
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				if (isChanging) {
					jbAdd.setIcon(new ImageIcon(this.getClass().getResource("/image/ChangeP.png")));
				} else {
					jbAdd.setIcon(new ImageIcon(this.getClass().getResource("/image/AddP.png")));
				}
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
		jbAdd.setBounds(249, 337, 80, 36);
		jbAdd.setBorderPainted(false);
		jbAdd.setFocusPainted(false);
		jbAdd.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbAdd);

		// ������ ��ư
		for (int i = 0; i < jbWeek.length; i++) {
			// ���� ��ư�� �� ����
			if (calToday.get(Calendar.DAY_OF_WEEK) - 1 == i) {
				jbWeek[i] = new JButton(new ImageIcon(this.getClass().getResource("/image/" + strWeek[i] + "T.png")));
			} else {
				jbWeek[i] = new JButton(new ImageIcon(this.getClass().getResource("/image/" + strWeek[i] + ".png")));
			}
			jbWeek[i].addMouseListener(new MouseListener() {
				// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
				@Override
				public void mouseReleased(MouseEvent e) {

					String[] jbList = e.getSource().toString().split(",");
					int week = (Integer.parseInt(jbList[1]) - 1) / 27;
					// ���� ��ư�� �� ����
					if (calToday.get(Calendar.DAY_OF_WEEK) - 1 == week) {
						jbWeek[week].setIcon(
								new ImageIcon(this.getClass().getResource("/image/" + strWeek[week] + "T.png")));
					} else {
						jbWeek[week].setIcon(
								new ImageIcon(this.getClass().getResource("/image/" + strWeek[week] + ".png")));
					}

					cal = Calendar.getInstance();
					seekDay(week + 1);
				}

				// ������ �� �̹��� ����
				@Override
				public void mousePressed(MouseEvent e) {
					String[] jbList = e.getSource().toString().split(",");
					int week = (Integer.parseInt(jbList[1]) - 1) / 27;
					// ���� ��ư�� �� ����
					if (calToday.get(Calendar.DAY_OF_WEEK) - 1 == week) {
						jbWeek[week].setIcon(
								new ImageIcon(this.getClass().getResource("/image/" + strWeek[week] + "TP.png")));
					} else {
						jbWeek[week].setIcon(
								new ImageIcon(this.getClass().getResource("/image/" + strWeek[week] + "P.png")));
					}

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
			jbWeek[i].setBounds(i * 27 + 1, 311, 26, 25);
			jbWeek[i].setBorderPainted(false);
			jbWeek[i].setFocusPainted(false);
			jbWeek[i].setContentAreaFilled(false);
			jfMain.getContentPane().add(jbWeek[i]);
		}

		// ������ ��ư
		jbLastWeek = new JButton(new ImageIcon(this.getClass().getResource("/image/Lastweek.png")));
		jbLastWeek.setFont(new Font("����", Font.PLAIN, 5));
		jbLastWeek.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbLastWeek.setIcon(new ImageIcon(this.getClass().getResource("/image/Lastweek.png")));
				seekWeek(-1);
			}

			// ������ �� �̹��� ����
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				jbLastWeek.setIcon(new ImageIcon(this.getClass().getResource("/image/LastweekP.png")));
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
		jbLastWeek.setBounds(190, 311, 26, 25);
		jbLastWeek.setBorderPainted(false);
		jbLastWeek.setFocusPainted(false);
		jbLastWeek.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbLastWeek);

		// ������ ��ư
		jbNextWeek = new JButton(new ImageIcon(this.getClass().getResource("/image/Nextweek.png")));
		jbNextWeek.setFont(new Font("����", Font.PLAIN, 5));
		jbNextWeek.addMouseListener(new MouseListener() {
			// ���� �� �̹��� ���� + date ���Ͽ� ���� ����
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbNextWeek.setIcon(new ImageIcon(this.getClass().getResource("/image/Nextweek.png")));
				seekWeek(1);
			}

			// ������ �� �̹��� ����
			@Override
			public void mousePressed(MouseEvent mouseevent) {
				jbNextWeek.setIcon(new ImageIcon(this.getClass().getResource("/image/NextweekP.png")));
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
		jbNextWeek.setBounds(217, 311, 26, 25);
		jbNextWeek.setBorderPainted(false);
		jbNextWeek.setFocusPainted(false);
		jbNextWeek.setContentAreaFilled(false);
		jfMain.getContentPane().add(jbNextWeek);

		// ��� ���� �ؿ��� �ؽ�Ʈ ���

		// ��¥
		jtDate = new JTextField();
		jtDate.setHorizontalAlignment(SwingConstants.CENTER);
		jtDate.setFont(new Font("���õ������ Bold", Font.PLAIN, 14));
		jtDate.setBounds(1, 337, 78, 35);
		jtDate.setColumns(10);
		jtDate.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		jtDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAssignment();
			}
		});
		jfMain.getContentPane().add(jtDate);

		// ����
		jtSubject = new JTextField();
		jtSubject.setFont(new Font("���õ������ Bold", Font.PLAIN, 14));
		jtSubject.setHorizontalAlignment(SwingConstants.CENTER);
		jtSubject.setBounds(80, 337, 49, 35);
		jtSubject.setColumns(10);
		jtSubject.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		jtSubject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAssignment();
			}
		});
		jfMain.getContentPane().add(jtSubject);

		// ����
		jtContent = new JTextField();
		jtContent.setFont(new Font("���õ������ Bold", Font.PLAIN, 14));
		jtContent.setHorizontalAlignment(SwingConstants.CENTER);
		jtContent.setBounds(130, 337, 117, 35);
		jtContent.setColumns(10);
		jtContent.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		jtContent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAssignment();
			}
		});
		jfMain.getContentPane().add(jtContent);

		// ���� ����Ʈ �߰�
		assignmentList();

		jfMain.setVisible(true);

	}

	// ���� �߰� �Լ�
	public static void addAssignment() {
		jbAdd.setIcon(new ImageIcon(AssignmentReminder.class.getResource("/image/Add.png")));
		if (isChanging) {
			try {
				PrintWriter pw = new PrintWriter("C:/AssignmentReminder/AssignmentList.txt");
				for (int i = 0; i < listAssignment.length; i++) {
					if (i == tmpChanging) {
						continue;
					}
					String data = backUpAssignment[i][0] + DIVISION + backUpAssignment[i][1] + DIVISION
							+ backUpAssignment[i][2] + DIVISION + PUSH + DIVISION + Del;
					pw.println(data);
				}
				pw.close();
				// ���� ����Ʈ ���ΰ�ħ
				jfMain.remove(scroll);
				assignmentList();
			} catch (Exception e) {
			}
			isChanging = false;
		}

		try {

			// ���� �Ǵ� ���� �Ѵ� ��������� ���X
			if (jtSubject.getText().equals("") && jtContent.getText().equals("")) {
				return;
			}

			// ��¥ 0,8���� �ƴϸ� ���X
			if (!(jtDate.getText().length() == 8) && !(jtDate.getText().length() == 0)) {
				return;
			}

			// jtDate�� 8�����϶� ���ڰ� �ƴϸ� catch�� ��������
			if (jtDate.getText().length() == 8) {
				Integer.parseInt(jtDate.getText());
			}

			// �ؽ�Ʈ�� ����
			PrintWriter pw = new PrintWriter(new FileWriter("C:/AssignmentReminder/AssignmentList.txt", true));
			pw.println(jtDate.getText() + DIVISION + jtSubject.getText() + DIVISION + jtContent.getText() + DIVISION
					+ PUSH + DIVISION + Del);
			pw.close();

			// �ؽ�Ʈ ����
			jtDate.setText("");
			jtSubject.setText("");
			jtContent.setText("");

		} catch (Exception e) {
		}
		// ���� ����Ʈ ���ΰ�ħ
		jfMain.remove(scroll);
		assignmentList();
	}

	// ������ �̵� ��ư �Լ�
	public static void seekWeek(int week) {

		// ���� ��ŭ ���Ѵ�.
		cal.add(Calendar.DAY_OF_YEAR, week * 7);

		// ���� �⵵, ��, ��
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);

		jtDate.setText(year + "" + String.format("%02d", month) + "" + String.format("%02d", date));

		jtDate.requestFocusInWindow();

	}

	// ���� ��ư �Լ�
	public static void seekDay(int day) {

		// ���� ��ŭ ���Ѵ�.
		cal.add(Calendar.DAY_OF_YEAR, -(cal.get(Calendar.DAY_OF_WEEK) - day));

		// ���� �⵵, ��, ��
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);

		jtDate.setText(year + "" + String.format("%02d", month) + "" + String.format("%02d", date));

		jtDate.requestFocusInWindow();

	}

	// ���� ����Ʈ ��� �Լ�
	public static void assignmentList() {
		int cntAssignment = 0;
		try {
			// �ؽ�Ʈ�� listAssignment[][]�� �ű�
			try {
				String tmpAssignment[][] = new String[1000][5];
				BufferedReader br = new BufferedReader(new FileReader("C:/AssignmentReminder/AssignmentList.txt"));
				String line = br.readLine();
				for (int i = 0; line != null; i++, cntAssignment++) {
					tmpAssignment[i] = line.split(DIVISION);
					line = br.readLine();
				}
				listAssignment = new String[cntAssignment][5];
				backUpAssignment = new String[cntAssignment][5];
				for (int j = 0; j < cntAssignment; j++) {
					listAssignment[j] = tmpAssignment[j].clone();
				}
				br.close();
			} catch (Exception e) {
			}

			// listAssignment[][0] ����
			for (int i = 0; i < listAssignment.length; i++) {
				for (int j = 1; j < listAssignment.length - i; j++) {
					try {
						if (Integer.parseInt(listAssignment[j - 1][0]) > Integer.parseInt(listAssignment[j][0])) {
							String tmp[] = listAssignment[j - 1].clone();
							listAssignment[j - 1] = listAssignment[j].clone();
							listAssignment[j] = tmp.clone();
						}
					} // ���ڷ� ��ȯ���� ���� �� �ڷ� ����
					catch (Exception e) {
						// �� �� ���ڰų� �տ� �͸� ���ڸ� �� �ٲ�
						try {
							Integer.parseInt(listAssignment[j][0]);
						} catch (Exception e2) {
							continue;
						}
						if (listAssignment[j - 1][0].equals("")) {
							String tmp[] = listAssignment[j - 1].clone();
							listAssignment[j - 1] = listAssignment[j].clone();
							listAssignment[j] = tmp.clone();
						}
					}
				}
			}

			// ��¥�� D-day ǥ������� ����
			for (int i = 0; i < listAssignment.length; i++) {
				try {
					backUpAssignment[i] = listAssignment[i].clone();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
					String date = formatter.format(new Date());
					Date beginDate;
					beginDate = formatter.parse(date);
					Date endDate = formatter.parse(listAssignment[i][0]);
					long diff = endDate.getTime() - beginDate.getTime();
					long diffDays = diff / (24 * 60 * 60 * 1000);
					if (diffDays == 0) {
						listAssignment[i][0] = "D-day";
					} else if (diffDays > 0) {
						listAssignment[i][0] = "D-" + diffDays;
					} else {
						listAssignment[i][0] = "D+" + diffDays * -1;
					}
				} // ��¥�� ���ڰ� �ƴϸ� '-' �� ǥ��
				catch (Exception e) {
					listAssignment[i][0] = "-";
				}
			}

		} catch (Exception e) {
		}

		// ����� ����Ʈ�� �𵨿� ����
		modelAssignment = new DefaultTableModel(listAssignment, header) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int a, int b) {
				return false;
			}
		};

		// ���̺� ����
		jtAssignment = new JTable(modelAssignment);
		jtAssignment.getTableHeader().setReorderingAllowed(false);
		jtAssignment.getTableHeader().setResizingAllowed(false);
		// ���� ũ��
		jtAssignment.getColumnModel().getColumn(0).setMaxWidth(49);
		jtAssignment.getColumnModel().getColumn(0).setMinWidth(49);
		jtAssignment.getColumnModel().getColumn(0).setWidth(49);
		// ���� ũ��
		jtAssignment.getColumnModel().getColumn(1).setMaxWidth(55);
		jtAssignment.getColumnModel().getColumn(1).setMinWidth(55);
		jtAssignment.getColumnModel().getColumn(1).setWidth(55);
		// ���� ũ��
		jtAssignment.getColumnModel().getColumn(3).setMaxWidth(30);
		jtAssignment.getColumnModel().getColumn(3).setMinWidth(30);
		jtAssignment.getColumnModel().getColumn(3).setWidth(30);
		// �Ϸ� ũ��
		jtAssignment.getColumnModel().getColumn(4).setMaxWidth(30);
		jtAssignment.getColumnModel().getColumn(4).setMinWidth(30);
		jtAssignment.getColumnModel().getColumn(4).setWidth(30);

		jtAssignment.getTableHeader().setPreferredSize(new Dimension(0, 23));
		jtAssignment.setRowHeight(23);

		jtAssignment.getTableHeader().setFont(new Font("���õ������ Bold", Font.PLAIN, 15));
		jtAssignment.setFont(new Font("���õ������ Bold", Font.PLAIN, 15));

		// ����, ���� ��ư
		jtAssignment.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent mouseevent) {
				// ���� ���̸� ����, ���� �ź�
				if (isChanging) {
					return;
				}
				// ���� ��ư
				if (jtAssignment.getSelectedColumn() == 3) {
					// ���� ��
					isChanging = true;
					// ��� ° ������ ����
					tmpChanging = jtAssignment.getSelectedRow();
					// �ؽ�Ʈ ����
					jtDate.setText(backUpAssignment[tmpChanging][0]);
					jtSubject.setText(backUpAssignment[tmpChanging][1]);
					jtContent.setText(backUpAssignment[tmpChanging][2]);
					// ���� ��ư���� ����
					jbAdd.setIcon(new ImageIcon(this.getClass().getResource("/image/Change.png")));
					// Ŀ�� �̵�
					jtDate.requestFocusInWindow();
				} // ���� ��ư
				else if (jtAssignment.getSelectedColumn() == 4) {
					try {
						PrintWriter pw = new PrintWriter("C:/AssignmentReminder/AssignmentList.txt");
						for (int i = 0; i < listAssignment.length; i++) {
							// ������ ���̸� �����븮��Ʈ�� �߰��ϰ� continue
							if (i == jtAssignment.getSelectedRow()) {
								PrintWriter wbpw = new PrintWriter(
										new FileWriter("C:/AssignmentReminder/WastebasketList.txt", true));
								wbpw.println(backUpAssignment[i][0] + DIVISION + backUpAssignment[i][1] + DIVISION
										+ backUpAssignment[i][2] + DIVISION + PUSH + DIVISION + Del);
								wbpw.close();
								continue;
							}
							String data = backUpAssignment[i][0] + DIVISION + backUpAssignment[i][1] + DIVISION
									+ backUpAssignment[i][2] + DIVISION + PUSH + DIVISION + Del;
							pw.println(data);
						}
						pw.close();
						jfMain.remove(scroll);
						assignmentList();
					} catch (Exception e) {
					}
				}
				// �������� ���������� ������ ���ΰ�ħ
				if (isWastebasket) {
					Wastebasket.jfWastebasket.remove(Wastebasket.scrollWastebasket);
					Wastebasket.wastebasketList();
				}

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
		jtAssignment.repaint();

		// ��� ����
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = jtAssignment.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
		// ��ũ�� ����
		scroll = new JScrollPane(jtAssignment);
		if (cntAssignment >= 11) {
			scroll.setBounds(1, 36, 329, 270);
		} else {
			scroll.setBounds(1, 36, 329, cntAssignment * 23 + 23);
		}
		scroll.setOpaque(true);
		scroll.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		jfMain.getContentPane().add(scroll);

		jfMain.revalidate();
		jfMain.repaint();
	}

	// ���� �޼ҵ�
	public static void main(String[] args) {
		// �̹� ���� ���̸� ���� �� �̶�� �޼��� ��� �� ����
		try {
			isRun = new DatagramSocket(1103);
		} catch (SocketException e) {
			TrayIconHandler.registerTrayIcon(
					new ImageIcon(AssignmentReminder.class.getResource("/image/list.png")).getImage(), "���� �����δ�",
					new ActionListener() {
						// ���� Ŭ�� ��
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!isMain) {
								new AssignmentReminder();
							}
						}
					});
			TrayIconHandler.displayMessage("���� �����δ�", "���� �����δ��� �̹� ���� �� �Դϴ�", MessageType.INFO);
			System.exit(0);
		}

		// ���� Ŭ��
		TrayIconHandler.registerTrayIcon(
				new ImageIcon(AssignmentReminder.class.getResource("/image/list.png")).getImage(), "���� �����δ�",
				new ActionListener() {
					// ���� Ŭ�� ��
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!isMain) {
							new AssignmentReminder();
						}
					}
				});

		// �޴� �߰�
		TrayIconHandler.addItem("����", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isMain) {
					new AssignmentReminder();
				}
			}
		});

		// �޴� �߰�
		TrayIconHandler.addItem("����", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// ����� ���๮��
		TrayIconHandler.displayMessage("���� �����δ�", "���� �����δ��� �����մϴ�", MessageType.INFO);

		new AssignmentReminder();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	// â ����� is���� false�� �������� ���������� ����
	@Override
	public void windowClosing(WindowEvent e) {
		if (isWastebasket) {
			isWastebasket = false;
			Wastebasket.jfWastebasket.dispose();
		}
		isMain = false;
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