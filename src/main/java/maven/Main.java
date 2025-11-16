package maven;

public class Main {
	public int max(int[] data) {
		int answer = 0;
		int subans = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 1) {
				subans += 1;
			} else {
				answer = Math.max(answer, subans);
				subans = 0;
			}
		}
		return answer;
	}
	public int max(int[] data, int i) {
		
		return 0;
	}
	public int max(int[][] data) {
		int answer = 0;
		
		return answer;
	}
	public static void main(String[] args) throws Exception {
		var main = new Main();
		int[] data = {0,1,1,0,1,1,1,0};
		int[][] mat = {{0,1,1,0,1},{1,1,0,1,0},{0,1,1,1,0},
				{1,1,1,1,0},{1,1,1,1,1},{0,0,0,0,0}};
		System.out.println(main.max(data));
	}
}
