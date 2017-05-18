public class ObserverClass implements Observer
{
	public static int count;       // ��� Observable������ count ��ȭ�� �ϳ��� ��ġ�� ���� static ������ ����
	private Observable observable;
	
	public ObserverClass(Observable observable)
	{
		this.observable = observable;
		observable.RegisterObserver(this);
	}
	
	@Override
	public void update(int num)
	{
		count += num;                        // �Ű����� num�� Observable�κ��� �޾ƿ� ��, count�� ��ȭ��Ŵ
		String text = "Count: "+count;
		GrimPanFrameMain.countLbl.setText(text);           // ��ȭ�� count���� �������� countLbl�� ���� ��ȭ��Ŵ (Count: count(static ����))
	}
	
	public void display()
	{
		System.out.println("ī��Ʈ : " + count);
	}
}
