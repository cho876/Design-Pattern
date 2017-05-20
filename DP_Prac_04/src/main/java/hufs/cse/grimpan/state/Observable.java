package hufs.cse.grimpan.state;

public interface Observable {
	
	public abstract void RegisterObserver(Observer observer);   // Observer 등록 함수
	public abstract void RemoveObserver(Observer observer);   // Observer 삭제 함수
	public abstract void NotifyObserver();   // 상태 변경 시, Observer들에게 호출 함수
}
