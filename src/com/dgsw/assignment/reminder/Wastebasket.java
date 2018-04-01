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

	// 휴지통 프레임
	public static JFrame jfWastebasket;

	public static JLabel jlVersion;

	// 색 변경 버튼
	public static JButton jbColor[] = new JButton[5];
	// 회색, 분홍색, 노란색, 파란색, 검은색
	public static int colorList[][] = { { 202, 203, 198 }, { 254, 184, 184 }, { 253, 228, 159 }, { 174, 217, 207 },
			{ 57, 51, 63 } };

	// 새로고침 버튼
	public static JButton jbWasteRefresh;
	// 휴지통 닫기 버튼
	public static JButton jbWasteDispose;

	// 휴지통 리스트 변수
	public static JTable jtWastebasket;
	public static String[][] listWastebasket;
	public static String[][] backUpWastebasket;
	public static JScrollPane scrollWastebasket;
	public static String header[] = { "기한", "과목", "해야할 일", "복구", "삭제" };
	public static DefaultTableModel modelWastebasket;

	// 휴지통 프레임 컴포넌트 추가
	public Wastebasket() {

		// 휴지통 프레임 설정
		jfWastebasket = new JFrame("휴지통");
		jfWastebasket.getContentPane().setLayout(null);
		jfWastebasket.setSize(332, 312);
		jfWastebasket.setUndecorated(true);
		jfWastebasket.getContentPane().setBackground(AssignmentReminder.frameColor);
		jfWastebasket.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 662, 0);
		jfWastebasket.setIconImage(new ImageIcon(this.getClass().getResource("/image/list.png")).getImage());
		jfWastebasket.addWindowListener(this);

		// 버전 라벨
		jlVersion = new JLabel("<html>" + "Assignment<br>Reminder " + AssignmentReminder.VERSION + "</html>");
		jlVersion.setFont(new Font("Tmon몬소리 Black", Font.PLAIN, 10));
		jlVersion.setHorizontalAlignment(SwingConstants.CENTER);
		jlVersion.setBounds(1, 2, 83, 34);
		jfWastebasket.getContentPane().add(jlVersion);

		// 색 변경 버튼
		for (int i = 0; i < 5; i++) {
			jbColor[i] = new JButton(new ImageIcon(this.getClass().getResource("/image/Color0" + (i + 1) + ".png")));
			jbColor[i].setBounds(i * 30 + 90, 5, 26, 25);
			jbColor[i].setBorderPainted(false);
			jbColor[i].setFocusPainted(false);
			jbColor[i].setContentAreaFilled(false);
			jbColor[i].addMouseListener(new MouseListener() {
				// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
				@Override
				public void mouseReleased(MouseEvent e) {
					// MouseEvent정보를 27
					String[] jbList = e.getSource().toString().split(",");
					int num = (Integer.parseInt(jbList[1]) - 90) / 30;
					jbColor[num]
							.setIcon(new ImageIcon(this.getClass().getResource("/image/Color0" + (num + 1) + ".png")));
					// 색 변수 값 변경
					AssignmentReminder.frameColor = new Color(colorList[num][0], colorList[num][1], colorList[num][2]);
					// 메인창 색 변경, 새로고침
					AssignmentReminder.jfMain.getContentPane().setBackground(AssignmentReminder.frameColor);
					AssignmentReminder.jfMain.revalidate();
					AssignmentReminder.jfMain.repaint();
					// 휴지통 색 변경, 새로고침
					jfWastebasket.getContentPane().setBackground(AssignmentReminder.frameColor);
					jfWastebasket.revalidate();
					jfWastebasket.repaint();
					// 색 설정 저장하기 (텍스트 아웃풋)
					try {
						PrintWriter pw = new PrintWriter(new FileWriter("C:/AssignmentReminder/Setting.txt"));
						pw.println(colorList[num][0] + ", " + colorList[num][1] + ", " + colorList[num][2]);
						pw.close();
					} catch (Exception e2) {
					}
				}

				// 눌렀을 때 이미지 변경
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

				// 마우스 올렸을 때 이미지 변경
				@Override
				public void mouseEntered(MouseEvent e) {
				}

				// 나갔을 때 이미지 변경
				@Override
				public void mouseExited(MouseEvent e) {
				}
			});
			jfWastebasket.getContentPane().add(jbColor[i]);
		}

		// 새로고침 버튼
		jbWasteRefresh = new JButton(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
		jbWasteRefresh.setFont(new Font("굴림", Font.PLAIN, 10));
		jbWasteRefresh.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbWasteRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
				// 과제 리스트 새로고침
				jfWastebasket.remove(scrollWastebasket);
				wastebasketList();
			}

			// 눌렀을 때 이미지 변경
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

		// 닫기 버튼
		jbWasteDispose = new JButton(new ImageIcon(this.getClass().getResource("/image/WasteDispose.png")));
		jbWasteDispose.setFont(new Font("굴림", Font.PLAIN, 10));
		jbWasteDispose.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbWasteRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/WasteDispose.png")));
				AssignmentReminder.isWastebasket = false;
				jfWastebasket.dispose();
			}

			// 눌렀을 때 이미지 변경
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

	// 휴지통 리스트 출력 함수
	static public void wastebasketList() {
		int cntWastebasket = 0;
		try {
			// 텍스트를 listWastebasket[][]로 옮김
			try {
				String tmpWastebasket[][] = new String[1000][5];
				BufferedReader br = new BufferedReader(new FileReader("C:/AssignmentReminder/WastebasketList.txt"));
				String line = br.readLine();
				// cntWastebasket 는 자료개수
				for (int i = 0; line != null; i++, cntWastebasket++) {
					tmpWastebasket[i] = line.split(AssignmentReminder.DIVISION);
					line = br.readLine();
				}
				// 새로 cntWastebasket 크기만큼 새로운 배열 생성
				listWastebasket = new String[cntWastebasket][5];
				backUpWastebasket = new String[cntWastebasket][5];
				for (int j = 0; j < cntWastebasket; j++) {
					listWastebasket[j] = tmpWastebasket[j].clone();
				}
				br.close();
			} catch (Exception e) {
			}

			// listWastebasket[][0] 정렬
			for (int i = 0; i < listWastebasket.length; i++) {
				for (int j = 1; j < listWastebasket.length - i; j++) {
					try {
						if (Integer.parseInt(listWastebasket[j - 1][0]) > Integer.parseInt(listWastebasket[j][0])) {
							String tmp[] = listWastebasket[j - 1].clone();
							listWastebasket[j - 1] = listWastebasket[j].clone();
							listWastebasket[j] = tmp.clone();
						}
					} // 숫자로 변환되지 않을 시 뒤로 보냄
					catch (Exception e) {
						// 둘 다 숫자거나 앞에 것만 숫자면 안 바꿈
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

			// 날짜를 D-day 표기법으로 변경
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
				} // 날짜가 숫자가 아니면 '-' 로 표기
				catch (Exception e) {
					listWastebasket[i][0] = "-";
				}
			}

		} catch (Exception e) {
		}

		// 헤더와 리스트를 모델에 넣음
		modelWastebasket = new DefaultTableModel(listWastebasket, header) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int a, int b) {
				return false;
			}
		};

		// 테이블 설정
		jtWastebasket = new JTable(modelWastebasket);
		jtWastebasket.getTableHeader().setReorderingAllowed(false);
		jtWastebasket.getTableHeader().setResizingAllowed(false);

		// 남은 날짜
		jtWastebasket.getColumnModel().getColumn(0).setMaxWidth(49);
		jtWastebasket.getColumnModel().getColumn(0).setMinWidth(49);
		jtWastebasket.getColumnModel().getColumn(0).setWidth(49);
		// 과목
		jtWastebasket.getColumnModel().getColumn(1).setMaxWidth(55);
		jtWastebasket.getColumnModel().getColumn(1).setMinWidth(55);
		jtWastebasket.getColumnModel().getColumn(1).setWidth(55);
		// 복구
		jtWastebasket.getColumnModel().getColumn(3).setMaxWidth(30);
		jtWastebasket.getColumnModel().getColumn(3).setMinWidth(30);
		jtWastebasket.getColumnModel().getColumn(3).setWidth(30);
		// 삭제
		jtWastebasket.getColumnModel().getColumn(4).setMaxWidth(30);
		jtWastebasket.getColumnModel().getColumn(4).setMinWidth(30);
		jtWastebasket.getColumnModel().getColumn(4).setWidth(30);

		jtWastebasket.getTableHeader().setPreferredSize(new Dimension(0, 23));
		jtWastebasket.setRowHeight(23);

		jtWastebasket.getTableHeader().setFont(new Font("경기천년제목 Bold", Font.PLAIN, 15));
		jtWastebasket.setFont(new Font("경기천년제목 Bold", Font.PLAIN, 15));

		// 복구,삭제 버튼
		jtWastebasket.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent mouseevent) {
				// 수정 중에는 복구, 삭제 불가능
				if (AssignmentReminder.isChanging) {
					return;
				}
				// 3번 째 누르면 복구
				if (jtWastebasket.getSelectedColumn() == 3) {
					try {
						PrintWriter pw = new PrintWriter("C:/AssignmentReminder/WastebasketList.txt");
						for (int i = 0; i < listWastebasket.length; i++) {
							// 복구 할 배열이면 AssignmentList.txt에 추가하고 continue
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
				} // 4번 째 누르면 삭제
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

		// 가운데 정렬
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = jtWastebasket.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}

		// 스크롤 설정
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

	// 창 꺼지면 is 변수 false로
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
