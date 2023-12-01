package ml.data;

public class InspectData {

	public static void main(String[]args) {
		String path = "data/wines.train";
		DataSet data = new DataSet(path, 1);
		for (int i = 0; i < data.getData().size(); i++) {
			System.out.println(data.getData().get(i).getLabel());
		}
	}
}
