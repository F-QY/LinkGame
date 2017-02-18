import java.util.*;

public class UsMap<K,V> extends HashMap<K,V>
{
	//����value��ɾ��ָ����
	public void removeByValue(Object value)
	{
		for(Object key : keySet())
		{
			if(get(key) == value)
			{
				remove(key);
				break;
			}
		}
	}

	//��ȡ����value��ɵ�set����
	public Set<V> valueSet()
	{
		Set<V> result = new HashSet<V>();
		//��������key��ɵļ���
		for(K key : keySet())
		{	
			//��ÿ��key��Ӧ��value��ӵ�result������
			result.add(get(key));
		}
		return result;
	}
	
	//����value����key
	public K getKeyByValue(V val)
	{
		//��������key��ɵļ���
		for(K key : keySet())
		{
			//���key��Ӧ��value�뱻������value��ͬ
			if(get(key).equals(val)
				&& get(key) == val)
			{
				return key;
			}
		}
		return null;
	}

	//��дHashMap��put����,������value�ظ�
	public V put(K key , V value)
	{
		//��������value��ɵļ���
		for (V val : valueSet())
		{
			if(val.equals(value)
				&& val.hashCode() == value.hashCode())
			{
				throw new RuntimeException
					("UsMapʵ���в��������ظ�value");
			}
		}
		return super.put(key , value);
	}
}