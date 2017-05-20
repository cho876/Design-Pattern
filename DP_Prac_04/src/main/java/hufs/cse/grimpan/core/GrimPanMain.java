/*
 *	 Project # 4
	SPring을 이용한 그림판 구현
	Locale과 Properties file 처리

	1) 자신의 그림판 수정 사용, GrimPanLocale 참고
	2) 메뉴나 타이틀 등 디스플레이되는 텍스트에 locale 적용
	3) GrimPan Property 중에서 default.properties와 
   		default.properties.xml에 필요한 성질 추가(fill=yes,no, fillcolor 등)
 */

package hufs.cse.grimpan.core;

public class GrimPanMain {
	public static void main(String[] args) {
		GrimPanController grimPanController = new GrimPanController();
	}
}
