import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

public class LagouSpider {
    private static boolean clickflg = true; //点击标记变量
    public static void main(String[] args) {
        //设置 Webdriver 路径
        System.setProperty("webdriver.chrome.driver","target/classes/chromedriver.exe");

        //创建 webdriver
        WebDriver webDriver = new ChromeDriver();

        //跳转页面
        webDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");

        //通过 xpath 选择元素
        clickOption(webDriver, "工作经验", "应届毕业生");
        clickOption(webDriver, "学历要求", "本科");
        clickOption(webDriver, "融资阶段", "不限");
        clickOption(webDriver, "公司规模", "不限");
        clickOption(webDriver, "行业领域", "移动互联网");

        //解析元素
        extractJobsByPagination(webDriver);

    }

    private static void extractJobsByPagination(WebDriver webDriver) {
        List<WebElement> jobElements = webDriver.findElements(By.className("con_list_item"));
        for (WebElement jobElement : jobElements) {
            WebElement moneyElement = jobElement.findElement(By.className("position")).findElement(By.className("money"));
            System.out.print(moneyElement.getText()+" ");
            String companyName = jobElement.findElement(By.className("company_name")).getText();
            System.out.println(companyName);
        }

        WebElement nextPageBtn = webDriver.findElement(By.className("pager_next"));
        if(!nextPageBtn.getAttribute("class").contains("pager_next_disabled")){

            //掩盖的元素的去除
            WebElement obscureDiv = webDriver.findElement(By.className("foot-fix-close"));
            if(clickflg){
                obscureDiv.click();
                clickflg = false;
            }
//            //使用显示等待，等待掩盖的div消失
//            WebDriverWait wait = new WebDriverWait(webDriver,60);
//            wait.until(ExpectedConditions.invisibilityOf(obscureDiv));
//            //等待到可点击状态
//            wait.until(ExpectedConditions.elementToBeClickable(nextPageBtn));
            nextPageBtn.click();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            extractJobsByPagination(webDriver);
        }
    }

    private static void clickOption(WebDriver webDriver, String choseTitle, String optionTitle) {
        WebElement chosenElement = webDriver.findElement(By.xpath("//li[@class='multi-chosen']//span[contains(text(),'" + choseTitle + "')]"));
        WebElement optionElement = chosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTitle + "')]"));
        optionElement.click();
    }
}
