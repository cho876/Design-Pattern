package hufs.ces.grimpan.shape;

import hufs.ces.grimpan.core.GrimPanFrameView;

public class ObserverClass implements Observer
{
	public static int count;       // 모든 Observable에서의 count 변화를 하나로 합치기 위해 static 변수로 선언
	private Observable observable;
	
	public ObserverClass(Observable observable)
	{
		this.observable = observable;
		observable.RegisterObserver(this);
	}
	
	@Override
	public void update(int num)
	{
		count += num;                        // 매개변수 num을 Observable로부터 받아올 시, count를 변화시킴
		String text = "Count: "+count;
		GrimPanFrameView.countLbl.setText(text);           // 변화된 count값을 바탕으로 countLbl의 값을 변화시킴 (Count: count(static 변수))
	}
	
	public void display()
	{
		System.out.println("카운트 : " + count);
	}
}