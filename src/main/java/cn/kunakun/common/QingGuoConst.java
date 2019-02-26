package cn.kunakun.common;

public interface QingGuoConst {
	
	 //获取验证码的url
    public static String get_Code = "http://202.196.240.43/jwweb/sys/ValidateCode.aspx";
    //获取cookie的url
    public static String get_Cookie = "http://202.196.240.43/jwweb/";
    //进行登录的url
    public static String do_Login = "http://202.196.240.43/jwweb/_data/home_login.aspx";
    //学生信息url
    public static String get_Student_Info = "http://202.196.240.43/jwweb/xsxj/Stu_MyInfo_RPT.aspx";
    //学生成绩信息
    public static String get_Student_Head = "http://202.196.240.43/jwweb/xscj/Stu_MyScore_rpt.aspx";
    //学生成绩
    public static String get_Student_Score = "http://202.196.240.43/jwweb/xscj/Stu_MyScore_Drawimg.aspx?";
    //课表查询时，有一个隐藏的验证码加密 需要jsoup获取
    public static String get_Class_code = "http://202.196.240.43/jwweb/znpk/Pri_StuSel.aspx";
    //课表查询 获取图片地址
    public static String get_Class_url = "http://202.196.240.43/jwweb/znpk/Pri_StuSel_rpt.aspx?";
    //获取图片
    public static String get_Class_img="http://202.196.240.43/jwweb/znpk/";

}
