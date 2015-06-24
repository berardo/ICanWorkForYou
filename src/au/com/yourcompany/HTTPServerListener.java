package au.com.yourcompany;

public interface HTTPServerListener {
	public void onActionStart(HTTPServer.Status status);
	public void onActionStop(HTTPServer.Status status);
}
