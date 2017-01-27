package slack;


public class Runner {

	public static void main(String args[]) {
		ConsumeUsers testConsume = new ConsumeUsers();
		UserObject abc = testConsume.getUserInfo("U2G79FELT");
		System.out.println(abc.isOk());
	}
	
}
