import java.io.*;
import java.net.*;
import java.util.*;

import protocal.*;
import static protocal.MyProtocol.*;

class ServerThread extends Thread {
	// ���浱ǰ�߳�socket
	private Socket currentSk;
	private BufferedReader br = null;
	private PrintStream ps = null;

	public ServerThread(Socket socket) {
		currentSk = socket;
	}

	public void run() {
		String line = null;
		try {
			System.out.println(Thread.currentThread().getName());
			// ��ǰScoket��Ӧ��������
			br = new BufferedReader(new InputStreamReader(currentSk.getInputStream()));

			// ��ȡ��ǰScoket��Ӧ���������
			ps = new PrintStream(currentSk.getOutputStream());

			while ((line = br.readLine()) != null) {
				// ����յ�ע����Ϣ
				if (line.startsWith(REGISTER) && line.endsWith(REGISTER)) {

					// ���ȥ��Э��ͷָ�������ʵ��Ϣ
					String[] splitMsg = ParseStr.getRealMsg(line);
					String result = Server.connSql.register(splitMsg[0], splitMsg[1], splitMsg[2],
							Integer.valueOf(splitMsg[3]));
					ps.println(result);
				}

				else
				// ����յ���¼��Ϣ
				if (line.startsWith(LOGIN) && line.endsWith(LOGIN)) {
					System.out.println("�յ���¼��Ϣ");
					// ���ȥ��Э��ͷָ�������ʵ��Ϣ
					String[] splitMsg = ParseStr.getRealMsg(line);

					// ���������û�,�Ƿ��ظ���¼
					for (UserInfo user : Server.clients.keySet()) {
						if (user.getName().equals(splitMsg[0])) {
							ps.println(LOGIN_AGAIN);
							return;
						}
					}

					// ���û��������봫�����ݿ���������֤
					String result = Server.connSql.login(splitMsg[0], splitMsg[1]);

					if (result.startsWith(LOGIN_SUCCESS) && result.endsWith(LOGIN_SUCCESS)) {
						// ֪ͨ�ͻ��˵�½�ɹ�
						ps.println(LOGIN_SUCCESS);
						// ���ȥ��Э�����Ϣ
						String realMsg = ParseStr.getCombMsg(result);
						// ������ʵ��Ϣ����UserInfo����
						UserInfo user = UserFactory.createUserInfo(realMsg);
						System.out.println(user.getName() + " " + user.getSex() + " "
								+ user.getScore() + " " + user.getIconIndex() + " "
								+ user.getHouseNum() + " " + user.getSeat());
						// ���������û���Ϣ
						Server.clients.put(user, currentSk);
						// ��ͻ��˷������������û�����Ϣ
						DealWithMsg.printlnAllUser(ps);
						// ֪ͨ�����ͻ������˼������
						DealWithMsg.joinHall(user, currentSk);
						System.out.println("��¼�ɹ�");
					} else {
						ps.println(LOGIN_ERROR);
					}
				}

				// ����յ�˽����Ϣ
				if (line.startsWith(PRIVATE_ROUND) && line.endsWith(PRIVATE_ROUND)) {
					System.out.println("�յ�˽����Ϣ");
					// ���ȥ��Э��ͷָ�������ʵ��Ϣ
					String[] splitMsg = ParseStr.getRealMsg(line);
					DealWithMsg.sendPrivate(splitMsg);
				}

				else

				// ����յ�Ⱥ����Ϣ
				if (line.startsWith(USER_ROUND) && line.endsWith(USER_ROUND)) {
					System.out.println(line);
					DealWithMsg.sendUserRound(line, currentSk);
				}

				else

				// �յ�����
				if (line.startsWith(LINK_COOR) && line.endsWith(LINK_COOR)) {
					// ���ȥ��Э��ͷָ�������ʵ��Ϣ
					String[] splitMsg = ParseStr.getRealMsg(line);
					System.out.println(splitMsg[0] + splitMsg[1] + splitMsg[2] + splitMsg[3]
							+ splitMsg[4]);
					// ת�����������
					DealWithMsg.sendCoor(splitMsg, currentSk);
				}

				else

				// ����յ��˳�������Ϣ
				if (line.startsWith(EXIT_HALL) && line.endsWith(EXIT_HALL)) {
					// Server.sendUserRound(line);
					for (int i = 0; i < 100; i++) {
						System.out.println(EXIT_HALL);
					}
				}

				else

				// ����յ����뷿����Ϣ
				if (line.startsWith(JOIN_HOUSE) && line.endsWith(JOIN_HOUSE)) {
					// ���ȥ��Э�����Ϣ
					String houseStr = ParseStr.getCombMsg(line);
					int houseNum = Integer.valueOf(houseStr);
					// ������Լ��뷿��
					if (DealWithMsg.canJionHouse(houseNum, currentSk)) {
						DealWithMsg.jionHouse(houseNum, currentSk);
					} else {
						// ��սģʽ
					}
				}

				else

				// ׼����Ϣ
				if (line.startsWith(GAME_PREPARE) && line.endsWith(GAME_PREPARE)) {
					// ���ȥ��Э��ͷָ�������ʵ��Ϣ
					String[] splitMsg = ParseStr.getRealMsg(line);
					DealWithMsg.preGame(splitMsg);
				}

				else

				// ����յ��˳�������Ϣ
				if (line.startsWith(EXIT_HOUSE) && line.endsWith(EXIT_HOUSE)) {
					// ���ȥ��Э��ͷָ�������ʵ��Ϣ
					String[] splitMsg = ParseStr.getRealMsg(line);
					System.out.println(splitMsg[0] + " " + splitMsg[1]);
					DealWithMsg.exitHouse(splitMsg, currentSk);
				}

				else

				if (line.startsWith(CREATE_HOUSE) && line.endsWith(CREATE_HOUSE)) {
					DealWithMsg.creatHouse(currentSk);
				}

			}

		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("�ͻ��˶Ͽ�:" + line);
			try {
				// �����û��˳�����
				DealWithMsg.exitHall(currentSk);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
