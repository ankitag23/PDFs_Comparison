package mypck;

public class ResultDetails {
	String r1_file_name;
	String r2_file_name;
	String r1_diff_details;
	String r2_diff_details;
	public ResultDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ResultDetails(String r1_file_name, String r2_file_name, String diff_details) {
		super();
		this.r1_file_name = r1_file_name;
		this.r2_file_name = r2_file_name;
		this.r1_diff_details = diff_details;
	}
	public String getR1_file_name() {
		return r1_file_name;
	}
	public void setR1_file_name(String r1_file_name) {
		this.r1_file_name = r1_file_name;
	}
	public String getR2_file_name() {
		return r2_file_name;
	}
	public void setR2_file_name(String r2_file_name) {
		this.r2_file_name = r2_file_name;
	}
	public String getR1_Diff_details() {
		return r1_diff_details;
	}
	public void setR1_Diff_details(String diff_details) {
		this.r1_diff_details = diff_details;
	}
	public String getR2_diff_details() {
		return r2_diff_details;
	}
	public void setR2_diff_details(String r2_diff_details) {
		this.r2_diff_details = r2_diff_details;
	}
	

}
