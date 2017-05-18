package hufs.ces.grimpan.shape;
public interface Observable {
	
	public abstract void RegisterObserver(Observer observer);   // Observer들 추가
	public abstract void RemoveObserver(Observer observer);   // Observer 삭제
	public abstract void NotifyObserver();   // 값 변경 시, Observer들에게 알림
}
