package hufs.ces.grimpan.shape;
public interface Observable {
	
	public abstract void RegisterObserver(Observer observer);   // Observer�� �߰�
	public abstract void RemoveObserver(Observer observer);   // Observer ����
	public abstract void NotifyObserver();   // �� ���� ��, Observer�鿡�� �˸�
}
