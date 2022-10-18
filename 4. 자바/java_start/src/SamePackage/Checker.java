package SamePackage;

import OtherPackage.*;

class SameClassChecker {
	public SameClassChecker() {
		this._public();
		this._protected();
		this._default();
		this._private();
	}
	public void _public() {
		System.out.println("public");
	}
	
	protected void _protected() {
		System.out.println("protected");
	}
	
	void _default() {
		System.out.println("default");	
	}
	
	private void _private() {
		System.out.println("private");
	}
}

class SamePackageChecker extends Same {
	public SamePackageChecker() {
		public SamePackageChecker() {
			// 같은 패키지
			Same same = new Same();
			same._public();
			same._protected();
			same._default();
//			same.private();         오류 발생
			
			// 같은 패키지 상속관계
			this._public();
			this._protected();
			this._default();
//			this._private();        오류 발생
		}
	}
}

//https://www.youtube.com/watch?v=XIFB7hisx8w&list=PLuHgQVnccGMCeAy-2-llhw3nWoQKUvQck&index=106
