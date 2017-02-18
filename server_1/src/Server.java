import java.net.*;
import java.io.*;
import java.util.*;

import protocal.*;
import static protocal.MyProtocol.*;

public class Server {
	// �̶��˿�
	public static final int SERVER_PORT = 30001;
	// �������߿ͻ���Ϣ�Ͷ�ӦSocket
	public static UsMap<UserInfo, Socket> clients = new UsMap<UserInfo, Socket>();
	public static ConnectSql connSql = new ConnectSql();
	// Ĭ�Ϸ�����Ϊ18,���з������˲ſ����ӷ���
	public static int houseNum = 18;
	// ��������м������
	public static final int USER_NUM = 4;
	public static List<HouseInfo> houseList = new LinkedList<HouseInfo>();

	public void init() {
		// ��ʼ��������Ϣ,0��17��Ӧ����ĺ���
		for (int i = 0; i < houseNum; i++) {
			houseList.add(new HouseInfo());
		}

		ServerSocket ss = null;
		try {
			ss = new ServerSocket(SERVER_PORT);
			// ���ϻ�ÿͻ��˵�����
			while (true) {
				Socket currentSk = ss.accept();
				new ServerThread(currentSk).start();
			}
		} catch (IOException ex) {
			System.out.println("����������ʧ�ܣ��Ƿ�˿�" + SERVER_PORT + "�ѱ�ռ��");
		} finally {
			try {
				if (ss != null) {
					ss.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			System.exit(1);
		}
	}
}
