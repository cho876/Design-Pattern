package hufs.cse.grimpan.state;

public interface Observer {
	public abstract void update(int num);  // Observer의 상태 update (여기서는 count(도형의 갯수)를 주고 받음)
}
