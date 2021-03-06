package luckyclient.caserun.exwebdriver.extestlink;

import java.io.IOException;

import org.openqa.selenium.WebDriver;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import luckyclient.caserun.exinterface.TestControl;
import luckyclient.caserun.exwebdriver.WebDriverInitialization;
import luckyclient.dblog.DbLink;
import luckyclient.dblog.LogOperation;
import luckyclient.testlinkapi.TestBuildApi;
import luckyclient.testlinkapi.TestCaseApi;

/**
 * =================================================================
 * 这是一个受限制的自由软件！您不能在任何未经允许的前提下对程序代码进行修改和用于商业用途；也不允许对程序代码修改后以任何形式任何目的的再发布。
 * 为了尊重作者的劳动成果，LuckyFrame关键版权信息严禁篡改
 * 有任何疑问欢迎联系作者讨论。 QQ:1573584944  seagull1985
 * =================================================================
 * 
 * @author： seagull
 * @date 2017年12月1日 上午9:29:40
 * 
 */
public class WebOneCaseExecuteTestLink{
	
	@SuppressWarnings("static-access")
	public static void oneCaseExecuteForTast(String projectname,String testCaseExternalId,int version,String taskid){
		//记录日志到数据库
		DbLink.exetype = 0;   
		TestControl.TASKID = taskid;
		int drivertype = LogOperation.querydrivertype(taskid);
		WebDriver wd = null;
		try {
			wd = WebDriverInitialization.setWebDriverForTask(drivertype);
		} catch (IOException e1) {
			luckyclient.publicclass.LogUtil.APP.error("初始化WebDriver出错！", e1);
			e1.printStackTrace();
		}
		LogOperation caselog = new LogOperation(); 
		 //删除旧的用例
		LogOperation.deleteCaseDetail(testCaseExternalId, taskid);
		//删除旧的日志
		LogOperation.deleteCaseLogDetail(testCaseExternalId, taskid);    
		TestBuildApi.getBuild(projectname);
		TestCase testcase = TestCaseApi.getTestCaseByExternalId(testCaseExternalId, version);
		luckyclient.publicclass.LogUtil.APP.info("开始执行用例：【"+testCaseExternalId+"】......");
		try {
			WebCaseExecutionTestLink.caseExcution(projectname,testcase, taskid,wd,caselog);
			luckyclient.publicclass.LogUtil.APP.info("当前用例：【"+testcase.getFullExternalId()+"】执行完成......进入下一条");
		} catch (InterruptedException e) {
			luckyclient.publicclass.LogUtil.APP.error("用户执行过程中抛出异常！", e);
			e.printStackTrace();
		}
        //关闭浏览器
        wd.quit();
	}

}
