package protocal;
import static protocal.MyProtocol.*;

//����Э�鹤����
public class ParseStr
{
		//ȥ����ͷ�ͽ�β�ͷָ���Э��ķ���
	public static String[] getRealMsg(String line)
	{
		String realMsg = line.substring(
			PROTOCOL_LEN , line.length() - PROTOCOL_LEN);
		//ͨ���ָ�����������
		String[] splitMsg = realMsg.split(SPLIT_SIGN);
		return splitMsg;
	}

	//ֻȥ����ͷ�ͽ�βЭ��ķ���
	public static String getCombMsg(String line)
	{
		return line.substring(PROTOCOL_LEN ,
				line.length() - PROTOCOL_LEN);
	}
}
