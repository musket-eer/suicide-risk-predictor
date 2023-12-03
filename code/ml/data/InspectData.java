package ml.data;

public class InspectData {

	public static void main(String[]args) {
		String path = "data/suicide-test.train";
		WordFilter filter = new WordFilter(path);
		DataSet data = new DataSet(path, filter, 1);
		for (int i = 0; i < data.getData().size(); i++) {
			System.out.println(data.getData().get(i).getLabel());
		}
	}
}
