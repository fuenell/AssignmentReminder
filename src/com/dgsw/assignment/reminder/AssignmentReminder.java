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

	// 버전 정보
	public static final String VERSION = "V1.2";

	// 구분 상수
	public static final String DIVISION = "ㅸㅩㅨㅭㅬㅫㅱ";

	// 삭제 버튼 상수
	public static final String PUSH = "○";
	public static final String Del = "●";

	// 메인 창
	public static JFrame jfMain;
	public static Color frameColor = new Color(174, 217, 207);
	// 텍스트
	public static JTextField jtDate;
	public static JTextField jtSubject;
	public static JTextField jtContent;

	// 버튼
	public static JButton jbClassroom;
	public static JButton jbBand;
	public static JButton jbDispose;
	public static JButton jbLastWeek;
	public static JButton jbNextWeek;
	public static JButton jbWastebasket;
	public static JButton jbRefresh;
	public static JButton jbAdd;

	// 요일 선택 버튼
	public static JButton jbWeek[] = new JButton[7];

	// 요일 문자열
	public static String strWeek[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };

	// 과제 리스트
	public static JTable jtAssignment;
	public static String[][] listAssignment;
	public static String[][] backUpAssignment;
	public static JScrollPane scroll;
	public static String header[] = { "기한", "과목", "해야할 일", "수정", "완료" };
	public static DefaultTableModel modelAssignment;

	// 날짜
	public static Calendar cal = Calendar.getInstance();
	public static Calendar calToday = Calendar.getInstance();

	// 프로그램이 실행중인지
	public static DatagramSocket isRun;

	// 메인창 켜져 있는지
	public static boolean isMain = false;

	// 휴지통 켜져 있는지
	public static boolean isWastebasket = false;

	// 수정 중 인지
	public static boolean isChanging = false;
	public static int tmpChanging;

	// 여기부터 메소드

	// (생성자)메인 프레임 컴포넌트 추가
	public AssignmentReminder() {

		// 생성할 파일경로 지정
		String path = "C:/AssignmentReminder";
		// 파일 객체 생성
		File file = new File(path);
		// !표를 붙여주어 파일이 존재하지 않는 경우의 조건을 걸어줌
		if (!file.exists()) {
			// 디렉토리 생성 메서드
			file.mkdirs();
		}

		// 창 켜짐
		isMain = true;
		// 색 설정 불러오기
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
		// 메인 프레임 설정
		jfMain = new JFrame("과제 리마인더");
		jfMain.getContentPane().setLayout(null);
		jfMain.setUndecorated(true);
		jfMain.getContentPane().setBackground(frameColor);
		jfMain.setSize(331, 373);
		jfMain.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width - 331, 0);
		jfMain.setIconImage(new ImageIcon(this.getClass().getResource("/image/list.png")).getImage());
		jfMain.addWindowListener(this);

		// ↓↓ 여기 밑에는 버튼 ↓↓

		// 클래스룸 바로가기 버튼
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

		// 밴드 바로가기 버튼
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

		// 휴지통 버튼
		jbWastebasket = new JButton(new ImageIcon(this.getClass().getResource("/image/Wastebasket.png")));
		jbWastebasket.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbWastebasket.setIcon(new ImageIcon(this.getClass().getResource("/image/Wastebasket.png")));
				// 과제 리스트 새로고침
				if (!isWastebasket) {
					isWastebasket = true;
					new Wastebasket();
				}
			}

			// 눌렀을 때 이미지 변경
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

		// 새로고침 버튼
		jbRefresh = new JButton(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
		jbRefresh.setFont(new Font("굴림", Font.PLAIN, 10));
		jbRefresh.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbRefresh.setIcon(new ImageIcon(this.getClass().getResource("/image/Refresh.png")));
				// 과제 리스트 새로고침
				jfMain.remove(scroll);
				assignmentList();
			}

			// 눌렀을 때 이미지 변경
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

		// 닫기 버튼
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

		// 과제 추가 버튼
		jbAdd = new JButton(new ImageIcon(this.getClass().getResource("/image/Add.png")));
		jbAdd.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + 과제 추가 메소드 실행
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				addAssignment();
			}

			// 눌렀을 때 이미지 변경 수정 중이면 수정 이미지 출력
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

		// 일주일 버튼
		for (int i = 0; i < jbWeek.length; i++) {
			// 오늘 버튼만 색 변경
			if (calToday.get(Calendar.DAY_OF_WEEK) - 1 == i) {
				jbWeek[i] = new JButton(new ImageIcon(this.getClass().getResource("/image/" + strWeek[i] + "T.png")));
			} else {
				jbWeek[i] = new JButton(new ImageIcon(this.getClass().getResource("/image/" + strWeek[i] + ".png")));
			}
			jbWeek[i].addMouseListener(new MouseListener() {
				// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
				@Override
				public void mouseReleased(MouseEvent e) {

					String[] jbList = e.getSource().toString().split(",");
					int week = (Integer.parseInt(jbList[1]) - 1) / 27;
					// 오늘 버튼만 색 변경
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

				// 눌렀을 때 이미지 변경
				@Override
				public void mousePressed(MouseEvent e) {
					String[] jbList = e.getSource().toString().split(",");
					int week = (Integer.parseInt(jbList[1]) - 1) / 27;
					// 오늘 버튼만 색 변경
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

				// 마우스 올렸을 때 이미지 변경
				@Override
				public void mouseEntered(MouseEvent e) {
				}

				// 나갔을 때 이미지 변경
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

		// 지난주 버튼
		jbLastWeek = new JButton(new ImageIcon(this.getClass().getResource("/image/Lastweek.png")));
		jbLastWeek.setFont(new Font("굴림", Font.PLAIN, 5));
		jbLastWeek.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbLastWeek.setIcon(new ImageIcon(this.getClass().getResource("/image/Lastweek.png")));
				seekWeek(-1);
			}

			// 눌렀을 때 이미지 변경
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

		// 다음주 버튼
		jbNextWeek = new JButton(new ImageIcon(this.getClass().getResource("/image/Nextweek.png")));
		jbNextWeek.setFont(new Font("굴림", Font.PLAIN, 5));
		jbNextWeek.addMouseListener(new MouseListener() {
			// 땠을 때 이미지 변경 + date 요일에 맞춰 변경
			@Override
			public void mouseReleased(MouseEvent mouseevent) {
				jbNextWeek.setIcon(new ImageIcon(this.getClass().getResource("/image/Nextweek.png")));
				seekWeek(1);
			}

			// 눌렀을 때 이미지 변경
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

		// ↓↓ 여기 밑에는 텍스트 ↓↓

		// 날짜
		jtDate = new JTextField();
		jtDate.setHorizontalAlignment(SwingConstants.CENTER);
		jtDate.setFont(new Font("경기천년제목 Bold", Font.PLAIN, 14));
		jtDate.setBounds(1, 337, 78, 35);
		jtDate.setColumns(10);
		jtDate.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		jtDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addAssignment();
			}
		});
		jfMain.getContentPane().add(jtDate);

		// 과목
		jtSubject = new JTextField();
		jtSubject.setFont(new Font("경기천년제목 Bold", Font.PLAIN, 14));
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

		// 내용
		jtContent = new JTextField();
		jtContent.setFont(new Font("경기천년제목 Bold", Font.PLAIN, 14));
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

		// 과제 리스트 추가
		assignmentList();

		jfMain.setVisible(true);

	}

	// 과제 추가 함수
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
				// 과제 리스트 새로고침
				jfMain.remove(scroll);
				assignmentList();
			} catch (Exception e) {
			}
			isChanging = false;
		}

		try {

			// 과목 또는 할일 둘다 비어있으면 등록X
			if (jtSubject.getText().equals("") && jtContent.getText().equals("")) {
				return;
			}

			// 날짜 0,8글자 아니면 등록X
			if (!(jtDate.getText().length() == 8) && !(jtDate.getText().length() == 0)) {
				return;
			}

			// jtDate가 8글자일때 숫자가 아니면 catch로 빠져나감
			if (jtDate.getText().length() == 8) {
				Integer.parseInt(jtDate.getText());
			}

			// 텍스트에 저장
			PrintWriter pw = new PrintWriter(new FileWriter("C:/AssignmentReminder/AssignmentList.txt", true));
			pw.println(jtDate.getText() + DIVISION + jtSubject.getText() + DIVISION + jtContent.getText() + DIVISION
					+ PUSH + DIVISION + Del);
			pw.close();

			// 텍스트 비우기
			jtDate.setText("");
			jtSubject.setText("");
			jtContent.setText("");

		} catch (Exception e) {
		}
		// 과제 리스트 새로고침
		jfMain.remove(scroll);
		assignmentList();
	}

	// 일주일 이동 버튼 함수
	public static void seekWeek(int week) {

		// 요일 만큼 더한다.
		cal.add(Calendar.DAY_OF_YEAR, week * 7);

		// 현재 년도, 월, 일
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);

		jtDate.setText(year + "" + String.format("%02d", month) + "" + String.format("%02d", date));

		jtDate.requestFocusInWindow();

	}

	// 요일 버튼 함수
	public static void seekDay(int day) {

		// 요일 만큼 더한다.
		cal.add(Calendar.DAY_OF_YEAR, -(cal.get(Calendar.DAY_OF_WEEK) - day));

		// 현재 년도, 월, 일
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);

		jtDate.setText(year + "" + String.format("%02d", month) + "" + String.format("%02d", date));

		jtDate.requestFocusInWindow();

	}

	// 과제 리스트 출력 함수
	public static void assignmentList() {
		int cntAssignment = 0;
		try {
			// 텍스트를 listAssignment[][]로 옮김
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

			// listAssignment[][0] 정렬
			for (int i = 0; i < listAssignment.length; i++) {
				for (int j = 1; j < listAssignment.length - i; j++) {
					try {
						if (Integer.parseInt(listAssignment[j - 1][0]) > Integer.parseInt(listAssignment[j][0])) {
							String tmp[] = listAssignment[j - 1].clone();
							listAssignment[j - 1] = listAssignment[j].clone();
							listAssignment[j] = tmp.clone();
						}
					} // 숫자로 변환되지 않을 시 뒤로 보냄
					catch (Exception e) {
						// 둘 다 숫자거나 앞에 것만 숫자면 안 바꿈
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

			// 날짜를 D-day 표기법으로 변경
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
				} // 날짜가 숫자가 아니면 '-' 로 표기
				catch (Exception e) {
					listAssignment[i][0] = "-";
				}
			}

		} catch (Exception e) {
		}

		// 헤더와 리스트를 모델에 넣음
		modelAssignment = new DefaultTableModel(listAssignment, header) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int a, int b) {
				return false;
			}
		};

		// 테이블 설정
		jtAssignment = new JTable(modelAssignment);
		jtAssignment.getTableHeader().setReorderingAllowed(false);
		jtAssignment.getTableHeader().setResizingAllowed(false);
		// 기한 크기
		jtAssignment.getColumnModel().getColumn(0).setMaxWidth(49);
		jtAssignment.getColumnModel().getColumn(0).setMinWidth(49);
		jtAssignment.getColumnModel().getColumn(0).setWidth(49);
		// 과목 크기
		jtAssignment.getColumnModel().getColumn(1).setMaxWidth(55);
		jtAssignment.getColumnModel().getColumn(1).setMinWidth(55);
		jtAssignment.getColumnModel().getColumn(1).setWidth(55);
		// 수정 크기
		jtAssignment.getColumnModel().getColumn(3).setMaxWidth(30);
		jtAssignment.getColumnModel().getColumn(3).setMinWidth(30);
		jtAssignment.getColumnModel().getColumn(3).setWidth(30);
		// 완료 크기
		jtAssignment.getColumnModel().getColumn(4).setMaxWidth(30);
		jtAssignment.getColumnModel().getColumn(4).setMinWidth(30);
		jtAssignment.getColumnModel().getColumn(4).setWidth(30);

		jtAssignment.getTableHeader().setPreferredSize(new Dimension(0, 23));
		jtAssignment.setRowHeight(23);

		jtAssignment.getTableHeader().setFont(new Font("경기천년제목 Bold", Font.PLAIN, 15));
		jtAssignment.setFont(new Font("경기천년제목 Bold", Font.PLAIN, 15));

		// 수정, 삭제 버튼
		jtAssignment.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent mouseevent) {
				// 수정 중이면 수정, 삭제 거부
				if (isChanging) {
					return;
				}
				// 수정 버튼
				if (jtAssignment.getSelectedColumn() == 3) {
					// 수정 중
					isChanging = true;
					// 몇번 째 줄인지 저장
					tmpChanging = jtAssignment.getSelectedRow();
					// 텍스트 변경
					jtDate.setText(backUpAssignment[tmpChanging][0]);
					jtSubject.setText(backUpAssignment[tmpChanging][1]);
					jtContent.setText(backUpAssignment[tmpChanging][2]);
					// 수정 버튼으로 변경
					jbAdd.setIcon(new ImageIcon(this.getClass().getResource("/image/Change.png")));
					// 커서 이동
					jtDate.requestFocusInWindow();
				} // 삭제 버튼
				else if (jtAssignment.getSelectedColumn() == 4) {
					try {
						PrintWriter pw = new PrintWriter("C:/AssignmentReminder/AssignmentList.txt");
						for (int i = 0; i < listAssignment.length; i++) {
							// 삭제할 열이면 휴지통리스트에 추가하고 continue
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
				// 휴지통이 켜져있으면 휴지통 새로고침
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

		// 가운데 정렬
		DefaultTableCellRenderer tScheduleCellRenderer = new DefaultTableCellRenderer();
		tScheduleCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		TableColumnModel tcmSchedule = jtAssignment.getColumnModel();
		for (int i = 0; i < tcmSchedule.getColumnCount(); i++) {
			tcmSchedule.getColumn(i).setCellRenderer(tScheduleCellRenderer);
		}
		// 스크롤 설정
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

	// 메인 메소드
	public static void main(String[] args) {
		// 이미 실행 중이면 실행 중 이라는 메세지 출력 후 종료
		try {
			isRun = new DatagramSocket(1103);
		} catch (SocketException e) {
			TrayIconHandler.registerTrayIcon(
					new ImageIcon(AssignmentReminder.class.getResource("/image/list.png")).getImage(), "과제 리마인더",
					new ActionListener() {
						// 더블 클릭 시
						@Override
						public void actionPerformed(ActionEvent e) {
							if (!isMain) {
								new AssignmentReminder();
							}
						}
					});
			TrayIconHandler.displayMessage("과제 리마인더", "과제 리마인더가 이미 실행 중 입니다", MessageType.INFO);
			System.exit(0);
		}

		// 더블 클릭
		TrayIconHandler.registerTrayIcon(
				new ImageIcon(AssignmentReminder.class.getResource("/image/list.png")).getImage(), "과제 리마인더",
				new ActionListener() {
					// 더블 클릭 시
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!isMain) {
							new AssignmentReminder();
						}
					}
				});

		// 메뉴 추가
		TrayIconHandler.addItem("열기", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isMain) {
					new AssignmentReminder();
				}
			}
		});

		// 메뉴 추가
		TrayIconHandler.addItem("종료", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		// 실행시 실행문장
		TrayIconHandler.displayMessage("과제 리마인더", "과제 리마인더를 실행합니다", MessageType.INFO);

		new AssignmentReminder();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	// 창 종료시 is변수 false로 휴지통이 켜져있으면 끄기
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