import java.net.*;
import java.awt.Checkbox;
import java.io.*;
import java.util.*;

import static protocal.MyProtocol.*;

import java.util.*;

import protocal.*;

//������Ϣ������
class DealWithMsg 
{
	public static void printlnAllUser(PrintStream ps)
	{
		//ѭ�����������û���Ϣ���ͻ���
		String msg = "";
		for(UserInfo user : Server.clients.keySet())
		{
			msg  = UPDATE_ALL + user.getName() 
				+ SPLIT_SIGN + user.getSex()
				+ SPLIT_SIGN + user.getScore()
				+ SPLIT_SIGN + user.getIconIndex()
				+ SPLIT_SIGN + user.getHouseNum()
				+ SPLIT_SIGN + user.getSeat()
				+ UPDATE_ALL;
			ps.println(msg);
		}
		
		HouseInfo houseInfo;
		for (int i = 0; i < Server.houseList.size(); i++ )
		{
			houseInfo = Server.houseList.get(i);
			//��Ϸ�Ƿ�ʼ,������򽫷���ŷ����ͻ���
			if(houseInfo.getGameState())
			{
				msg = HOUSE_INFO + i + HOUSE_INFO;
				ps.println(msg);
			}
		}
		//�û���Ϣ������Ϻ�ͳ�οͻ���ֹͣ������Ϣ
		ps.println(STOP);
	}
	
	//֪ͨ�����ͻ������˼������,�����ͼ����ߵ�����
	public static void joinHall(UserInfo joinUser , Socket joinSk)
		throws Exception
	{
		String msg = null;
		for(Socket socket : Server.clients.valueSet())
		{
			if(socket != joinSk)
			{
				msg  = JOIN_HALL + joinUser.getName() 
						+ SPLIT_SIGN + joinUser.getSex()
						+ SPLIT_SIGN + joinUser.getScore()
						+ SPLIT_SIGN + joinUser.getIconIndex()
						+ SPLIT_SIGN + joinUser.getHouseNum()
						+ SPLIT_SIGN + joinUser.getSeat()
						+ JOIN_HALL;
				prinlnMsg(socket , msg);
			}
		}
	}
	
	//����˽����Ϣ�ķ���
	public static void sendPrivate(String[] splits)
		throws Exception
	{
		String name = splits[0];
		String msg = splits[1];
		
		for(UserInfo user : Server.clients.keySet())
		{
			if(user.getName().equals(name))
			{
				Socket socket = Server.clients.get(user);
				prinlnMsg(socket 
					, PRIVATE_ROUND + msg + PRIVATE_ROUND);
				return;
			}
			//ps.println(msg);
			//System.out.println("---"+msg);
		}	
	}
	
	//Ⱥ�ķ���
	public static void sendUserRound(String msg , Socket socket)
		throws Exception
	{
		//���ȥ��Э��ͷָ�������ʵ��Ϣ
		String[] splitMsg = ParseStr.getRealMsg(msg);
		String model = splitMsg[0];

		UserInfo user = Server.clients.getKeyByValue(socket);
		
		if(model.equals(HALL_ROUND))
		{
			for(Socket sk : Server.clients.valueSet())
			{
				prinlnMsg(sk , msg);
			}
		}
		
		if(model.equals(GAME_ROUND))
		{
			HouseInfo houseInfo = Server.houseList.get(user.getHouseNum());
			for(int i = 0 ; i < Server.USER_NUM ; i++)
			{ 
				if(houseInfo.userPrint[i] != null)
				{
					houseInfo.userPrint[i].println(msg);	
				}
			}
		}

	}

	public static void exitHall(Socket exitSk)
		throws Exception
	{
		UserInfo exitUser;
		//����socket��ȡ��Ӧ���û���Ϣ
		exitUser = Server.clients.getKeyByValue(exitSk);
		
		//������û�����Ϸ��������
		if(exitUser.getHouseNum() > -1)
		{
			HouseInfo houseInfo = 
				Server.houseList.get(exitUser.getHouseNum());
			//ɾ�����û��ڷ��������Ϣ
			houseInfo.removeUser(exitUser.getSeat());
			//���������Ϸ��
			if (houseInfo.getGameState())
			{
				//����ǿ��
				forceExit(houseInfo , exitUser);
			}
		}

		//ɾ���������д˿ͻ��˵���Ϣ
		Server.clients.remove(exitUser);  
		String msg = EXIT_HALL + exitUser.getName() + EXIT_HALL;;	
		//֪ͨ�����ͻ������˳�
		for(Socket sk : Server.clients.valueSet())
		{
			prinlnMsg(sk , msg);
		}
	}

	//�Ƿ���Խ�����Ϸ����
	public static boolean canJionHouse(int houseNum , Socket socket)
		throws Exception
	{
		HouseInfo houseInfo = Server.houseList.get(houseNum);
		UserInfo user = Server.clients.getKeyByValue(socket);
		
		String msg ="";
		//������û����˷��ź�����,�򲻿��ظ������ķ���
		if(user.getHouseNum() != -1 ||user.getSeat() != -1)
		{
			return false;
		}
		
		//��������δ������Ϸδ��ʼ�ſɽ���
		if(houseInfo.getCrruntNum() >= Server.USER_NUM ||
			houseInfo.getGameState() == true)
		{
			msg = JOIN_HOUSE_ERROR+JOIN_HOUSE_ERROR;
			prinlnMsg(socket , msg);
		}
		else
		{
			return true;
		}

		return false;
	}
	
	//���뷿��
	public static void jionHouse(int houseNum , Socket socket)
		throws Exception
	{
		//���ͨ�����Ż�ø÷�����Ϣ
		HouseInfo houseInfo = Server.houseList.get(houseNum);
		UserInfo user = Server.clients.getKeyByValue(socket);
		//���浱ǰ�������ҵ������
		PrintStream joinPs = null;
		int seat = -1;
		//�����ĸ���λ�п�,���뵽��һ���յ���λ
		for(int i = 0 ; i < Server.USER_NUM ; i++)
		{
			if(houseInfo.userPrint[i] == null)
			{
				//���ü����û��ķ���,����
				user.setHouseNum(houseNum);
				user.setSeat(i);
				//��ӷ������Ϣ
				joinPs = new PrintStream(socket.getOutputStream());
				houseInfo.userPrint[i] = joinPs;
				houseInfo.addUser(i , user.getName() , joinPs);
				seat = i;
				break;
			}
			System.out.println(houseInfo.userState[i]);
		}
		
		/*
		  ֪ͨ�ͻ��˼��뷿��ɹ�,�����߿ͻ������˼����ź�����
		  ����Ǳ��û�,����±��û�,����������û���ͬ����
		  ����������û���ͬ����Ϣ
		*/
		String msg = JOIN_HOUSE_SUCCESS + user.getName() 
			+ SPLIT_SIGN + houseNum 
			+ SPLIT_SIGN + seat 
			+ JOIN_HOUSE_SUCCESS;
		for(Socket sk : Server.clients.valueSet())
		{
			prinlnMsg(sk , msg);
		}
		
		//����ǰ������ҵ���Ϣ��װ���ַ������͸��ͻ���
		for(int i = 0 ; i < Server.USER_NUM ; i++)
		{
			//i��ʾ����
			joinPs.println(HOUSE_CHANGE + i 
				+ SPLIT_SIGN + houseInfo.useName[i] 
				+ SPLIT_SIGN + houseInfo.userState[i] 
				+  HOUSE_CHANGE);
		}
	}

	public static void exitHouse(String[] splits, Socket socket)
		throws Exception
	{
		int houseNum = Integer.valueOf(splits[0]);
		int seat = Integer.valueOf(splits[1]);

		if (houseNum == -1 || seat == -1)
		{
			return;
		}
		//ͨ�����Ż�ø÷�����Ϣ
		HouseInfo houseInfo = Server.houseList.get(houseNum);
		UserInfo user = Server.clients.getKeyByValue(socket);
		
		//ɾ�����û��ڷ��������Ϣ
		houseInfo.removeUser(seat);
		//���ø��û��ڷ������б���ķ��ź�����Ϊ��
		user.setHouseNum(-1);
		user.setSeat(-1);

		String msg = null;
		
		//���������Ϸ��
		if (houseInfo.getGameState())
		{
			//����ǿ��
			forceExit(houseInfo , user);
		}

		msg = EXIT_HOUSE + user.getName() 
			+ SPLIT_SIGN + houseNum 
			+ SPLIT_SIGN + seat 
			+ EXIT_HOUSE;
		//֪ͨ���пͻ������û��˳�����
		for(Socket sk : Server.clients.valueSet())
		{
			prinlnMsg(sk , msg);
		}
	}
	
	//��������
	public static void sendCoor(String[] splits , Socket socket)
		throws Exception
	{
		UserInfo user = Server.clients.getKeyByValue(socket);
		HouseInfo houseInfo = Server.houseList.get(user.getHouseNum());
		
		String msg = LINK_COOR + user.getSeat() 
			+ SPLIT_SIGN + splits[0] 
			+ SPLIT_SIGN + splits[1]
			+ SPLIT_SIGN + splits[2]
			+ SPLIT_SIGN + splits[3]
			+ SPLIT_SIGN + splits[4]
			+ LINK_COOR;
		
		String doneStr = splits[4].substring(0 
			, splits[4].lastIndexOf("%")); 
		int done = Integer.valueOf(doneStr);
		//�ڷ�������¼�ڸ���ҵ���ɶ�
		houseInfo.setDone(user.getSeat() , done);
		
		//ת����ͬ�����
		for(int i = 0 ; i < Server.USER_NUM ; i++)
		{ 
			if(houseInfo.userPrint[i] != null
				&& !houseInfo.useName[i].equals(user.getName()))
			{
				houseInfo.userPrint[i].println(msg);
			}
		}
		
		msg = GAME_OVER;
		int index = 0;
		if(done == 100)
		{
			for(int i = 0 ; i < Server.USER_NUM ; i++)
			{ 
				if(houseInfo.userPrint[i] != null)
				{
					
					if(index > 0){msg += SPLIT_SIGN;}
					//��÷���
					int score = assignScore(houseInfo.getDone(i) 
						, houseInfo.getUserNum());
					msg += houseInfo.useName[i] + SPLIT_SIGN + score;
					index ++;
					System.out.println("��λ"+i+":���"+houseInfo.getDone(i));
				}
			}
			msg += GAME_OVER;
			
			if(!msg.equals(GAME_OVER))
			{
				//֪ͨ���пͻ�����Ϸ����
				for(Socket sk : Server.clients.valueSet())
				{
					prinlnMsg(sk , msg);
				}
			}
			
			//�����ڸ÷�����Ϣ
			houseInfo.reSetInfo();
			//�����ڸ÷�����ɶ�
			houseInfo.reSetDone();
		}
		
	}
	
	//����յ�׼����Ϣ
	public static void preGame(String[] splits)
		throws Exception
	{
		int houseNum = Integer.valueOf(splits[0]);
		int seat = Integer.valueOf(splits[1]);
		//ͨ�����Ż�ø÷�����Ϣ
		HouseInfo houseInfo = Server.houseList.get(houseNum);
		//���÷������и÷������λ��׼��״̬
		houseInfo.userState[seat] = true;
		
		//֪ͨ����ͬ���Ŀͻ�������׼��
		for(int i = 0 ; i < Server.USER_NUM ; i++)
		{ 
			if(houseInfo.userPrint[i] != null)
			{
				houseInfo.userPrint[i].println(GAME_PREPARE 
					+ seat
					+ GAME_PREPARE);
			}
		}
		
		int preNum = 0;
		//ͳ��׼��������
		for(int i = 0 ; i < Server.USER_NUM ; i++)
		{
			if(houseInfo.userState[i])
			{
				preNum ++;
			}
		}
		
		//�����������=׼������,�Ϳ�ʼ��Ϸ
		if(houseInfo.getUserNum() == preNum && preNum > 1)
		{
			houseInfo.setGameState(true);
			//������ɶ�
			houseInfo.reSetDone();
			int[][] nodes = Checker.newGame(14, 8, 12, Mode.classic);
			StringBuilder builder = new StringBuilder();
			for(int i = 0; i < nodes.length; i++) {
				for(int j = 0; j < nodes[0].length; j++) {
					builder.append(nodes[i][j] + SPLIT_SIGN);
				}
			}
			String msg = GAME_START + houseNum+SPLIT_SIGN+builder.toString() + GAME_START;
			//֪ͨ�ͻ����Կ�ʼ
			for(Socket sk : Server.clients.valueSet())
			{
				prinlnMsg(sk , msg);
			}
		}

	}
	
	//���������䷽��
	public static void creatHouse(Socket socket)
		throws Exception
	{
		UserInfo user = Server.clients.getKeyByValue(socket);
		
		HouseInfo houseInfo;
		//�������з����Ƿ��пշ���
		for (int i = 0; i < Server.houseList.size(); i++ )
		{
			houseInfo = Server.houseList.get(i);
			//����÷�������Ϊ0
			if (houseInfo.getUserNum() == 0)
			{
				jionHouse(i, socket);
				break;
			}
		}
	}

	private static int assignScore(int done , int userNum)
	{
		if(userNum == 4)
		{
			if (done == 100)
			{
				return 30;
			}
			if (done >= 85 && done < 100)
			{
				return 8;
			}
		}
		if(userNum == 3)
		{
			if (done == 100)
			{
				return 27;
			}
			if (done >= 85 && done < 100)
			{
				return 5;
			}
		}
		if(userNum == 2)
		{
			if (done == 100)
			{
				return 20;
			}
			if (done >= 85 && done < 100)
			{
				return 0;
			}
		}

		if (done >= 60 && done < 85)
		{
			return -10;
		}

		if (done >= 0 && done < 60)
		{
			return -20;
		}
		return 0;
	}

	//����ǿ�˵ķ���
	private static void forceExit(HouseInfo houseInfo , UserInfo user)
		throws Exception
	{
		//����÷���ֻʣ��һ����,����Ϸ����
		if(houseInfo.getCrruntNum() == 1)
		{
			
			String msg = GAME_OVER + user.getName() 
				+ SPLIT_SIGN + "-50" + GAME_OVER;
			//֪ͨ���пͻ������û���;�˳�
			for(Socket sk : Server.clients.valueSet())
			{
				prinlnMsg(sk , msg);
			}
			System.out.println("����ǿ��");
			houseInfo.setGameState(false);
		}
	}

	private static void prinlnMsg(Socket socket , String msg)
		throws Exception
	{
		PrintStream ps = new PrintStream(socket.getOutputStream());
		ps.println(msg);
	}
}
