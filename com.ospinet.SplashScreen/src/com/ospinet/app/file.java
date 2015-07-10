package com.ospinet.app;


public class file {

    private String id;
    private String member_id;
    private String file_name;
    private String fname;
    private String lname;

    public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}

    public file() {

    }
    public String getid() {
        return id;
    }
    public void setid(String id) {
        this.id = id;
    }
    public String getmember_id() {
        return member_id;
    }
    public void setmember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getFile_name() {
        return file_name;
    }
    public void setfile_name(String file_name) {
        this.file_name = file_name;
    }
}