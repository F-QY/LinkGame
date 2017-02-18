import java.io.*;
class HouseInfo
{
	//��ǰ��������
	private int crruntNum = 0;
	//�������
	public String[] useName = new String[Server.USER_NUM];
	//���״̬ ׼��(true)/��׼��(false)
	public boolean[] userState = new boolean[Server.USER_NUM];
	//������Ҷ�Ӧ�������
	public PrintStream [] userPrint = new PrintStream[Server.USER_NUM];
	//������ҵ���ɶ�
	private int[] done = new int[Server.USER_NUM];
	
	//��Ϸ״̬
	private boolean playing = false;
	
	public int getCrruntNum()
	{
		return crruntNum;
	}
	
	//�����û�
	public void addUser(int seat , String name , PrintStream ps)
	{
		useName[seat] = name;
		userPrint[seat] = ps;
		if(crruntNum < 4)
		{
			crruntNum ++;
		}
	}
	
	//ɾ���û�
	public void removeUser(int seat)
	{
		useName[seat] = null;
		userPrint[seat] = null;
		userState[seat] = false;
		done[seat] = 0;
		if(crruntNum > 0 )
		{
			crruntNum --;
		}
	}

	public boolean getGameState()
	{
		return playing;
	}
	
	public void setGameState(boolean state)
	{
		playing = state;
	}

	public int getUserNum()
	{
		return crruntNum;
	}

	public void setDone(int seat , int done)
	{
		this.done[seat] = done;
	}

	public int getDone(int seat)
	{
		return done[seat];
	}
	
	//���÷�����Ϣ
	public void reSetInfo()
	{
		for(int i = 0 ;i < Server.USER_NUM ; i++)
		{
			userState[i] = false;
		}
		playing = false;
	}
	
	//������ɶ�
	public void reSetDone()
	{
		for(int i = 0 ;i < Server.USER_NUM ; i++)
		{
			done[i] = 0;
		}
	}
	
}
